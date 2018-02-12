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

    final private static int MAX_LENGTH = 40;

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

    public static User parseInfoObject(final JsonObject infoObject) throws Exception {

        UserBuilder ub = new UserBuilder();

        if (infoObject.getAsJsonPrimitive("id") != null) {
            ub.setUserId(infoObject.getAsJsonPrimitive("id").getAsInt());
        } else {
            throw new Exception("Ошибка при получении id пользователя");
        }

        if (infoObject.getAsJsonObject("city") != null) {
            ub.setCity(infoObject.getAsJsonObject("city").getAsJsonPrimitive("title").getAsString());
        } else {
            ub.setCity(null);
        }

        if (infoObject.getAsJsonPrimitive("sex") != null) {
            ub.setSex(infoObject.getAsJsonPrimitive("sex").getAsInt());
        } else {
            throw new Exception("Ошибка при получении пола пользователя");
        }

        if (infoObject.getAsJsonPrimitive("first_name") != null) {
            final String firstName = infoObject.getAsJsonPrimitive("first_name").getAsString();
            if (firstName.length() > MAX_LENGTH) {
                ub.setFirstName(firstName.substring(0, MAX_LENGTH));
            } else {
                ub.setFirstName(firstName);
            }
        } else {
            throw new Exception("Ошибка при получении имени пользователя");
        }

        if (infoObject.getAsJsonPrimitive("last_name") != null) {
            final String lastName = infoObject.getAsJsonPrimitive("last_name").getAsString();
            if (lastName.length() > MAX_LENGTH) {
                ub.setLastName(lastName.substring(0, MAX_LENGTH));
            } else {
                ub.setLastName(lastName);
            }
        } else {
            throw new Exception("Ошибка при получении фамилии пользователя");
        }

        if (infoObject.getAsJsonPrimitive("bdate") != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                java.util.Date tempDate = format.parse(infoObject.getAsJsonPrimitive("bdate").getAsString());
                ub.setBirthDate(new Date(tempDate.getTime()));
            } catch (ParseException e) {
                ub.setBirthDate(null);
            }
        } else {
            ub.setBirthDate(null);
        }

        return ub.build();
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

        public User build() {
            return new User(this);
        }

    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", birthDate=" + birthDate +
                ", city='" + city + '\'' +
                ", sex=" + sex +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

}
