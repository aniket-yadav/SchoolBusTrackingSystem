package sbts.dmw.com.sbtrackingsystem.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sbts.dmw.com.sbtrackingsystem.R;
import sbts.dmw.com.sbtrackingsystem.classes.SessionManager;
import sbts.dmw.com.sbtrackingsystem.classes.SingletonClass;

public class LoginActivity extends AppCompatActivity {

    private int LOCATION_PERMISSION_CODE = 1;
    private EditText username, password;
    private ProgressBar loading;
    private Button login;
    private String role, user;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        loading = findViewById(R.id.loading);

        final Intent register = new Intent(this, RegisterUserActivity.class);
        final Intent reset = new Intent(this, PasswordResetActivity.class);

        TextView register_user = findViewById(R.id.userRegistration);
        String register_text = "If your not a registered user click here to sign-up";
        SpannableString rss = new SpannableString(register_text);
        ClickableSpan rcs = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(register);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
            }
        };
        rss.setSpan(rcs, 30, 40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        register_user.setText(rss);
        register_user.setMovementMethod(LinkMovementMethod.getInstance());

        TextView reset_password = findViewById(R.id.resetPassword);
        String reset_text = "Forgot Password?";
        SpannableString pss = new SpannableString(reset_text);
        ClickableSpan pcs = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(reset);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
            }
        };
        pss.setSpan(pcs, 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        reset_password.setText(pss);
        reset_password.setMovementMethod(LinkMovementMethod.getInstance());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            requestLocationPermission();
        }

        loading.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
    }

    public void checkLogin(View view) {

        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            if (username.getText().toString().isEmpty()) {
                username.setError("Please enter your username ");
            }
            if (password.getText().toString().isEmpty()) {
                password.setError("Please enter your password");
            }
        } else {
            loading.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.Login_URL), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("login");

                        if (success.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                role = object.getString("Role").trim();
                                user = object.getString("Username").trim();
                                sessionManager.createSession(user, role);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid username /password", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        login.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("password", password.getText().toString());
                    params.put("username", username.getText().toString());
                    return params;
                }
            };
            SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }

    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("Allow SBTS to access this device's location?")
                    .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
