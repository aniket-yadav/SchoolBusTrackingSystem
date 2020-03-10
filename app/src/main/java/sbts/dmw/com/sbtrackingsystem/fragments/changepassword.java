package sbts.dmw.com.sbtrackingsystem.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import sbts.dmw.com.sbtrackingsystem.R;
import sbts.dmw.com.sbtrackingsystem.activities.MainActivity;
import sbts.dmw.com.sbtrackingsystem.classes.SessionManager;
import sbts.dmw.com.sbtrackingsystem.classes.SingletonClass;


public class changepassword extends Fragment {

    View view;
    String User,Role;
    SessionManager sessionManager;
    TextView old_password, new_password;
    Button submit;
    public changepassword() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_changepassword, container, false);
        getActivity().setTitle("Change Password");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        old_password = view.findViewById(R.id.old_password);
        new_password = view.findViewById(R.id.new_password);
        submit = view.findViewById(R.id.password_change);
        sessionManager = new SessionManager(getActivity().getApplicationContext());
        final HashMap<String, String> user = sessionManager.getUserDetails();
        User = user.get(SessionManager.USERNAME);
        Role=user.get(SessionManager.ROLE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (old_password.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter your old password ", Toast.LENGTH_LONG).show();

                } else if (new_password.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter your new password ", Toast.LENGTH_LONG).show();

                } else if (old_password.getText().toString().equalsIgnoreCase(new_password.getText().toString())) {

                    Toast.makeText(getActivity().getApplicationContext(), "old  password and new password should not be the same ", Toast.LENGTH_LONG).show();

                } else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.Change_Password_URL), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("username", User);
                            params.put("role",Role);
                            params.put("old_password", old_password.getText().toString());
                            params.put("new_password", new_password.getText().toString());
                            return params;
                        }
                    };
                    SingletonClass.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);

                }
            }

        });
    }
}