package com.juancoob.nanodegree.and.vegginner.ui;

import android.arch.lifecycle.ViewModelProviders;
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
import android.view.View;
import android.widget.FrameLayout;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.ui.beginning.BeginningFragment;
import com.juancoob.nanodegree.and.vegginner.ui.comingSoon.ComingSoonFragment;
import com.juancoob.nanodegree.and.vegginner.ui.places.PlacesFrament;
import com.juancoob.nanodegree.and.vegginner.ui.recipeDetails.RecipeDetailsFragment;
import com.juancoob.nanodegree.and.vegginner.ui.recipes.RecipesFragment;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.viewmodel.RecipesViewModel;
import com.juancoob.nanodegree.and.vegginner.viewmodel.VegginnerViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class is the main activity which manages fragments using a drawer
 *
 * Created by Juan Antonio Cobos Obrero on 21/07/18.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.t_main)
    public Toolbar mainToolbar;

    @BindView(R.id.dl_main)
    public DrawerLayout mainDrawerLayout;

    @BindView(R.id.fl_main_content)
    public FrameLayout mainContentFrameLayout;

    @BindView(R.id.nv_main)
    public NavigationView mainNavigationView;

    @Inject
    public VegginnerViewModelFactory vegginnerViewModelFactory;

    private RecipesViewModel mRecipesViewModel;
    private boolean mBackToMainWhenPressBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((VegginnerApp) getApplication()).getVegginnerRoomComponent().injectMainActivity(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbar();
        mainNavigationView.setNavigationItemSelectedListener(this);
        setBeginningFragment(savedInstanceState == null);
        mRecipesViewModel = ViewModelProviders.of(this, vegginnerViewModelFactory).get(RecipesViewModel.class);
        mRecipesViewModel.getFragmentDetailToReplace().observe(this, fragmentDetailToReplace -> {
            if (fragmentDetailToReplace != null) {
                mBackToMainWhenPressBack = true;
                if (Constants.RECIPE_DETAILS.equals(fragmentDetailToReplace)) {
                    replaceFragmentToActivity(getSupportFragmentManager(), RecipeDetailsFragment.getInstance(), R.id.fl_main_content, Constants.RECIPE_DETAILS, true);
                }
                hideToolbar();
                lockDrawer();
            }
        });
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

    private void setBeginningFragment(boolean isSavedInstanceStateEmpty) {
        if (isSavedInstanceStateEmpty) {
            replaceFragmentToActivity(getSupportFragmentManager(), BeginningFragment.getInstance(), R.id.fl_main_content, Constants.BEGINNING, false);
        }
    }

    private void replaceFragmentToActivity(FragmentManager fragmentManager, Fragment fragment, int fragmentId, String tag, boolean addToBackStack) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(fragmentId, fragment, tag);
            if (addToBackStack) transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mBackToMainWhenPressBack) {
                    onBackPressed();
                } else {
                    mainDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_beginning:
                replaceFragmentToActivity(getSupportFragmentManager(), BeginningFragment.getInstance(), R.id.fl_main_content, Constants.BEGINNING, false);
                break;
            case R.id.nav_advices:
                replaceFragmentToActivity(getSupportFragmentManager(), ComingSoonFragment.getInstance(), R.id.fl_main_content, Constants.COMING_SOON, false);
                break;
            case R.id.nav_equivalencies:
                replaceFragmentToActivity(getSupportFragmentManager(), ComingSoonFragment.getInstance(), R.id.fl_main_content, Constants.COMING_SOON, false);
                break;
            case R.id.nav_recipes:
                replaceFragmentToActivity(getSupportFragmentManager(), RecipesFragment.getInstance(), R.id.fl_main_content, Constants.RECIPES, false);
                break;
            case R.id.nav_places:
                replaceFragmentToActivity(getSupportFragmentManager(), PlacesFrament.getInstance(), R.id.fl_main_content, Constants.PLACES, false);
                break;
            case R.id.nav_events:
                replaceFragmentToActivity(getSupportFragmentManager(), ComingSoonFragment.getInstance(), R.id.fl_main_content, Constants.COMING_SOON, false);
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
        } else if (mBackToMainWhenPressBack) {
            comeBackToMainFragment();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.exit_dialog_title)
                    .setMessage(R.string.exit_dialog_message)
                    .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        MainActivity.super.onBackPressed();
                    })
                    .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss()).show();
        }
    }

    private void comeBackToMainFragment() {
        removeFragmentFromTheStack();
        initFragmentDetailToReplace();
        showToolbar();
        setToolbar();
        unlockDrawer();
        mBackToMainWhenPressBack = false;
    }

    private void hideToolbar() {
        mainToolbar.setVisibility(View.GONE);
    }

    private void showToolbar() {
        mainToolbar.setVisibility(View.VISIBLE);
    }

    private void lockDrawer() {
        mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void unlockDrawer() {
        mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void removeFragmentFromTheStack() {
        getSupportFragmentManager().popBackStack();
    }

    private void initFragmentDetailToReplace() {
        mRecipesViewModel.getFragmentDetailToReplace().setValue(null);
    }
}
