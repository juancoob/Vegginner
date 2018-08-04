package com.juancoob.nanodegree.and.vegginner.ui.recipeDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.util.CheckInternetConnection;
import com.juancoob.nanodegree.and.vegginner.util.CheckWebAsyncTask;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.util.callbacks.IAlertDialogCallback;
import com.juancoob.nanodegree.and.vegginner.util.callbacks.ICheckWebCallback;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This fragment shows the recipe's author and web page
 *
 * Created by Juan Antonio Cobos Obrero on 30/07/18.
 */
public class RecipeStepsFragment extends Fragment implements IAlertDialogCallback, ICheckWebCallback {

    @BindView(R.id.tv_steps_author)
    public TextView stepsAuthorTextView;

    @BindView(R.id.wv_steps_web)
    public WebView stepsWebView;

    @BindView(R.id.tv_no_recipes)
    public TextView noRecipesTextView;

    private Recipe mRecipe;

    public RecipeStepsFragment() {
        // Empty constructor
    }

    public static RecipeStepsFragment getInstance(Recipe recipe) {
        RecipeStepsFragment fragment = new RecipeStepsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE, recipe);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mRecipe = bundle.getParcelable(Constants.RECIPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, view);
        if(mRecipe != null) {
            stepsAuthorTextView.setText(mRecipe.getRecipeAuthor());
            showStepsWeb();
        }
        return view;
    }

    private void showStepsWeb() {
        if(CheckInternetConnection.isConnected(Objects.requireNonNull(getContext()))) {
            // Shows the web page and checks if it has the recipe
            WebSettings webSettings = stepsWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            stepsWebView.setWebViewClient(new WebViewClient());
            stepsWebView.loadUrl(mRecipe.getRecipeWeb());
            new CheckWebAsyncTask(this).execute(mRecipe);
        } else {
            CheckInternetConnection.showDialog(this, getContext(), R.string.no_internet_title, R.string.no_internet_message, R.string.retry, R.string.no);
        }
    }

    // Alert dialog results
    @Override
    public void showPositiveResult() {
        showStepsWeb();
    }

    @Override
    public void showNegativeResult() {
        // Do nothing
    }

    // Web checker result
    @Override
    public void containsSteps(boolean ok) {
        // If the web page drops the recipe
        if(!ok) {
            stepsWebView.setVisibility(View.GONE);
            noRecipesTextView.setVisibility(View.VISIBLE);
        }
    }
}
