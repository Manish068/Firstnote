package com.manish.firstnote.Data;

import java.io.Serializable;

//storing information of user we need to make class like this

public class Userinfo implements Serializable {

    String username="";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    String useremail="";
    String mobileno="";

    public Userinfo(){

    }

    public Userinfo(String name,String email,String mobile){
        username=name;
        useremail=email;
        mobileno=mobile;
    }
}
