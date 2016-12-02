package com.yamamz.hroracle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.yamamz.hroracle.prefs.Settings;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private DrawerLayout mDrawerLayout;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        getCredendialsInprefs();

if(username.isEmpty() && password.isEmpty()) {
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
}
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_reorder_white_24dp);
        }
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }



    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new EmployeeListFragment(), "Employees");
        adapter.addFragment(new createEmployee(), "add Employee");
        adapter.addFragment(new EmployeeListFragment(), "Country");
        viewPager.setAdapter(adapter);

    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this,"first",Toast.LENGTH_LONG).show();
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_messages:
                        Toast.makeText(MainActivity.this,"second",Toast.LENGTH_LONG).show();
                        mDrawerLayout.closeDrawers();

                        break;
                    case R.id.nav_friends:
                        Toast.makeText(MainActivity.this,"third",Toast.LENGTH_LONG).show();
                        mDrawerLayout.closeDrawers();
                        break;

                    case R.id.nav_discussion:
                        Toast.makeText(MainActivity.this,"forth",Toast.LENGTH_LONG).show();
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        Adapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_settings:
                Intent modifySettings = new Intent(MainActivity.this,Settings.class);
                startActivity(modifySettings);
                return true;

            case R.id.action_logout:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username","");
                editor.putString("password","");
                editor.apply();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    void getCredendialsInprefs(){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        username = sharedPrefs.getString("username", "");
        password = sharedPrefs.getString("password", "");
    }

}
