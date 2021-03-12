package com.alex.yastocks.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class PrefsManager {

    private final SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String LAST_SEARCHES = "lastSearches";


    public PrefsManager(Context context){
        String FILE_NAME = "temp";
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveLastSearchString(String str){
        /*
        * Storage Example:
        * lastSearches: Apple Inc/Yandex/MOMO/
        * */

        editor = sp.edit();
        StringBuilder lastSearches = new StringBuilder(str);
        String[] result = sp.getString(LAST_SEARCHES, "").split("/");
        int n = Math.min(result.length, 11);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(
                result
        ).subList(0, n));

        for(String s: list){
            if (s.isEmpty()) continue;
            lastSearches.append("/").append(s);
        }

        editor.putString(LAST_SEARCHES, lastSearches.toString());
        editor.apply();
    }

    // возврат первых 10 элементов
    public String[] getLastSearches(){
        return sp.getString(LAST_SEARCHES, "").split("/");
    }

}
