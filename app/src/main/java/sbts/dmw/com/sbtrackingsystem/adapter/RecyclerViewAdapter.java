package sbts.dmw.com.sbtrackingsystem.adapter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import sbts.dmw.com.sbtrackingsystem.R;
import sbts.dmw.com.sbtrackingsystem.activities.AttendeeNavigation;
import sbts.dmw.com.sbtrackingsystem.classes.SingletonClass;
import sbts.dmw.com.sbtrackingsystem.fragments.StudentList;
import sbts.dmw.com.sbtrackingsystem.model.Student;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context sContext;
    private List<Student> sData;
    AlertDialog.Builder builder;

    public RecyclerViewAdapter(Context sContext, List<Student> sData, AlertDialog.Builder builder) {

        this.sContext = sContext;
        this.sData = sData;
        this.builder = builder;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(sContext);
        view = inflater.inflate(R.layout.student_row_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.student_name.setText(sData.get(i).getName());
        myViewHolder.student_division.setText(sData.get(i).getDivision());
        myViewHolder.student_roll_no.setText(sData.get(i).getRoll_no());
        myViewHolder.student_class.setText(sData.get(i).getS_class());
        myViewHolder.student_thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(Base64.decode(sData.get(i).getPhoto(), Base64.DEFAULT), 0, Base64.decode(sData.get(i).getPhoto(), Base64.DEFAULT).length));

        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final View view = v;
                String name = ((TextView) v.findViewById(R.id.student_name)).getText().toString();
                final String rollno = ((TextView) v.findViewById(R.id.student_roll_no)).getText().toString();

                builder.setMessage("Is " + name + " present today ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String url = sContext.getString(R.string.Mark_Present_URL);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(sContext, response, Toast.LENGTH_LONG).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("rollno", rollno);
                                params.put("isPresent", "true");
                                return params;
                            }
                        };
                        SingletonClass.getInstance(sContext.getApplicationContext()).addToRequestQueue(stringRequest);


                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                AlertDialog alert = builder.create();
                alert.setTitle("Attendance");

                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView student_name, student_division, student_roll_no, student_class;
        ImageView student_thumbnail;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.container);
            student_name = itemView.findViewById(R.id.student_name);
            student_division = itemView.findViewById(R.id.student_division);
            student_roll_no = itemView.findViewById(R.id.student_roll_no);
            student_class = itemView.findViewById(R.id.student_class);
            student_thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }
}
