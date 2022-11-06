package com.example.belinked.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.belinked.R;
import com.example.belinked.StartActivity;
import com.example.belinked.organization.OrganizationHomeFragment;
import com.example.belinked.organization.OrganizationMainActivity;
import com.example.belinked.organization.OrganizationNotificationsFragment;
import com.example.belinked.organization.OrganizationProfileFragment;
import com.example.belinked.organization.OrganizationSearchFragment;
import com.example.belinked.organization.PostActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentMainActivity extends AppCompatActivity {

    // Variables -----------------------------------------

    // Views
    private Toolbar toolbarStudent;
    private ViewPager viewPagerStudent;
    private TabLayout tabLayoutStudent;
//    private BottomNavigationView bottomNavigationViewStudent;
    private NavigationView navigationViewStudent;
    private DrawerLayout drawerLayoutStudent;
//    private FloatingActionButton floatingActionButtonPostStudent;

    // Local Variables
    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_search_24,
            R.drawable.ic_baseline_notifications_24,
            R.drawable.ic_baseline_person_24
    };

    // Nav header
    private ImageView imageViewStudentHeader;
    private TextView textViewUsernameStudentHeader;
    private TextView textViewEmailStudentHeader;

    // Firebase
    FirebaseAuth mAuth;
    //FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // onCreate Method -------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        connectViews();
        toolbarStudent.setTitle("Home");
        setSupportActionBar(toolbarStudent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawer();

        drawerClicks();

        StudentMainActivity.SectionsPagerAdapter pagerAdapter = new StudentMainActivity.SectionsPagerAdapter(getSupportFragmentManager());
        //Attach the FragmentPagerAdapter to the ViewPager.
        viewPagerStudent.setAdapter(pagerAdapter);

        tabLayoutStudent.setupWithViewPager(viewPagerStudent);

        setupTabIcons();

        viewPagerStudent.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutStudent));
        tabLayoutStudent.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        viewPagerStudent.setCurrentItem(0);
                        toolbarStudent.setTitle(getResources().getText(R.string.home_menu));
                        break;
                    case 1:
                        viewPagerStudent.setCurrentItem(1);
                        toolbarStudent.setTitle(getResources().getText(R.string.search_menu));
                        break;
                    case 2:
                        viewPagerStudent.setCurrentItem(2);
                        toolbarStudent.setTitle(getResources().getText(R.string.notification_menu));
                        break;
                    case 3:
                        viewPagerStudent.setCurrentItem(3);
                        toolbarStudent.setTitle(getResources().getText(R.string.profile_menu));
                    default:
                        viewPagerStudent.setCurrentItem(tab.getPosition());
                        //toolbar.setTitle("Fragment Star");
                        break;
                }



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        floatingActionButtonPostStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(StudentMainActivity.this, PostActivity.class);
//                startActivity(intent);
//            }
//        });

        setStudentInfo();

    }

    // Functions --------------------------------------------

    private void connectViews() {
        toolbarStudent = findViewById(R.id.toolbar_activity_student_main);
        viewPagerStudent = findViewById(R.id.view_pager_activity_student_main);
        tabLayoutStudent = findViewById(R.id.tab_layout_activity_student_main);
//        bottomNavigationViewStudent = findViewById(R.id.btm_nav_activity_student_main);
        drawerLayoutStudent = findViewById(R.id.drawer_layout_activity_student_main);
        navigationViewStudent = findViewById(R.id.nav_view_activity_student_main);
//        floatingActionButtonPostStudent = findViewById(R.id.fab_post_activity_student_main);

        // nav header xml layout
        imageViewStudentHeader = findViewById(R.id.iv_user_nav_header_student);
        textViewUsernameStudentHeader = findViewById(R.id.tv_username_nav_header_student);
        textViewEmailStudentHeader = findViewById(R.id.tv_email_nav_header_student);
    }

    private void setupTabIcons() {
        tabLayoutStudent.getTabAt(0).setIcon(tabIcons[0]);
        tabLayoutStudent.getTabAt(1).setIcon(tabIcons[1]);
        tabLayoutStudent.getTabAt(2).setIcon(tabIcons[2]);
        tabLayoutStudent.getTabAt(3).setIcon(tabIcons[3]);
    }

    // Internal Classes ----------------------------------------------------------------------------

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }
        @Override
        //Say how many pages the ViewPager should contain.
        public int getCount() {
            return 4;
        }
        @Override
        //Specify which fragment should appear on each page.
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new StudentHomeFragment();
                case 1:
                    return new StudentSearchFragment();
                case 2:
                    return new StudentNotificationsFragment();
                case 3:
                    return new StudentProfileFragment();
            }
            return null;
        }
        //This method adds the text to the tabs.
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getText(R.string.home_menu);/*"Home"*/
                case 1:
                    return getResources().getText(R.string.search_menu);
                case 2:
                    return getResources().getText(R.string.notification_menu);
                case 3:
                    return getResources().getText(R.string.profile_menu);
            }
            return null;
        }

    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                StudentMainActivity.this,
                drawerLayoutStudent,
                R.string.open,
                R.string.close
        );
        drawerLayoutStudent.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case(android.R.id.home): {
                drawerLayoutStudent.openDrawer(GravityCompat.START);
                return true;
            }
            default:
                return true;
        }
    }

    private void updateUsernameInHeader(String username, String email, int image) {
        View headerView = navigationViewStudent.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.tv_username_nav_header_student);
        TextView textViewEmail = headerView.findViewById(R.id.tv_email_nav_header_student);
        ImageView imageViewProfile = headerView.findViewById(R.id.iv_user_nav_header_student);
        textViewUsername.setText(username);
        textViewEmail.setText(email);
        if(image != 0) {
            imageViewProfile.setImageResource(image);
        }
        else {
            imageViewProfile.setImageResource(R.drawable.twitter_logo);
        }
    }

    private void drawerClicks() {
        navigationViewStudent.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case(R.id.home): {
                        drawerLayoutStudent.closeDrawer(GravityCompat.START);
                        return true;
                    }
                    case(R.id.logout): {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent = new Intent(StudentMainActivity.this, StartActivity.class);
                        startActivity(intent);
                        return true;
                    }
//                    default:
//                        return true;
                }
                return true;
            }
        });
    }

    private void setStudentInfo() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //user = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");
        Query query = databaseReference.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                String fName = "" + snapshot.child("firstName").getValue();
//                String lName = "" + snapshot.child("lastName").getValue();
//                String email = "" + snapshot.child("email").getValue();
//                int image;

                for(DataSnapshot ds: snapshot.getChildren()) {
                    // get data
                    String fName = ""+ds.child("firstName").getValue();
                    String lName = ""+ds.child("lastName").getValue();
                    String email = "" + ds.child("email").getValue();
                    int image = ds.child("image").getValue(int.class);

                    updateUsernameInHeader(fName + " " + lName, email, image);


//                    textViewUsernameStudentHeader.setText(fName + " " + lName);
//                    textViewEmailStudentHeader.setText(email);
                }
//                if(snapshot.child("image").getValue(int.class) != null) {
//                    image = snapshot.child("image").getValue(int.class);
//                    imageViewStudentHeader.setImageResource(image);
//                }
//                else {
//                    image = 0;
//                    imageViewStudentHeader.setImageResource(R.drawable.twitter_logo);
//                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}