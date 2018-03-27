package com.proto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupWindow;

import com.proto.calendar.CalendarEntry;
import com.proto.calendar.CalendarFragment;
import com.proto.home.HomeFragment;
import com.proto.user.ChangePasswordFragment;
import com.proto.oauth.LoginActivity;
import com.tyczj.extendedcalendarview.Day;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LoaderManager.LoaderCallbacks<List<CalendarEntry>> { //CalendarEventsFragment.OnDaySelectedListener {

    public static boolean SQLDeleted = false;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawer;

    @Bind(R.id.nav_view)
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public android.support.v4.content.Loader<List<CalendarEntry>> onCreateLoader(int id, Bundle args) {
        return new CalendarEntry.CalendarListLoader(MainActivity.this, "https://www.proto.utwente.nl/api/events/upcoming");
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<CalendarEntry>> loader, List<CalendarEntry> data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<CalendarEntry>> loader) {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int menuItemId = item.getItemId();
        Fragment fragment = null;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.nav_tabs);
        tabLayout.setVisibility(View.GONE);

        //initializing the fragment object which is selected
        switch (menuItemId) {
            case R.id.nav_home:
                tabLayout.setVisibility(View.VISIBLE);
                fragment = new HomeFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.nav_history:
                fragment = new PurchaseHistoryFragment();
                break;
            case R.id.nav_calendar:
                tabLayout.setVisibility(View.VISIBLE);
                fragment = new CalendarFragment();
                break;
            case R.id.nav_change_password:
                fragment = new ChangePasswordFragment();
                break;
            case R.id.nav_login:
                //fragment = new LoginFragment();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
//                Intent oauthIntent = new Intent(this, OAuth2Activity.class);
//                startActivity(oauthIntent);
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
