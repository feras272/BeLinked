package com.example.belinked.organization;

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
import com.example.belinked.models.StudentItem;
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

import org.w3c.dom.Text;


public class OrganizationMainActivity extends AppCompatActivity {

    private Toolbar toolbarOrganization;
    private ViewPager viewPagerOrganization;
    private TabLayout tabLayoutOrganization;
    private BottomNavigationView bottomNavigationViewOrganization;
    private NavigationView navigationViewOrganization;
    private DrawerLayout drawerLayoutOrganization;
    private FloatingActionButton floatingActionButtonPost;

    // Nav header
    private ImageView imageViewOrganizationHeader;
    private TextView textViewUsernameOrganizationHeader;
    private TextView textViewEmailOrganizationHeader;

    // Firebase
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_search_24,
            R.drawable.ic_baseline_notifications_24,
            R.drawable.ic_baseline_person_24
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_main);

        connectViews();
        toolbarOrganization.setTitle("Home");
        setSupportActionBar(toolbarOrganization);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupDrawer();

        drawerClicks();

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //Attach the FragmentPagerAdapter to the ViewPager.
        viewPagerOrganization.setAdapter(pagerAdapter);

        tabLayoutOrganization.setupWithViewPager(viewPagerOrganization);

        setupTabIcons();

        viewPagerOrganization.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutOrganization));
        tabLayoutOrganization.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        viewPagerOrganization.setCurrentItem(0);
                        toolbarOrganization.setTitle(getResources().getText(R.string.home_menu));
                        break;
                    case 1:
                        viewPagerOrganization.setCurrentItem(1);
                        toolbarOrganization.setTitle(getResources().getText(R.string.search_menu));
                        break;
                    case 2:
                        viewPagerOrganization.setCurrentItem(2);
                        toolbarOrganization.setTitle(getResources().getText(R.string.notification_menu));
                        break;
                    case 3:
                        viewPagerOrganization.setCurrentItem(3);
                        toolbarOrganization.setTitle(getResources().getText(R.string.profile_menu));
                    default:
                        viewPagerOrganization.setCurrentItem(tab.getPosition());
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

        floatingActionButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizationMainActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        setOrganizationInfo();


    }

    private void connectViews() {

        // OrganizationMainActivity
        toolbarOrganization = findViewById(R.id.toolbar_activity_organization_main);
        viewPagerOrganization = findViewById(R.id.view_pager_activity_organization_main);
        tabLayoutOrganization = findViewById(R.id.tab_layout_activity_organization_main);
//        bottomNavigationViewOrganization = findViewById(R.id.btm_nav_activity_organization_main);
        drawerLayoutOrganization = findViewById(R.id.drawer_layout_activity_organization_main);
        navigationViewOrganization = findViewById(R.id.nav_view_activity_organization_main);
        floatingActionButtonPost = findViewById(R.id.fab_post_activity_organization_main);

        // nav header xml layout
        imageViewOrganizationHeader = findViewById(R.id.iv_user_nav_header);
        textViewUsernameOrganizationHeader = findViewById(R.id.tv_username_nav_header);
        textViewEmailOrganizationHeader = findViewById(R.id.tv_email_nav_header);

    }

    private void setupTabIcons() {
        tabLayoutOrganization.getTabAt(0).setIcon(tabIcons[0]);
        tabLayoutOrganization.getTabAt(1).setIcon(tabIcons[1]);
        tabLayoutOrganization.getTabAt(2).setIcon(tabIcons[2]);
        tabLayoutOrganization.getTabAt(3).setIcon(tabIcons[3]);
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
                    return new OrganizationHomeFragment();
                case 1:
                    return new OrganizationSearchFragment();
                case 2:
                    return new OrganizationNotificationsFragment();
                case 3:
                    return new OrganizationProfileFragment();
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
                OrganizationMainActivity.this,
                drawerLayoutOrganization,
                R.string.open,
                R.string.close
        );
        drawerLayoutOrganization.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case(android.R.id.home): {
                drawerLayoutOrganization.openDrawer(GravityCompat.START);
                return true;
            }
            default:
                return true;
        }
    }

    private void updateUsernameInHeader(String username, String email, int image) {
        View headerView = navigationViewOrganization.getHeaderView(0);
        //TextView textViewUsername = headerView.findViewById(R.id.tv_email_nav_header);
//                textViewUsername.setText(username);
        TextView textViewUsername = headerView.findViewById(R.id.tv_username_nav_header);
        TextView textViewEmail = headerView.findViewById(R.id.tv_email_nav_header);
        ImageView imageViewProfile = headerView.findViewById(R.id.iv_user_nav_header);
        textViewUsername.setText(username);
        textViewEmail.setText(email);
        if(image != 0) {
            imageViewProfile.setImageResource(image);
        }
        else {
            imageViewProfile.setImageResource(R.drawable.twitter_logo);
        }
    }

    //setNavigationItemSelectedListener
    private void drawerClicks() {
        navigationViewOrganization.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case(R.id.home): {
                        drawerLayoutOrganization.closeDrawer(GravityCompat.START);
                        return true;
                    }
                    case(R.id.logout): {
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        Intent intent = new Intent(OrganizationMainActivity.this, StartActivity.class);
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

    private void setOrganizationInfo() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference("users");
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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

//                String fName = "" + snapshot.child("firstName").getValue();
//                String lName = "" + snapshot.child("lastName").getValue();
//                String email = "" + snapshot.child("email").getValue();
//
//                int image = snapshot.child("image").getValue(int.class);
//
//                updateUsernameInHeader(fName + " " + lName, email, image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}