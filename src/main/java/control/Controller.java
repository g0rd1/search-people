package control;

import com.google.gson.JsonObject;
import javafx.scene.layout.AnchorPane;
import model.Group;

import java.util.List;

public class Controller {

    final static int GROUP_COUNT_TO_RECORD = 1;

    public static void UpdateData() {
        ViewController controller = new ViewController();
        int recordedGroupCount = 0;
        controller.setProgressBar1Progress(0);
        DatabaseController.deleteTable();
        DatabaseController.createTable();
        List<Group> groups = VKRequestAPI.getUserGroups(0, GROUP_COUNT_TO_RECORD);
        for (Group group : groups) {
            insertRecordInDataBase(group);
            final int finalRecordedGroupCount = recordedGroupCount++;
            controller.setProgressBar1Progress((float) finalRecordedGroupCount / (float) GROUP_COUNT_TO_RECORD);
        }
        controller.setProgressBar1Progress(100);
    }

    public static void insertRecordInDataBase(final Group group){
        ViewController controller = new ViewController();
        controller.setProgressBar2Progress(0);
        for (int offset = 0; offset < group.getMembersCount(); offset+=25000) {
            final int finalOffset = offset;
            final List<List<JsonObject>> InfoObjects = VKRequestAPI.getGroupMembersInfoObjects(finalOffset, group.getId());
            DatabaseController.insertAll(InfoObjects, group.getId());
            controller.setProgressBar2Progress((float) finalOffset / (float) group.getMembersCount());
        }
        controller.setProgressBar2Progress(100);
    }

}
