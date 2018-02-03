package model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void getUserId() {

        String groupId = "123456";

        String json1 = "{" +
                        "\"id\": 172310," +
                        "\"first_name\": \"Алексей\"," +
                        "\"last_name\": \"Есенин\"," +
                        "\"sex\": 2," +
                        "\"bdate\": \"31.10.1988\"," +
                        "\"city\": {" +
                        "\"id\": 2," +
                        "\"title\": \"Санкт-Петербург\"" +
                        "}" +
                        "}";

        System.out.println(json1);
        JsonObject jsonObject1 = new JsonParser().parse(json1).getAsJsonObject();

        try {
            new User(jsonObject1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String json2 = "{" +
                        "\"id\": 27974925," +
                        "\"first_name\": \"Александра\"," +
                        "\"last_name\": \"Шлапунова\"," +
                        "\"deactivated\": \"banned\"," +
                        "\"sex\": 1" +
                        "}";

        System.out.println(json2);
        JsonObject jsonObject2 = new JsonParser().parse(json2).getAsJsonObject();
        try {
            new User(jsonObject2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}