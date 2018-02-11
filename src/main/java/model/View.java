package model;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.web.WebView;

public class View {

    private static WebView webView1;

    private static Button button1;

    private static ProgressBar progressBar1;

    private static ProgressBar progressBar2;

    public static WebView getWebView1() {
        return webView1;
    }

    public static void setWebView1(WebView webView1) {
        View.webView1 = webView1;
    }

    public static Button getButton1() {
        return button1;
    }

    public static void setButton1(Button button1) {
        View.button1 = button1;
    }

    public static ProgressBar getProgressBar1() {
        return progressBar1;
    }

    public static void setProgressBar1(ProgressBar progressBar1) {
        View.progressBar1 = progressBar1;
    }

    public static ProgressBar getProgressBar2() {
        return progressBar2;
    }

    public static void setProgressBar2(ProgressBar progressBar2) {
        View.progressBar2 = progressBar2;
    }
}
