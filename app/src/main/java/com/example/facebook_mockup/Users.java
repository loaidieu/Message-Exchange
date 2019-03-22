package com.example.facebook_mockup;

public class Users {
    private String userName;
    private String password;
    private String firstName;
    private String avatar;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatar() {
        return avatar;
    }

    public Users(String userName, String password, String firstName, String avatar, boolean active) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.avatar = avatar;
        this.active = active;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj != null && obj instanceof Users) {
            isEqual = (this.userName.equals(((Users) obj).userName));
        }
        return isEqual;
    }
}
