package com.juancoob.nanodegree.and.vegginner.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.Ingredient;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.IngredientRepository;
import com.juancoob.nanodegree.and.vegginner.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * This class is a service which helps to bind data on the view
 *
 * Created by Juan Antonio Cobos Obrero on 5/08/18.
 */
public class VegginnerShoppingListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new VegginnerRemoteViewFactory(this.getApplicationContext(), intent);
    }

    public class VegginnerRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

        @Inject
        public IngredientRepository ingredientRepository;

        private Context mCtx;
        private int mAppWidgetId;
        private List<Ingredient> mShoppingListIngredients = new ArrayList<>();

        public VegginnerRemoteViewFactory(Context applicationContext, Intent intent) {
            mCtx = applicationContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            ((VegginnerApp) mCtx).getRecipeComponent().injectVegginnerRemoteViewFactory(this);
        }

        @Override
        public void onDataSetChanged() {
            mShoppingListIngredients = ingredientRepository.getIngredientListForWidgetFromShoppingList();
        }

        @Override
        public void onDestroy() {
            mShoppingListIngredients.clear();
        }

        @Override
        public int getCount() {
            return mShoppingListIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            // Construct remote view items based on the widget item xml file
            RemoteViews remoteViews = new RemoteViews(mCtx.getPackageName(), R.layout.item_vegginner_shopping_list);
            remoteViews.setTextViewText(R.id.tv_ingredient, mShoppingListIngredients.get(position).getIngredientName());

            if(mShoppingListIngredients.get(position).isBought()) {
                remoteViews.setViewVisibility(R.id.iv_check, View.VISIBLE);
            } else {
                remoteViews.setViewVisibility(R.id.iv_check, View.GONE);
            }

            // Set the fill-in intent to fill in the pending intent template which is set on the collection view in VegginnerShoppingListProvider
            Bundle extras = new Bundle();
            extras.putInt(Constants.EXTRA_INGREDIENT_ID, mShoppingListIngredients.get(position).getIngredientId());
            extras.putBoolean(Constants.EXTRA_IS_BOUGHT, mShoppingListIngredients.get(position).isBought());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            remoteViews.setOnClickFillInIntent(R.id.ll_shopping_list_item, fillInIntent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}