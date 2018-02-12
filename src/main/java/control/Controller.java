package control;

import com.google.gson.JsonObject;
import javafx.scene.layout.AnchorPane;
import model.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Controller {

    final static int GROUP_COUNT_TO_RECORD = 5;

    final static int GROUPS_PER_REQUEST = 25_000;

    final static int REQUESTS_PERIOD = 400;

    final static int CORE_POOL_SIZE = 16;

    final static float INITIAL_PROGRESS = 0;

    final static float COMPLETED_PROGRESS = 1;

    public static void UpdateData() {
        int recordedGroupCount = 0;
        ViewController.setProgressBar1Progress(COMPLETED_PROGRESS);
        DatabaseController.deleteTable();
        DatabaseController.createTable();
        List<Group> groups = VKRequestAPI.getUserGroups(0, GROUP_COUNT_TO_RECORD);
        for (Group group : groups) {
            insertRecordInDataBase(group);
            ViewController.setProgressBar1Progress((float) ++recordedGroupCount / (float) GROUP_COUNT_TO_RECORD);
        }
        ViewController.setProgressBar1Progress(1);
    }

    private static void insertRecordInDataBase(final Group group){
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        List<ScheduledFuture<List<List<JsonObject>>>> futureList = new ArrayList<>();
        ViewController.setProgressBar2Progress(INITIAL_PROGRESS);
        for (int i = 0;  i * GROUPS_PER_REQUEST < group.getMembersCount(); i++) {
            final int offset = i * GROUPS_PER_REQUEST;
            final int delay = i * REQUESTS_PERIOD;
            Callable task = () -> VKRequestAPI.getGroupMembersInfoObjects(offset, group.getId());
            ScheduledFuture<List<List<JsonObject>>> scheduledFuture = executorService.schedule(task, delay, TimeUnit.MILLISECONDS);
            futureList.add(scheduledFuture);
        }
        for (ScheduledFuture<List<List<JsonObject>>>  future : futureList) {
            try {
                final List<List<JsonObject>> InfoObjects = future.get();
                DatabaseController.insertAll(InfoObjects, group.getId());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Ошибка при получении списка с списком объектов из потока");
            } finally {
                executorService.shutdown();
            }
        }
        ViewController.setProgressBar2Progress(COMPLETED_PROGRESS);
    }

}
