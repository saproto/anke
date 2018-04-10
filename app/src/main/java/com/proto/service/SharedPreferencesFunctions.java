package com.proto.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.proto.user.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Dennis on 03/04/2018.
 * @TODO Dennis: Kijken of het mogelijk is om Dagger 2 toe te voegen
 */

public class SharedPreferencesFunctions {

    private static SharedPreferences sharedPreferences;
    //private final String NOTIFICATIONS_ENABLED = "NOTIFICATIONS_ENABLED";

    public SharedPreferencesFunctions(Context context){
        sharedPreferences = context.getSharedPreferences("SharedPreferences",0);
    }

    public static void setName(String name){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.apply();
    }

    public static void updateUser(User user) {
        Class userClass = User.class;
        for (Method method : userClass.getMethods()){
            try {
                method.invoke(user);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    public void setNotifications_enabled(boolean enabled){
    }

    public boolean isNotificationsEnabled(){
        return false;
    }
}
