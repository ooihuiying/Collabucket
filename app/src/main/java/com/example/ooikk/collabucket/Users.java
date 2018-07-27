package com.example.ooikk.collabucket;

public class Users {
    private String FullName;
    private String UserType;
    private String Description;
    private String thumb_image;
    private String Image;

    public Users(){

    }

    public Users(String fullName, String userType, String description, String Thumb_image, String Image){
        this.FullName=fullName;
        this.UserType=userType;
        this.Description=description;
        this.thumb_image=Thumb_image;
        this.Image=Image;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName (String fullName) {
        FullName = fullName;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String Thumb_image) {
        this.thumb_image = Thumb_image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
