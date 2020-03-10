package sbts.dmw.com.sbtrackingsystem.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

import sbts.dmw.com.sbtrackingsystem.activities.AttendeeNavigation;
import sbts.dmw.com.sbtrackingsystem.activities.LoginActivity;
import sbts.dmw.com.sbtrackingsystem.activities.MainActivity;
import sbts.dmw.com.sbtrackingsystem.activities.ParentNavigation;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String USERNAME = "USERNAME";
    public static final String ROLE = "ROLE";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();


    }

    public void createSession(String user, String role) {
        editor.putBoolean(LOGIN, true);
        editor.putString(USERNAME, user);
        editor.putString(ROLE, role);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogIn() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(ROLE, sharedPreferences.getString(ROLE, null));

        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        try {
            ((AttendeeNavigation) context).finish();
        } catch (Exception ignored) {
        }
        try{
            ((ParentNavigation) context).finish();
        }catch(Exception ignored){}
    }
}
