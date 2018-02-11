package control;

import com.google.gson.JsonObject;
import model.Database;
import model.User;

import java.io.File;
import java.sql.*;
import java.util.List;

public class DatabaseController {

    public static void initializeDatabase() {

        final String curDir = new File("").getAbsolutePath();
        final String URL = "jdbc:hsqldb:file:" + curDir + "\\src\\main\\resources\\base\\base";
        try {
            Database.setConnection(DriverManager.getConnection(URL, "SA", ""));
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных!");
        }

    }

    public static boolean isTableExist() {
        try {
            Database.getConnection().createStatement().execute("SELECT * FROM PUBLIC.USER");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void createTable () {
        if (isTableExist()) {
            System.err.println("Создание не было произведено, так как таблица уже существует");
            return;
        }
        try {
            Database.getConnection().createStatement().execute("CREATE TABLE USER(USER_ID int not null, " +
                                                                        "GROUP_ID varchar(40) not null, " +
                                                                        "BIRTH_DATE date, " +
                                                                        "CITY varchar(40)," +
                                                                        "SEX int," +
                                                                        "FIRST_NAME varchar(40)," +
                                                                        "LAST_NAME varchar(40)" +
                                                                        ");");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы в базе данных!");
        }
    }

    public static void deleteTable() {
        if (!isTableExist()) {
            System.err.println("Удаление не было произведено, так как таблица не создана");
            return;
        }
        try {
            Database.getConnection().createStatement().execute("DROP TABLE PUBLIC.USER");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы в базе данных!");
        }
    }

    public static void insertRecord(final User user, final String groupId) {
        try {
            PreparedStatement preparedStatement = Database.getConnection().prepareStatement("INSERT INTO PUBLIC.USER (USER_ID, " +
                                                                                                            "GROUP_ID, " +
                                                                                                            "BIRTH_DATE, " +
                                                                                                            "CITY, " +
                                                                                                            "SEX, " +
                                                                                                            "FIRST_NAME, " +
                                                                                                            "LAST_NAME) " +
                                                                                    "VALUES (?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.setString(2, groupId);
            preparedStatement.setDate(3, user.getBirthDate());
            preparedStatement.setString(4, user.getCity());
            preparedStatement.setInt(5, user.getSex());
            preparedStatement.setString(6, user.getFirstName());
            preparedStatement.setString(7, user.getLastName());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Ошибка при записи данных в таблицу!");
        }
    }

    public static void insertAll(final List<List<JsonObject>> infoObjects, final String groupId){
        for (List<JsonObject> objectList : infoObjects) {
            for (JsonObject infoObject : objectList) {
                try {
                    insertRecord(User.parseInfoObject(infoObject), groupId);
                } catch (Exception e) {
                    System.err.println("Ошибка при преобразовании InfoObject в User");
                }
            }
        }
    }

    public static void closeConnection(){
        try {
            Database.getConnection().close();
        } catch (SQLException e) {
            System.err.println("Ошибка при попытки закрытия соединения с базой данных!");
        }
    }

}
