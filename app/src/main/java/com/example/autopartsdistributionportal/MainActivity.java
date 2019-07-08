package com.example.autopartsdistributionportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPre", Context.MODE_PRIVATE);
        Boolean saveLogin = sharedPreferences.getBoolean("saveLogin", false);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayselectedscreen(R.id.dashboard);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.blackie));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        displayselectedscreen(id);
        return true;
    }

    public void displayselectedscreen(int id) {
        Fragment fragment = null;
        FragmentTransaction ft;
        if (id == R.id.dashboard) {
            fragment = new Fragment_DashBoard();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
//            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.edit_accounts) {
            fragment = new Fragment_EditAccounts();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.add_address) {
            /*fragment = new Fragment_AddAddress();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();*/
        } else if (id == R.id.employee) {
            fragment = new Fragment_Employee();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } /*else if (id == R.id.employee_designation) {
            fragment = new Fragment_Employee_Designation();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }*/ else if (id == R.id.change_password) {
            fragment = new Fragment_ChangePassword();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.vehilce) {
            fragment = new Fragment_Vehicle();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.product_holder) {
            fragment = new Fragment_Product_Holder();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.my_product) {
            fragment = new Fragment_My_Product();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.assign_order) {
            fragment = new Fragment_MyOrders();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.check_product_agent) {
            fragment = new Fragment_CheckProductAgent();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.my_transaction) {
            fragment = new Fragment_Transactions();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.logout) {
            editor = sharedPreferences.edit();
            editor.putBoolean("saveLogin", false);
            editor.apply();
            Intent i = new Intent(getApplicationContext(), Activity_Login.class);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
