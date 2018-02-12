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

    public static Group parseInfoObject(final JsonObject infoObject) {

        final String Id;

        final int membersCount;

        final JsonElement jsonMembersCount = infoObject.getAsJsonObject().get("members_count");

        final JsonElement jsonId = infoObject.getAsJsonObject().get("id");

        if (jsonMembersCount != null) {
            membersCount = jsonMembersCount.getAsInt();
        } else {
            return null;
        }

        if (jsonId != null) {
            Id = String.valueOf(jsonId.getAsInt());
        } else {
            return null;
        }

        return new Group(Id, membersCount);

    }

}
