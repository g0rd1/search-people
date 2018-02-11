package control;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import javafx.scene.web.WebEngine;
import model.VK;

public class VKAuthorizationAPI {

    private static final Integer APP_ID = 5884308, VK_APP_MASK = 8;

    private static final String CLIENT_SECRET = "5vgcL7tw88TsS74o4YWc";

    private static final String REDIRECT_URL = "https://oauth.vk.com/blank.html";

    private static final String VK_AUTH_URL = "https://oauth.vk.com/authorize?client_id=" + APP_ID + "&display=page&response_type=code&scope=" + VK_APP_MASK + "&redirect_url=" + REDIRECT_URL;

    private static final String REDIRECT_URI = "";

    public static void initializeVKAPI(final WebEngine engine) {
        setVkApiClient();
        setUserActor(engine);
    }

    private static void setVkApiClient() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VK.setVk(new VkApiClient(transportClient));
    }

    private static void setUserActor(final WebEngine engine) {
        final ViewController controller = new ViewController();
        engine.load(VK_AUTH_URL);

        engine.locationProperty().addListener((observable, oldValue, newValue) -> {

            String accessCode = null;

            if (newValue.startsWith(REDIRECT_URL)) {
                String[] urlParts = newValue.split("=|&");
                if (urlParts.length < 2) {
                    System.out.println("Ошибка авторизации!");
                } else {
                    accessCode = urlParts[1];
                }

                try {
                    UserAuthResponse authResponse = VK.getVk().oauth()
                            .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, accessCode)
                            .execute();
                    VK.setUserActor(new UserActor(authResponse.getUserId(), authResponse.getAccessToken()));
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                }

                controller.setWebView1Visible(false);
                controller.setButton1Visible(false);
            }
        });
    }

    //    private static void getServiceActor(Controller controller){
    //        try {
    //            ServiceClientCredentialsFlowResponse authResponse = vk.oauth()
    //                    .serviceClientCredentialsFlow(APP_ID, CLIENT_SECRET)
    //                    .execute();
    //            serviceActor = new ServiceActor(APP_ID, CLIENT_SECRET, authResponse.getAccessToken());
    //        } catch (ApiException | ClientException e) {
    //            e.printStackTrace();
    //        }
    //        controller.getWebView1().setVisible(false);
    //        controller.getButton1().setVisible(true);
    //    }
}
