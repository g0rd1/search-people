package control;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.View;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ViewController implements Initializable {

    @FXML
    private WebView webView1;

    @FXML
    private Button button1;

    @FXML
    private ProgressBar progressBar1;

    @FXML
    private ProgressBar progressBar2;

    public static void setButton1Visible(final boolean visible) {
        View.getButton1().setVisible(visible);
    }

    public static void setWebView1Visible(final boolean visible) {
        View.getWebView1().setVisible(visible);
    }

    public static void setProgressBar1Progress(final float progress) {
        View.getProgressBar1().setProgress(progress);
    }

    public static void setProgressBar2Progress(final float progress) {
        View.getProgressBar2().setProgress(progress);
    }

    public static WebEngine getWebEngine() {
        return View.getWebView1().getEngine();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeView();
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

    private void initializeView() {
        View.setWebView1(webView1);
        View.setButton1(button1);
        View.setProgressBar1(progressBar1);
        View.setProgressBar2(progressBar2);
    }

}
