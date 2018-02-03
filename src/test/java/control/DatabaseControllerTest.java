package control;

import model.User;
import org.junit.Test;

import java.sql.Date;

public class DatabaseControllerTest {

    @Test
    public void initializeDatabase() {
        DatabaseController.initializeDatabase();
        DatabaseController.closeConnection();
    }

    @Test
    public void createTable() {
        DatabaseController.initializeDatabase();
        DatabaseController.createTable();
        DatabaseController.closeConnection();
    }

    @Test
    public void deleteTable() {
        DatabaseController.initializeDatabase();
        DatabaseController.deleteTable();
        DatabaseController.closeConnection();
    }

    @Test
    public void insertRecord() {

        User user = new User.UserBuilder().setUserId(12345)
                                                    .setBirthDate(new Date(1994, 4, 13))
                                                    .setCity("Kurgan")
                                                    .setSex(2)
                                                    .setFirstName("Igor")
                                                    .setLastName("Gordeev")
                                                    .build();
        User nullUser = new User.UserBuilder().setUserId(12345)
                                                .setBirthDate(null)
                                                .setCity(null)
                                                .setSex(2)
                                                .setFirstName(null)
                                                .setLastName(null)
                                                .build();
        DatabaseController.initializeDatabase();
        DatabaseController.createTable();
        DatabaseController.insertRecord(user, "123123");
        DatabaseController.insertRecord(nullUser, "123123");
        DatabaseController.deleteTable();
        DatabaseController.closeConnection();

    }

}