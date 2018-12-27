package com.tspolice.echallan.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tspolice.echallan.models.SettingsModel;

import java.lang.reflect.Type;

public class SharedPrefsHelper {

    private static Gson GSON = new Gson();
    Type typeOfObject = new TypeToken<Object>() {
    }.getType();
    private Context context;
    private static SharedPreferences sharedPreferences,s_pref_Settings;
    private static SharedPreferences.Editor sharedPreferencesEditor,s_pref_Settings_Editer;

    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public static void clearData(){
        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.apply();
    }

    public static void saveSettingsToS_Pref(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        s_pref_Settings = context.getSharedPreferences(preferenceFileName, 0);
        s_pref_Settings_Editer = s_pref_Settings.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        s_pref_Settings_Editer.putString(serializedObjectKey, serializedObject);
        s_pref_Settings_Editer.apply();
    }

    public static <GenericClass> GenericClass getSettinsFromPref(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        s_pref_Settings = context.getSharedPreferences(preferenceFileName, 0);
        if (s_pref_Settings.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(s_pref_Settings.getString(preferenceKey, ""), classType);
        }
        return null;
    }

    public static void clearSettingsData(String key){
        s_pref_Settings_Editer = s_pref_Settings.edit();
        s_pref_Settings_Editer.remove(key);
        s_pref_Settings_Editer.apply();
    }
}
