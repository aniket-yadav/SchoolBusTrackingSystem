package sbts.dmw.com.sbtrackingsystem.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import sbts.dmw.com.sbtrackingsystem.R;
import sbts.dmw.com.sbtrackingsystem.classes.SessionManager;
import sbts.dmw.com.sbtrackingsystem.classes.SingletonClass;
import sbts.dmw.com.sbtrackingsystem.fragments.ParentHome;
import sbts.dmw.com.sbtrackingsystem.fragments.changepassword;
import sbts.dmw.com.sbtrackingsystem.fragments.map;

public class ParentNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    SessionManager sessionManager;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView imageView;
    View header_view;
    String[] str;
    StringRequest stringRequest;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Bitmap bitmap;
    String url, User;
    TextView name, email;
    final int PICK_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_navigation);
        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        navigationView = findViewById(R.id.parentNavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        header_view = navigationView.getHeaderView(0);
        imageView = header_view.findViewById(R.id.menu_photo);
        name = header_view.findViewById(R.id.menu_name);
        email = header_view.findViewById(R.id.menu_email);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_CODE);

            }
        });

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        User = user.get(SessionManager.USERNAME);

        toolbar = findViewById(R.id.parent_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.parentDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.parent_nav_frame, new map()).commit();
            navigationView.setCheckedItem(R.id.nav_bus_location);
            toolbar.setTitle("Map");
        }
        getData();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_logout_parent: {
                sessionManager.logout();
                break;
            }
            case R.id.nav_bus_location: {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.parent_nav_frame, new map())
                        .addToBackStack(null)
                        .commit();
                toolbar.setTitle("Map");
                break;
            }
            case R.id.nav_parent_profile: {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.parent_nav_frame, new ParentHome())
                        .addToBackStack(null)
                        .commit();
                toolbar.setTitle("Profile");
                break;
            }
            case R.id.nav_change_pass: {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.parent_nav_frame, new changepassword())
                        .addToBackStack(null)
                        .commit();
                toolbar.setTitle("Change Password");
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void getData() {

        url = getString(R.string.Parent_URL);
        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        editor = sharedPreferences.edit();

                        str = Pattern.compile(",").split(response);
                        editor.putString("Full_Name", str[0]);
                        editor.putString("Photo", str[1]);
                        editor.putString("Email", str[2]);
                        editor.putString("Mobile_No1", str[3]);
                        editor.putString("Bus_No", str[4]);
                        editor.putString("DOB", str[5]);
                        editor.putString("Student_Name", str[6]);
                        editor.putString("Address", str[7]);
                        editor.putString("Latitude",str[8]);
                        editor.putString("Longitude",str[9]);
                        editor.apply();

                        String Photo = sharedPreferences.getString("Photo", null);
                        byte[] imagebit = Base64.decode(Photo, Base64.DEFAULT);
                        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imagebit, 0, imagebit.length));

                        name.setText(sharedPreferences.getString("Full_Name", null));
                        email.setText(sharedPreferences.getString("Email", null));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Objects.requireNonNull(sharedPreferences.getString("USERNAME", "NULL")));
                return params;
            }
        };
        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CODE && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                upload();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String imagetoString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void upload() {

        String imageURL = getString(R.string.Upload_Profile_Parent_URL);

        StringRequest image_request = new StringRequest(Request.Method.POST, imageURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", User);
                params.put("image", imagetoString(bitmap));

                return params;
            }
        };

        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(image_request);
    }

}
