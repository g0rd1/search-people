package model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Group {

    final private String Id;

    final private int membersCount;

    private Group(String id, int membersCount) {
        Id = id;
        this.membersCount = membersCount;
    }

    public String getId() {
        return Id;
    }

    public int getMembersCount() {
        return membersCount;
    }

    public static Group parseInfoObject(final JsonObject infoObject){

        final String Id = String.valueOf(infoObject.getAsJsonObject().get("id").getAsInt());

        final JsonElement jsonMembersCount = infoObject.getAsJsonObject().get("members_count");

        final int membersCount;

        if (jsonMembersCount != null) {
            membersCount = jsonMembersCount.getAsInt();
        } else {
            membersCount = 0;
        }

        return new Group(String.valueOf(Id), membersCount);

    }

}
