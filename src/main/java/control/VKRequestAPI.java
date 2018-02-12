package control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import model.Group;
import model.VK;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VKRequestAPI {

    public static List<Group> getUserGroups(final int offset, final int count) {
        List<Group> userGroups = new ArrayList<>();
        try {
            final String code = buildCodeToGetGroups(offset, count);
            final JsonObject response = VK.getVk().execute().code(VK.getUserActor(), code).execute().getAsJsonObject();
            final JsonArray items = response.getAsJsonArray("items");
            for (JsonElement element : items) {
                final Group group = Group.parseInfoObject(element.getAsJsonObject());
                if (group != null) {
                    userGroups.add(group);
                }
            }
            TimeUnit.MILLISECONDS.sleep(335); // 3 запроса в секунду
            return userGroups;
        } catch (ApiException | ClientException e) {
            System.err.println("Ошибка при запросе получении групп пользователя");
        } catch (InterruptedException e) {
            System.err.println("Ошибка при ожидании");
        }
        return null;
    }

    public static List<List<JsonObject>> getGroupMembersInfoObjects(final int offset, final String groupId) {
        String code = buildCodeToGetMemebers(offset, groupId);
        JsonArray response = null;
        try {
            response = VK.getVk().execute().code(VK.getUserActor(), code).execute().getAsJsonArray();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        List<List<JsonObject>> infoObjects = new ArrayList<>();
        final JsonArray finalResponse = response;
        for (JsonArray infoObjectArray : getItemsArraysFromResponse(finalResponse)) {
            infoObjects.add(getInfoObjectsFromInfoObjectArray(infoObjectArray));
        }
        return infoObjects;
    }

    private static List<JsonArray> getItemsArraysFromResponse(final JsonArray response) {
        List<JsonArray> ItemsArrays = new ArrayList<>();
        for (JsonElement element :
                response) {
            ItemsArrays.add(element.getAsJsonObject().getAsJsonArray("items"));
        }
        return ItemsArrays;
    }

    private static List<JsonObject> getInfoObjectsFromInfoObjectArray(final JsonArray infoObjectArray) {
        List<JsonObject> InfoObjects = new ArrayList<>();
        for (JsonElement InfoObject : infoObjectArray) {
            InfoObjects.add(InfoObject.getAsJsonObject());
        }
        return InfoObjects;
    }

    private static String buildCodeToGetMemebers(final int offset, final String groupId) {
        StringBuilder sb = new StringBuilder();
        sb.append("var a, i, offset, b=[]; ");
        sb.append("i=0; ");
        sb.append("offset=");
        sb.append(offset);
        sb.append("; ");
        sb.append("while (i<25) {");
        sb.append("a=API.groups.getMembers({\"group_id\": ");
        sb.append(groupId);
        sb.append(", \"offset\": i*1000+offset, \"count\": 1000, \"fields\": \"bdate, city, sex\"}); ");
        sb.append("i=i+1; ");
        sb.append("b.push(a); ");
        sb.append("}");
        sb.append("return b;");
        return sb.toString();
    }

    private static String buildCodeToGetGroups(final int offset, final int count) {
        StringBuilder sb = new StringBuilder();
        sb.append("return API.groups.get({\"extended\": true, \"fields\": \"members_count\", \"offset\": ");
        sb.append(offset);
        sb.append(", \"count\": ");
        sb.append(count);
        sb.append("});");
        return sb.toString();
    }
}
