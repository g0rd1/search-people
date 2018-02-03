package model;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import control.VKAuthorizationAPI;

public class VK {

    private static UserActor userActor;
    private static ServiceActor serviceActor;
    private static VkApiClient vk;

    public static ServiceActor getServiceActor() {
        return serviceActor;
    }

    public static UserActor getUserActor() {
        return userActor;
    }

    public static VkApiClient getVk() {
        return vk;
    }

    public static void setUserActor(UserActor userActor) {
        VK.userActor = userActor;
    }

    public static void setVk(VkApiClient vk) {
        VK.vk = vk;
    }
}
