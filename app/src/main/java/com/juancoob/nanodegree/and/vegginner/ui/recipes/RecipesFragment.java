package com.juancoob.nanodegree.and.vegginner.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.viewmodel.RecipesViewModel;
import com.juancoob.nanodegree.and.vegginner.viewmodel.VegginnerViewModelFactory;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Juan Antonio Cobos Obrero on 26/07/18.
 */
public class RecipesFragment extends Fragment implements IRetryLoadingCallback {

    @BindView(R.id.rv_recipes)
    public RecyclerView recipesRecyclerView;

    @BindView(R.id.pb_loading)
    public ProgressBar loadingProgressBar;

    @BindView(R.id.tv_no_recipes)
    public TextView noRecipesTextView;

    @BindView(R.id.btn_retry)
    public Button retryButton;

    @Inject
    public VegginnerViewModelFactory vegginnerViewModelFactory;

    private Context mCtx;
    private GridLayoutManager mGridLayoutManager;
    private RecipesListAdapter mRecipesListAdapter;
    private RecipesViewModel mRecipesViewModel;

    public RecipesFragment() {
        // Required empty public constructor
    }

    public static RecipesFragment getInstance() {
        return new RecipesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((VegginnerApp) Objects.requireNonNull(getActivity()).getApplication()).getRecipeComponent().injectRecipesSection(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);

        mRecipesListAdapter = new RecipesListAdapter(this, mCtx);
        mRecipesViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), vegginnerViewModelFactory).get(RecipesViewModel.class);
        mRecipesViewModel.getSecondRecipeResponseLiveData().observe(this, secondRecipeResponseList -> mRecipesListAdapter.submitList(secondRecipeResponseList));
        mRecipesViewModel.getNetworkState().observe(this, networkState -> mRecipesListAdapter.setNetworkState(networkState));
        mRecipesViewModel.getInitialLoadingLiveData().observe(this, networkState -> {
            mRecipesListAdapter.checkInitialLoading(networkState);
        });
        initRecyclerview();
        return view;
    }

    private void initRecyclerview() {
        mGridLayoutManager = new GridLayoutManager(mCtx, getNumberColumns());
        recipesRecyclerView.setLayoutManager(mGridLayoutManager);
        recipesRecyclerView.setAdapter(mRecipesListAdapter);
    }

    private int getNumberColumns() {
        if (getActivity() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int columns = width / getResources().getInteger(R.integer.width_divider);
            if (columns >= 2) return columns;
        }
        return 2;
    }

    @Override
    public void loadListAgain() {
        mRecipesViewModel.getAgainSecondRecipeResponseLiveData().observe(this, secondRecipeResponses -> {
            if(secondRecipeResponses != null && secondRecipeResponses.size() > 0) {
                mRecipesListAdapter.submitList(secondRecipeResponses);
            }
        });
    }

    @Override
    public void showProgressBar() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNoElements() {
        noRecipesTextView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_retry)
    public void OnRetryPressed() {
        hideNoElements();
        showProgressBar();
        loadListAgain();
    }

    public void hideNoElements() {
        noRecipesTextView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
    }

    @Override
    public void showRecipeDetails(Recipe selectedRecipe) {
        mRecipesViewModel.getSelectedRecipe().setValue(selectedRecipe);
        mRecipesViewModel.getFragmentDetailToReplace().setValue(Constants.RECIPE_DETAILS);
    }
}
