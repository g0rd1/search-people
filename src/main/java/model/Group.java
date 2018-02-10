package model;

public class Group {

    final private String Id;

    final private int membersCount;

    public Group(String id, int membersCount) {
        Id = id;
        this.membersCount = membersCount;
    }

    public String getId() {
        return Id;
    }

    public int getMembersCount() {
        return membersCount;
    }

}
