package com.example.belinked.organization;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.belinked.R;
import com.example.belinked.adapters.OrganizationHomeAdapter;
import com.example.belinked.adapters.StudentItemAdapter;
import com.example.belinked.models.StudentItem;
import com.example.belinked.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganizationHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizationHomeFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrganizationHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizationHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganizationHomeFragment newInstance(String param1, String param2) {
        OrganizationHomeFragment fragment = new OrganizationHomeFragment();
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

    RecyclerView recyclerViewOrganizationItems;
    DatabaseReference database;
    OrganizationHomeAdapter organizationAdapter;
    ArrayList<StudentItem> organizationItemList;
    ArrayList<User> usersItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_organization_home, container, false);



        //recyclerViewOrganizationItems = v.findViewById(R.id.rv_opportunities_fragment_organization_home);
        recyclerViewOrganizationItems = v.findViewById(R.id.rv_opportunities_fragment_organization_home);

        database = FirebaseDatabase.getInstance().getReference("posts");

        recyclerViewOrganizationItems.setHasFixedSize(true);

        recyclerViewOrganizationItems.setLayoutManager(new GridLayoutManager(this.getContext(), 1));

        organizationItemList = new ArrayList<>();
        usersItemList = new ArrayList<>();
        organizationAdapter = new OrganizationHomeAdapter(this.getContext(), organizationItemList);
        recyclerViewOrganizationItems.setAdapter(organizationAdapter);
        // Firebase --------------------------------------------------------------------------------

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("posts");

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()) {
                    // get data
                    String fName = ""+ds.child("firstName").getValue();
                    String lName = ""+ds.child("lastName").getValue();
                    String email = "" + ds.child("email").getValue();
                    String image = "" + ds.child("pImage").getValue();
                    String title = "" + ds.child("pTitle").getValue();
                    String description = "" + ds.child("pDescription").getValue();
                    organizationItemList.add(
                            new StudentItem(
                                    title,
                                    image,
                                    fName + " " + lName,
                                    email,
                                    description
                            )
                    );
                }

                organizationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
////                    StudentItem studentItem = dataSnapshot.getValue(StudentItem.class);
////                    studentItemList.add(studentItem);
////                    String fName = dataSnapshot.child("firstName").getValue(String.class);
////                    String lName = dataSnapshot.child("lastName").getValue(String.class);
////                    String email = dataSnapshot.child("email").getValue(String.class);
//                    User userItem = dataSnapshot.getValue(User.class);
//                    organizationItemList.add(
//                            new StudentItem(
//                                    "Volunteer Address: Plant Plants",
//                                    "Singed Up",
//                                    userItem.firstName + " " + userItem.lastName,
//                                    userItem.email
//                            )
//                    );
//
//                }
//                organizationAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return v;
    }
}