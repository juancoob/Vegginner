package com.juancoob.nanodegree.and.vegginner.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.IngredientRepository;
import com.juancoob.nanodegree.and.vegginner.util.AppExecutors;
import com.juancoob.nanodegree.and.vegginner.util.Constants;

import javax.inject.Inject;

/**
 * Implementation of App Widget functionality.
 *
 * Created by Juan Antonio Cobos Obrero on 5/08/18.
 */
public class VegginnerShoppingListProvider extends AppWidgetProvider {

    @Inject
    public IngredientRepository ingredientRepository;

    @Inject
    public AppExecutors appExecutors;

    @Override
    public void onReceive(Context context, Intent intent) {
        ((VegginnerApp)context.getApplicationContext()).getVegginnerRoomComponent().injectVegginnerShoppingListProvider(this);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_shopping_list);

        } else if(Constants.BOUGHT_ACTION.equals(intent.getAction())) {

            int ingredientId = intent.getIntExtra(Constants.EXTRA_INGREDIENT_ID, context.getResources().getInteger(R.integer.zero));
            boolean isBought = intent.getBooleanExtra(Constants.EXTRA_IS_BOUGHT, false);
            appExecutors.getDiskIO().execute(() ->
                    ingredientRepository.updateIngredientFromWidgetShoppingList(ingredientId, !isBought));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_shopping_list);

        } else if(Constants.CLEAR_ACTION.equals(intent.getAction())) {
            appExecutors.getDiskIO().execute(() ->
                    ingredientRepository.deleteBoughtIngredientsFromWidgetShoppingList());
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_shopping_list);
        }
        super.onReceive(context, intent);
    }

    public static void updateAppWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, VegginnerShoppingListProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_shopping_list);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            // Setup the intent which points to the service and provides the view for the collection
            Intent intent = new Intent(context, VegginnerShoppingListService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.vegginner_shopping_list);
            views.setRemoteAdapter(R.id.lv_shopping_list, intent);
            views.setEmptyView(R.id.lv_shopping_list, R.id.tv_empty_list);

            // Set pending intent template for the collection
            Intent boughtIntent = new Intent(context, VegginnerShoppingListProvider.class);
            boughtIntent.setAction(Constants.BOUGHT_ACTION);
            boughtIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            boughtIntent.setData(Uri.parse(boughtIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent boughtPendingItem = PendingIntent.getBroadcast(context,
                    context.getResources().getInteger(R.integer.widget_list_request_code), boughtIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.lv_shopping_list, boughtPendingItem);

            // Set click pending intent for the button
            Intent clearIntent = new Intent(context, VegginnerShoppingListProvider.class);
            clearIntent.setAction(Constants.CLEAR_ACTION);
            clearIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            clearIntent.setData(Uri.parse(clearIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent clearPendingIntent = PendingIntent.getBroadcast(context,
                    context.getResources().getInteger(R.integer.widget_button_request_code), clearIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btn_clear_purchased, clearPendingIntent);


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

