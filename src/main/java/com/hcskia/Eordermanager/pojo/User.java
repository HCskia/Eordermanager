package com.hcskia.Eordermanager.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Table(name = "user")
@Entity
public class User{
    @Id
    @Column(name = "userid")
    private String userId;

    @Column(name = "password")
    @JsonProperty(value="passWord")
    private String passWord;

    public String getUserId(){
        return this.userId;
    }
    public void setUserId(String userid){
        this.userId = userid;
    }

    public String getPw(){
        return this.passWord;
    }
    public void setPw(String passWord){
        this.passWord = passWord;
    }

}
