package com.example.belinked.organization;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.belinked.R;
import com.example.belinked.adapters.StudentItemAdapter;
import com.example.belinked.models.StudentItem;
import com.example.belinked.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganizationSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizationSearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrganizationSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizationSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganizationSearchFragment newInstance(String param1, String param2) {
        OrganizationSearchFragment fragment = new OrganizationSearchFragment();
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
        View v = inflater.inflate(R.layout.fragment_organization_search, container, false);

        recyclerViewStudentItems = v.findViewById(R.id.rv_users_fragment_organization_search);
        editTextSearch = v.findViewById(R.id.et_search_fragment_organization_search);

        database = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        recyclerViewStudentItems.setHasFixedSize(true);

        recyclerViewStudentItems.setLayoutManager(new GridLayoutManager(this.getContext(), 1));

        studentItemList = new ArrayList<>();
        usersItemList = new ArrayList<>();
        studentItemAdapter = new StudentItemAdapter(this.getContext(), studentItemList);
        recyclerViewStudentItems.setAdapter(studentItemAdapter);

        //SearchView searchView = MenuItemCompat.getActionView(editTextSearch);



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    StudentItem studentItem = dataSnapshot.getValue(StudentItem.class);
//                    studentItemList.add(studentItem);
//                    String fName = dataSnapshot.child("firstName").getValue(String.class);
//                    String lName = dataSnapshot.child("lastName").getValue(String.class);
//                    String email = dataSnapshot.child("email").getValue(String.class);
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
                    recyclerViewStudentItems.setAdapter(new StudentItemAdapter(OrganizationSearchFragment.this.getContext(), studentItemList));
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
//            recyclerViewStudentItems.setAdapter(new StudentItemAdapter(OrganizationSearchFragment.this.getContext(), filteredList));
//            studentItemAdapter.notifyDataSetChanged();
//            for (int i = 0; i < studentItemList.size(); i++) {
//
//                final String s = studentItemList.get(i).toString().toLowerCase();
//                if (text.contains(mQuery)) {
//
//                    filteredList.add(studentItemList.get(i));
//                }
//            }

//            if(studentItem.studentEmail.equals(text)) {
//                filteredList.add(studentItem);
//            }

        }

    }

//    public void addTextListener(){
//
//        editTextSearch.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {}
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            public void onTextChanged(CharSequence query, int start, int before, int count) {
//
//                query = query.toString().toLowerCase();
//                // studentItemList
//                filteredUsers = new ArrayList<>();
//
//                for (int i = 0; i < studentItemList.size(); i++) {
//
//                    final String text = studentItemList.get(i).toString().toLowerCase();
//                    if (text.contains(query)) {
//
//                        filteredUsers.add(studentItemList.get(i));
//                    }
//                }
//                recyclerViewStudentItems.removeAllViews();;
//                studentItemAdapter.notifyDataSetChanged();  // data set changed
//            }
//        });
//    }
}