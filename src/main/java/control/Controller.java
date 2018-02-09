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
import java.util.concurrent.Future;

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

    final int GROUP_COUNT_TO_RECORD = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button1.setVisible(false);
        DatabaseController.initializeDatabase();
        VK.initializeVKAPI(this);
    }

    public void UpdateData(MouseEvent mouseEvent) {
        Task task = new Task() {
            @Override
            protected Object call() {
                int recordedGroupCount = 0;
                ProgressBar1.setProgress(0);
                DatabaseController.deleteTable();
                DatabaseController.createTable();
                List<Group> groups = VKRequestAPI.getUserGroups(0, GROUP_COUNT_TO_RECORD);
                for (Group group : groups) {
                    insertRecordInDataBase(group);
                    final int finalRecordedGroupCount = recordedGroupCount++;
                    ProgressBar1.setProgress((float) finalRecordedGroupCount / (float) GROUP_COUNT_TO_RECORD);
                    AnchorPane.requestLayout();
                }
                ProgressBar1.setProgress(100);
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void insertRecordInDataBase(final Group group){
        ProgressBar2.setProgress(0);
        for (int offset = 0; offset < group.getMembersCount(); offset+=25000) {
            final int finalOffset = offset;
            final List<List<JsonObject>> InfoObjects = VKRequestAPI.getGroupMembersInfoObjects(finalOffset, group.getId());
            DatabaseController.insertAll(InfoObjects, group.getId());
            ProgressBar2.setProgress((float) finalOffset / (float) group.getMembersCount());
            AnchorPane.requestLayout();
        }
        ProgressBar2.setProgress(100);
    }

}
