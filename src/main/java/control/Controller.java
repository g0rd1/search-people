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

    private final static int REQUESTS_PERIOD = 350;

    private final static int CORE_POOL_SIZE = 16;

    private final static float INITIAL_PROGRESS = 0;

    private static int delay = 0;

    public static void UpdateData() {
        float generalProgress = 0;
        float groupProgress = 0;
        List<List<ScheduledFuture>> groupsFutureLists = new ArrayList<>();
        DatabaseController.deleteTable();
        DatabaseController.createTable();
        List<Group> groups = VKRequestAPI.getUserGroups(GROUP_COUNT_TO_RECORD);
        for (Group group : groups) {
            groupsFutureLists.add(getGroupFutureList(group));
        }
        ViewController.setProgressBar1Progress(INITIAL_PROGRESS);
        for (List<ScheduledFuture> groupFutureList : groupsFutureLists) {
            ViewController.setProgressBar2Progress(INITIAL_PROGRESS);
            for (ScheduledFuture future : groupFutureList) {
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
        delay = 0;
    }

    private static List<ScheduledFuture> getGroupFutureList(final Group group) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        List<ScheduledFuture> futureList = new ArrayList<>();
        for (int i = 0; i * GROUPS_PER_REQUEST < group.getMembersCount(); i++) {
            final int offset = i * GROUPS_PER_REQUEST;
            delay += REQUESTS_PERIOD;
            Runnable task = () -> {
                final List<List<JsonObject>> InfoObjects = VKRequestAPI.getGroupMembersInfoObjects(offset, group.getId());
                DatabaseController.insertAll(InfoObjects, group.getId());
            };
            ScheduledFuture scheduledFuture = executorService.schedule(task, delay, TimeUnit.MILLISECONDS);
            futureList.add(scheduledFuture);
        }
        return futureList;
    }

}
