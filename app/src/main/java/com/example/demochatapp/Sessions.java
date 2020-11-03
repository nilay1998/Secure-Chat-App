package com.example.demochatapp;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Sessions {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String PREF_NAME = "pref";

    public Sessions (Context context) {
        pref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession()
    {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    public void killSession()
    {
        editor.putBoolean(IS_LOGIN, false);
        //editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setValue(String key,String value)
    {
        editor.putString(key,value);
        editor.commit();
    }

    public String getValue(String key)
    {
        return pref.getString(key,"-1");
    }

    public void setBooleanValue(String key,boolean value)
    {
        editor.putBoolean(key,value);
        editor.commit();
    }

    public boolean getBooleanValue(String key)
    {
        return pref.getBoolean(key,false);
    }
}