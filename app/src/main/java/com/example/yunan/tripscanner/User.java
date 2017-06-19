package com.example.yunan.tripscanner;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunan on 2017-05-20.
 */
public class User implements Serializable{
    private Map<String,Object> user = new HashMap<String,Object>();

    public Map<String,Object> getUser(){
        return user;
    }
/*    private String email;
    private String password;
    private String password_confirmation;
    private String name;
    private String authentication_token;
    private String last_sign_in_at;
    private String provider;
    private String uid;
    private String created_at;
    private String updated_at;*/
}
