package sbts.dmw.com.sbtrackingsystem.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import sbts.dmw.com.sbtrackingsystem.R;

public class AttendeeHome extends Fragment {

    ImageView imageView;
    TextView name, email, mobile1, bus_no, dob, address;
    View view;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_attendee_home, container, false);

        Objects.requireNonNull(getActivity()).setTitle("Profile");
        imageView = (ImageView) view.findViewById(R.id.attendee_profile_image);
        name = (TextView) view.findViewById(R.id.attendee_profile_name);
        email = (TextView) view.findViewById(R.id.attendee_profile_email);
        mobile1 = (TextView) view.findViewById(R.id.attendee_profile_mobile1);
        bus_no = (TextView) view.findViewById(R.id.attendee_profile_bus);
        dob = (TextView) view.findViewById(R.id.attendee_profile_dob);
        address = (TextView) view.findViewById(R.id.attendee_profile_address);

        sharedPreferences = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        String Full_Name = sharedPreferences.getString("Full_Name", null);
        String Photo = sharedPreferences.getString("Photo", null);
        String Email = sharedPreferences.getString("Email", null);
        String Mobile_No1 = sharedPreferences.getString("Mobile_No1", null);
        String Bus_No = sharedPreferences.getString("Bus_No", null);
        String DOB = sharedPreferences.getString("DOB", null);
        String Address = sharedPreferences.getString("Address", null);

        name.setText(Full_Name);
        byte[] image_bit = Base64.decode(Photo, Base64.DEFAULT);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(image_bit, 0, image_bit.length));

        email.setText(Email);
        mobile1.setText(Mobile_No1);
        bus_no.setText(Bus_No);
        dob.setText(DOB);
        address.setText(Address);
        return view;

    }
}
