package control;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import model.Group;
import model.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Controller {

    private final static int GROUP_COUNT_TO_RECORD = 10;

    private final static int GROUPS_PER_REQUEST = 25_000;

    private final static int CORE_POOL_SIZE = 8;

    private final static float INITIAL_PROGRESS = 0;

    private final static long period = 1_200_000_000L;

    private static int counter = -1;

    private static long start = 0L;

    private static long end = 0L;

    public static void UpdateData() {
        float generalProgress = 0;
        float groupProgress = 0;
        List<List<Future>> groupsFutureLists = new ArrayList<>();
        DatabaseController.deleteTable();
        DatabaseController.createTable();
        List<Group> groups = VKRequestAPI.getUserGroups(GROUP_COUNT_TO_RECORD);
        for (Group group : groups) {
            groupsFutureLists.add(getGroupFutureList(group));
        }
        ViewController.setProgressBar1Progress(INITIAL_PROGRESS);
        for (List<Future> groupFutureList : groupsFutureLists) {
            ViewController.setProgressBar2Progress(INITIAL_PROGRESS);
            for (Future future : groupFutureList) {
                try {
                    future.get();
                    ViewController.setProgressBar2Progress(++groupProgress / (float) groupFutureList.size());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Ошибка в работе потока");
                }
            }
            groupProgress = 0;
            ViewController.setProgressBar1Progress(++generalProgress / (float) groupsFutureLists.size());
        }
    }

    private static List<Future> getGroupFutureList(final Group group) {
        ExecutorService executorService = Executors.newFixedThreadPool(CORE_POOL_SIZE);
        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i * GROUPS_PER_REQUEST < group.getMembersCount(); i++) {
            final int offset = i * GROUPS_PER_REQUEST;
            try {
                counter = controlTime(counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Runnable task = () -> {
                final List<List<JsonObject>> InfoObjects = VKRequestAPI.getGroupMembersInfoObjects(offset, group.getId());
                DatabaseController.insertAll(InfoObjects, group.getId());
            };
            Future future = executorService.submit(task);
            futureList.add(future);
        }
        return futureList;
    }

    private static int controlTime(final int counter) throws InterruptedException {
        if (counter < 1) {
            start = System.nanoTime();
        }
        if (counter > 1) {
            end = System.nanoTime();
            TimeUnit.NANOSECONDS.sleep(  period - (end - start));
            return 0;
        }
        return counter + 1;
    }

}
