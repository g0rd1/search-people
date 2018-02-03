package model;

import com.google.gson.JsonObject;

import javax.sql.rowset.serial.SerialRef;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class User {

        private int userId;

        private Date birthDate;

        private String city;

        private int sex;

        private String firstName;

        private String lastName;

        private User(UserBuilder rb) {

                userId = rb.userId;
                birthDate = rb.birthDate;
                city = rb.city;
                sex = rb.sex;
                firstName = rb.firstName;
                lastName = rb.lastName;

        }

        public User(final JsonObject item) throws Exception {

                if (item.getAsJsonPrimitive("id") != null) {
                        userId = item.getAsJsonPrimitive("id").getAsInt();
                } else {
                        throw new Exception("Ошибка при получении id пользователя");
                }

                if (item.getAsJsonObject("city") != null) {
                        city = item.getAsJsonObject("city").getAsJsonPrimitive("title").getAsString();
                } else {
                        city = null;
                }

                if (item.getAsJsonPrimitive("sex") != null) {
                        sex = item.getAsJsonPrimitive("sex").getAsInt();
                } else {
                        throw new Exception("Ошибка при получении пола пользователя");
                }

                if (item.getAsJsonPrimitive("first_name") != null) {
                        firstName = item.getAsJsonPrimitive("first_name").getAsString();
                } else {
                        throw new Exception("Ошибка при получении имени пользователя");
                }

                if (item.getAsJsonPrimitive("last_name") != null) {
                        lastName = item.getAsJsonPrimitive("last_name").getAsString();
                } else {
                        throw new Exception("Ошибка при получении фамилии пользователя");
                }

                if (item.getAsJsonPrimitive("bdate") != null) {
                        try {
                                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                                java.util.Date tempDate = format.parse(item.getAsJsonPrimitive("bdate").getAsString());
                                birthDate = new Date(tempDate.getTime());
                        } catch (ParseException e) {
                                birthDate = null;
//                                System.out.println(item.getAsJsonPrimitive("bdate").getAsString());
//                                System.err.println("Ошибка при получении дня рождения пользователя");
                        }
                } else {
                        birthDate = null;
                }

        }

        public int getUserId() {
                return userId;
        }

        public Date getBirthDate() {
                return birthDate;
        }

        public String getCity() {
                return city;
        }

        public int getSex() {
                return sex;
        }

        public String getFirstName() {
                return firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public static class UserBuilder {

                private int userId;

                private Date birthDate;

                private String city;

                private int sex;

                private String firstName;

                private String lastName;

                public UserBuilder setUserId(final int userId) {
                        this.userId = userId;
                        return this;
                }

                public UserBuilder setBirthDate(final Date birthDate) {
                        this.birthDate = birthDate;
                        return this;
                }

                public UserBuilder setCity(final String city) {
                        this.city = city;
                        return this;
                }

                public UserBuilder setSex(final int sex) {
                        this.sex = sex;
                        return this;
                }

                public UserBuilder setFirstName(final String firstName) {
                        this.firstName = firstName;
                        return this;
                }

                public UserBuilder setLastName(final String lastName) {
                        this.lastName = lastName;
                        return this;
                }

                public User build(){
                        return new User(this);
                }

        }

}
