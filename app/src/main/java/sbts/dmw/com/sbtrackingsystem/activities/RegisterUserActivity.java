package sbts.dmw.com.sbtrackingsystem.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import sbts.dmw.com.sbtrackingsystem.R;
import sbts.dmw.com.sbtrackingsystem.classes.SingletonClass;


public class RegisterUserActivity extends AppCompatActivity {
    EditText full_name, dob, email, mobile1, mobile2, address, city, pin_code, id, name;

    RadioGroup radioGroup;
    Button photo;
    RadioButton gender;
    String User, getUser;
    Bitmap bitmap;
    TextInputLayout textInputLayout;
    int selectedId;
    final int PICK_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        full_name = findViewById(R.id.register_full_name);
        textInputLayout = findViewById(R.id.register_full_name_layout);
        dob = findViewById(R.id.register_dob);
        email = findViewById(R.id.register_email);
        mobile1 = findViewById(R.id.register_Mobile1);
        mobile2 = findViewById(R.id.register_Mobile2);
        address = findViewById(R.id.register_address);
        city = findViewById(R.id.register_city);
        pin_code = findViewById(R.id.register_pin_code);
        id = findViewById(R.id.register_student_id);
        name = findViewById(R.id.register_student_name);
        radioGroup = findViewById(R.id.radioGrp);
        selectedId = radioGroup.getCheckedRadioButtonId();
        gender = findViewById(selectedId);
        photo = findViewById(R.id.register_photo);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void choosePhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CODE && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                photo.setText(getString(R.string.photo_Set_TEXT));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private String imageToString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void upload() {

        String imageURL = getString(R.string.Upload_Profile_URL);

        StringRequest image_request = new StringRequest(Request.Method.POST, imageURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                params.put("image", imageToString(bitmap));

                return params;
            }
        };

        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(image_request);
    }

    public void uploadData(View view) {

        if (full_name.getText().toString().isEmpty()) {
            textInputLayout.setError("Enter Full Name");
        } else if (dob.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Date of Birth", Toast.LENGTH_LONG).show();
        } else if (email.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_LONG).show();
        } else if (mobile1.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Mobile no", Toast.LENGTH_LONG).show();
        } else if (address.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Address", Toast.LENGTH_LONG).show();
        } else if (city.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_LONG).show();
        } else if (pin_code.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Pin-code", Toast.LENGTH_LONG).show();
        } else if (name.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Student Name", Toast.LENGTH_LONG).show();
        } else if (gender.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_LONG).show();
        } else if (id.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter Student Id", Toast.LENGTH_LONG).show();
        }
    }
}
