package com.juancoob.nanodegree.and.vegginner.ui.recipeDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This class is the recyclerview's adapter to list ingredients on RecipeIngredientsServingsFragment
 * <p>
 * Created by Juan Antonio Cobos Obrero on 30/07/18.
 */
public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.IngredientsViewHolder> {

    private ISelectedIngredientToListCallback mISelectedIngredientToListCallback;
    private Context mCtx;
    private List<String> mIngredientList;
    private List<String> mIngredientsFromShoppingList;

    public RecipeIngredientsAdapter(ISelectedIngredientToListCallback selectedIngredientToListCallback,
                                    Context context, List<String> ingredientList, List<String> ingredientsFromShoppingList) {
        mISelectedIngredientToListCallback = selectedIngredientToListCallback;
        mCtx = context;
        mIngredientList = ingredientList;
        mIngredientsFromShoppingList = ingredientsFromShoppingList;
    }

    public void updateIngredientsFromShoppingList(List<String> ingredientsFromShoppingList) {
        mIngredientsFromShoppingList = ingredientsFromShoppingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_recipe_ingredient, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        String ingredientName = mIngredientList.get(position);
        holder.ingredientTextView.setText(ingredientName);
        if (mIngredientsFromShoppingList.contains(ingredientName)) {
            holder.shoppingListItemCheckBox.setChecked(true);
        } else {
            holder.shoppingListItemCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnClickListener {

        @BindView(R.id.tv_ingredient)
        public TextView ingredientTextView;

        @BindView(R.id.cb_shopping_list_item)
        public CheckBox shoppingListItemCheckBox;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            shoppingListItemCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(shoppingListItemCheckBox.isChecked()) {
                mISelectedIngredientToListCallback.addIngredientToTheShoppingList(mIngredientList.get(getAdapterPosition()));
            } else {
                mISelectedIngredientToListCallback.removeIngredientFromTheShoppingList(mIngredientList.get(getAdapterPosition()));
            }
        }
    }
}
