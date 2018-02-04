package control;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import model.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane AnchorPane;

    @FXML
    private WebView webView1;

    @FXML
    private Button button1;

    @FXML
    private ProgressBar ProgressBar1;

    @FXML
    private ProgressBar ProgressBar2;

    public WebView getWebView1() {
        return webView1;
    }

    public Button getButton1() {
        return button1;
    }

    final int GROUP_COUNT_TO_RECORD = 2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button1.setVisible(false);
        DatabaseController.initializeDatabase();
        VKAuthorizationAPI.initializeVKAPI(this);
    }

    public void UpdateData(MouseEvent mouseEvent) {
        int recordedGroupCount = 0;
        ProgressBar1.setProgress(0);
        DatabaseController.deleteTable();
        DatabaseController.createTable();
        List<Integer> groupsIds = VKRequestAPI.getUserGroupsIds(0, GROUP_COUNT_TO_RECORD);
        for (int groupId : groupsIds) {
            insertRecordInDataBase(String.valueOf(groupId));
            final int finalRecordedGroupCount = recordedGroupCount++;
            Platform.runLater(() -> {
                ProgressBar1.setProgress((float) finalRecordedGroupCount / (float) GROUP_COUNT_TO_RECORD);
                AnchorPane.requestLayout();
            });
        }
        ProgressBar1.setProgress(0);
    }

    public void insertRecordInDataBase(final String groupId){
        final Integer groupMembersCount = VKRequestAPI.getGroupMembersCount(String.valueOf(groupId));
        ProgressBar2.setProgress(0);
        for (int offset = 0; offset < groupMembersCount; offset+=25000) {
            final List<List<JsonObject>> InfoObjects = VKRequestAPI.getGroupMembersInfoObjects(offset, groupId);
            DatabaseController.insertAll(InfoObjects, groupId);
            final int finalOffset = offset;
            Platform.runLater(() -> {
                ProgressBar2.setProgress((float) finalOffset / (float) groupMembersCount);
                AnchorPane.requestLayout();
            });
        }
        ProgressBar2.setProgress(0);
    }
}
