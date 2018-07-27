package com.juancoob.nanodegree.and.vegginner.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.ui.beginning.BeginningFragment;
import com.juancoob.nanodegree.and.vegginner.ui.recipes.RecipesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.t_main)
    public Toolbar mainToolbar;

    @BindView(R.id.dl_main)
    public DrawerLayout mainDrawerLayout;

    @BindView(R.id.fl_main_content)
    public FrameLayout mainContentFrameLayout;

    @BindView(R.id.nv_main)
    public NavigationView mainNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbar();
        mainNavigationView.setNavigationItemSelectedListener(this);
        setFragment();
    }

    private void setToolbar() {
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
            actionBar.setTitle(R.string.app_name);
        }
    }

    private void setFragment() {
        FragmentManager manager = getSupportFragmentManager();
        if(manager.findFragmentByTag("") == null) {
            replaceFragmentToActivity(getSupportFragmentManager(), BeginningFragment.getInstance(), R.id.fl_main_content);
        }
    }

    private void replaceFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int fragmentId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentId, fragment, "");
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mainDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_beginning:
                replaceFragmentToActivity(getSupportFragmentManager(), BeginningFragment.getInstance(), R.id.fl_main_content);
                break;
            case R.id.nav_advices:
                Toast.makeText(MainActivity.this, "Advices", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_equivalencies:
                Toast.makeText(MainActivity.this, "Equivalencies", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_recipes:
                replaceFragmentToActivity(getSupportFragmentManager(), RecipesFragment.getInstance(), R.id.fl_main_content);
                break;
            case R.id.nav_places:
                Toast.makeText(MainActivity.this, "Places", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_events:
                Toast.makeText(MainActivity.this, "Events", Toast.LENGTH_SHORT).show();
                break;
        }

        item.setChecked(true);
        mainDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainDrawerLayout.closeDrawers();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.exit_dialog_title)
                    .setMessage(R.string.exit_dialog_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
    }
}
