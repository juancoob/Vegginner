package com.juancoob.nanodegree.and.vegginner.data.recipes.local;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Juan Antonio Cobos Obrero on 26/07/18.
 */
public class FavoriteRecipeTypeConverter {

    @TypeConverter
    public static List<String> stringToStringList(String data) {
        if(data == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(data.split(", "));
    }

    @TypeConverter
    public static String stringListToString(List<String> data) {
        if(data == null) return null;
        else return data.toString();
    }
}
