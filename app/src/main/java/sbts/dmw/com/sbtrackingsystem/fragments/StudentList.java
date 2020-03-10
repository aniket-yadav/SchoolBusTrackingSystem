package sbts.dmw.com.sbtrackingsystem.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sbts.dmw.com.sbtrackingsystem.R;
import sbts.dmw.com.sbtrackingsystem.adapter.RecyclerViewAdapter;
import sbts.dmw.com.sbtrackingsystem.classes.SingletonClass;
import sbts.dmw.com.sbtrackingsystem.model.Student;

public class StudentList extends Fragment {

    private JsonArrayRequest request;
    private List<Student> studentList;
    private RecyclerView recyclerView;
    private ProgressBar loading;
    RecyclerViewAdapter sAdapter;
    AlertDialog.Builder builder;
    Bundle bundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        builder = new AlertDialog.Builder(getActivity());
        bundle = new Bundle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.studentlist_filter_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.all: {
                String url = getString(R.string.Student_List_URL);
                bundle.putString("url", url);
                StudentList studentList = new StudentList();
                studentList.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.nav_frame, studentList).commit();
                return true;
            }
            case R.id.present: {
                String url = getString(R.string.Present_Students_URL);
                bundle.putString("url", url);
                StudentList studentList = new StudentList();
                studentList.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.nav_frame, studentList).commit();
                return true;
            }
            case R.id.absent: {
                String url = getString(R.string.Absent_Students_URL);
                bundle.putString("url", url);
                StudentList studentList = new StudentList();
                studentList.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.nav_frame, studentList).commit();
                return true;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_list, container, false);
        getActivity().setTitle("Student List");
        studentList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recycler_view);
        loading = v.findViewById(R.id.loading_student_list);

        bundle=getArguments();
        String url=bundle.getString("url");

        request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject;

                for (int i = response.length() - 1; i >= 0; i--) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Student s = new Student();
                        s.setName(jsonObject.getString("Full_Name"));
                        s.setDivision(jsonObject.getString("Division"));
                        s.setRoll_no(jsonObject.getString("Roll_No"));
                        s.setS_class(jsonObject.getString("Class"));
                        s.setPhoto(jsonObject.getString("Photo"));
                        studentList.add(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                loading.setVisibility(View.GONE);

                sAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), studentList, builder);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setAdapter(sAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);

        return v;
    }
}
