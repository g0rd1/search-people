package model;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import control.ViewController;
import javafx.scene.web.WebEngine;

public class VK {

    private static UserActor userActor;

    private static VkApiClient vk;

    public static void setUserActor(final UserActor userActor) {
        VK.userActor = userActor;
    }

    public static void setVk(final VkApiClient vk) {
        VK.vk = vk;
    }

    public static UserActor getUserActor() {
        return userActor;
    }

    public static VkApiClient getVk() {
        return vk;
    }

}
