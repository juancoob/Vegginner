package com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class is a type converter to store a Recipe's list of ingredients
 *
 * Created by Juan Antonio Cobos Obrero on 26/07/18.
 */
public class FavoriteRecipeTypeConverter {

    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if(data == null) return Collections.emptyList();
        return Arrays.asList(data.split(", "));
    }

    @TypeConverter
    public static String stringListToString(List<String> data) {
        if(data == null) return null;
        return TextUtils.join(", ", data);
    }
}
