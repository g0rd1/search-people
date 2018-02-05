package control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import javafx.concurrent.Task;
import model.VK;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class VKRequestAPI {

    public static List<Integer> getUserGroupsIds(final int offset, final int count) {
        try {
            return VK.getVk().groups().get(VK.getUserActor()).offset(offset).count(count).execute().getItems();
        } catch (ApiException | ClientException e) {
            System.err.println("Ошибка при запросе id групп пользователя");
            return null;
        }
    }

    public static Integer getUserGroupsCount() {
        try {
            return VK.getVk().groups().get(VK.getUserActor()).execute().getCount();
        } catch (ApiException | ClientException e) {
            System.err.println("Ошибка при запросе количества групп пользователя");
            return null;
        }
    }

//    public static List<Integer> getGroupMembersId(final int offset, final int count, final String groupId){
//        try {
//            return vk.groups().getMembers(userActor).offset(offset).count(count).groupId(groupId).execute().getItems();
//        } catch (ApiException | ClientException e) {
//            System.err.println("Ошибка при запросе id членов группы");
//            return null;
//        }
//    }

    public static int getGroupMembersCount(final String groupId) {
        try {
            return VK.getVk().groups().getMembers(VK.getUserActor()).groupId(groupId).execute().getCount();
        } catch (ApiException | ClientException e) {
            System.err.println("Ошибка при запросе количества членов группы");
            return 0;
        }
    }

//    public static void testMethod() throws ClientException, ApiException {
//        //42532708 - T403
//        //22751485 - dvach
//        StringBuilder sb = new StringBuilder();
//        sb.append("var a, i, offset, b=[]; ");
//        sb.append("i=0; ");
//        sb.append("offset=0; ");
//        sb.append("while (i<25) {");
//        sb.append("a=API.groups.getMembers({\"group_id\": 42532708, \"offset\": i*1000+offset*25000, \"count\": 1000, \"fields\": \"bdate\"}); ");
//        sb.append("i=i+1; ");
//        sb.append("b.push(a); ");
//        sb.append("}");
//        sb.append("return b;");
//        String code = sb.toString();
//        System.out.println(code);
//        JsonArray result = VK.getVk().execute().code(VK.getUserActor(), code).execute().getAsJsonArray();
//        for (JsonElement element :
//                result) {
//            JsonArray array = element.getAsJsonObject().getAsJsonArray("items");
//            for (JsonElement item :
//                    array) {
//                int id = item.getAsJsonObject().getAsJsonPrimitive("id").getAsInt();
//                System.out.println(id);
//            }
//        }
//
//    }

    public static List<List<JsonObject>> getGroupMembersInfoObjects(final int offset, final String groupId) {
        String code = codeBuilder(offset, groupId);
        JsonArray response = null;
        try {
            response = VK.getVk().execute().code(VK.getUserActor(), code).execute().getAsJsonArray();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        List<List<JsonObject>> infoObjects = new ArrayList<>();
        final JsonArray finalResponse = response;
        for (JsonArray infoObjectArray : getInfoObjectArraysFromResponse(finalResponse)) {
            infoObjects.add(getInfoObjectsFromInfoObjectArray(infoObjectArray));
        }
        return infoObjects;
    }

    private static List<JsonArray> getInfoObjectArraysFromResponse(final JsonArray response) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<JsonArray> InfoObjectArrays = new ArrayList<>();
        List<Future<JsonArray>> futureList = new ArrayList<>();
        for (JsonElement element :
                response) {
            Callable<JsonArray> task = () -> element.getAsJsonObject().getAsJsonArray("items");
            Future<JsonArray> future = executorService.submit(task);
            futureList.add(future);
        }
        for (Future<JsonArray> future : futureList) {
            try {
                InfoObjectArrays.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return InfoObjectArrays;
    }

    private static List<JsonObject> getInfoObjectsFromInfoObjectArray(final JsonArray infoObjectArray) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<JsonObject> InfoObjects = new ArrayList<>();
        List<Future<JsonObject>> futureList = new ArrayList<>();
        for (JsonElement InfoObject : infoObjectArray) {
            Callable<JsonObject> task = () -> InfoObject.getAsJsonObject();
            Future<JsonObject> future = executorService.submit(task);
            futureList.add(future);
        }
        for (Future<JsonObject> future : futureList) {
            try {
                InfoObjects.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return InfoObjects;
    }

    private static String codeBuilder(final int offset, final String groupId) {
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

}
