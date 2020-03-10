package sbts.dmw.com.sbtrackingsystem.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

import sbts.dmw.com.sbtrackingsystem.R;
import sbts.dmw.com.sbtrackingsystem.classes.SessionManager;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogIn();

        HashMap<String, String> user = sessionManager.getUserDetails();
        String role = user.get(SessionManager.ROLE);

        if (role != null) {
            if (role.equals("Attendee")) {
                Intent attendee = new Intent(MainActivity.this, AttendeeNavigation.class);
                startActivity(attendee);
                finish();
            } else if(role.equals("Parent")) {
                Intent parent = new Intent(MainActivity.this, ParentNavigation.class);
                startActivity(parent);
                finish();
            }
        }
    }
}
