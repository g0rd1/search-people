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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Future;

public class ViewController implements Initializable {

    @FXML
    private WebView webView1;

    @FXML
    private Button button1;

    @FXML
    private ProgressBar ProgressBar1;

    @FXML
    private ProgressBar ProgressBar2;

    public void setButton1Visible(final boolean visible) {
        button1.setVisible(visible);
    }

    public void setWebView1Visible(final boolean visible) {
        webView1.setVisible(visible);
    }

    public void setProgressBar1Progress(final float progress) {
        ProgressBar1.setProgress(progress);
    }

    public void setProgressBar2Progress(final float progress) {
        ProgressBar2.setProgress(progress);
    }

    public WebEngine getWebView1Engine() {
        return webView1.getEngine();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        button1.setVisible(false);
        DatabaseController.initializeDatabase();
        VKAuthorizationAPI.initializeVKAPI();
    }

    public void UpdateData() {
        Task task = new Task() {
            @Override
            protected Object call() {
                Controller.UpdateData();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

}
