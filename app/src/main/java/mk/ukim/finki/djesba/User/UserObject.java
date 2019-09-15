package mk.ukim.finki.djesba.User;

import java.io.Serializable;

public class UserObject implements Serializable {
    private String name;
    private String phone;
    private String uid;
    private String notificationKey;

    public UserObject(String uid){
        this.uid = uid;
    }

    public UserObject (String uid, String name, String phone) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getUid() {
        return uid;
    }
    public String getNotificationKey() {
        return notificationKey;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }
}
