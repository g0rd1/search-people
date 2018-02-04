package control;

import com.google.gson.JsonObject;
import javafx.concurrent.Task;
import model.Base;
import model.User;
import org.hsqldb.Database;
import org.hsqldb.Table;

import java.io.File;
import java.sql.*;
import java.util.List;

public class DatabaseController {

    private static Connection connection = Base.getConnection();
    private static Table table = Base.getTable();
    private static Database database = Base.getDatabase();

    public static void initializeDatabase() {

        final String curDir = new File("").getAbsolutePath();
        final String URL = "jdbc:hsqldb:file:" + curDir + "\\src\\main\\resources\\base\\base";
        try {
            connection = DriverManager.getConnection(URL, "SA", "");
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных!");
        }

    }

    public static boolean isTableExist() {
        try {
            connection.createStatement().execute("SELECT * FROM PUBLIC.USER");
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
            connection.createStatement().execute("CREATE TABLE USER(USER_ID int not null, " +
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
            connection.createStatement().execute("DROP TABLE PUBLIC.USER");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы в базе данных!");
        }
    }

    public static void insertRecord(final User user, final String groupId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PUBLIC.USER (USER_ID, " +
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
        Task task = new Task() {
            @Override
            protected Object call() {
                for (List<JsonObject> objectList : infoObjects) {
                    for (JsonObject infoObject : objectList) {
                        try {
                            insertRecord(new User(infoObject), groupId);
                        } catch (Exception e) {
                            System.err.println("Ошибка при преобразовании InfoObject в User");
                        }
                    }
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    public static void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Ошибка при попытки закрытия соединения с базой данных!");
        }
    }

}
