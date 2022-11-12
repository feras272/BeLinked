package com.example.belinked.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.belinked.R;
import com.example.belinked.adapters.StudentItemAdapter;
import com.example.belinked.models.StudentItem;
import com.example.belinked.models.User;
import com.example.belinked.organization.OrganizationSearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentSearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentSearchFragment newInstance(String param1, String param2) {
        StudentSearchFragment fragment = new StudentSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // ---------------------------------------------------------------------------------------------

    // Views
    RecyclerView recyclerViewStudentItems;
    EditText editTextSearch;

    DatabaseReference database;
    StudentItemAdapter studentItemAdapter;
    ArrayList<StudentItem> studentItemList;
    ArrayList<User> usersItemList;
    FirebaseAuth mAuth;

    ArrayList<StudentItem> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_search, container, false);

        recyclerViewStudentItems = v.findViewById(R.id.rv_users_fragment_student_search);
        editTextSearch = v.findViewById(R.id.et_search_fragment_student_search);

        database = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        recyclerViewStudentItems.setHasFixedSize(true);

        recyclerViewStudentItems.setLayoutManager(new GridLayoutManager(this.getContext(), 1));

        studentItemList = new ArrayList<>();
        usersItemList = new ArrayList<>();
        studentItemAdapter = new StudentItemAdapter(this.getContext(), studentItemList);
        recyclerViewStudentItems.setAdapter(studentItemAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    User userItem = dataSnapshot.getValue(User.class);
                    studentItemList.add(
                            new StudentItem(
                                    "Volunteer Address: Plant Plants",
                                    "Singed Up",
                                    userItem.firstName + " " + userItem.lastName,
                                    userItem.email
                            )
                    );

                }
                studentItemAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        filteredList = new ArrayList<>();

        //addTextListener();
        editTextSearch.addTextChangedListener(new TextWatcher() {

            //ArrayList<StudentItem> filtered = new ArrayList<>();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filteredList.clear();

                if(s.toString().isEmpty()) {
                    recyclerViewStudentItems.setAdapter(new StudentItemAdapter(StudentSearchFragment.this.getContext(), studentItemList));
                    studentItemAdapter.notifyDataSetChanged();
                }
                else {
                    filter(s.toString(), s);
                }

            }
        });


        return v;
    }

    private void filter(String text, Editable query) {
        for (StudentItem studentItem:studentItemList) {

            String mQuery = query.toString().toLowerCase();

            if(studentItem.getStudentEmail().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(studentItem);
            }
            studentItemAdapter.filterList(filteredList);

        }

    }
}