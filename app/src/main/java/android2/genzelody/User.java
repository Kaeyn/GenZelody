package android2.genzelody;

public class User {

    String userName;
    String userImg;

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public User(String userName, String userImg) {
        this.userName = userName;
        this.userImg = userImg;
    }


}
