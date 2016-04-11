package com.prepp.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * <p/>
 * Project: <b>Chuglee</b><br/>
 * Created by: Anand K. Rai on 1/6/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class PreferencesUtility {

    public static long getSharedPrefLongData(Context context, String key) {

        SharedPreferences userAccountPreference = context.getSharedPreferences(
                AppConstants.PREF_APP, Context.MODE_PRIVATE);

        return userAccountPreference.getLong(key, 0);

    }

    public static boolean getSharedPrefBooleanData(Context context, String key) {

        SharedPreferences userAcountPreference = context.getSharedPreferences(
                AppConstants.PREF_APP, Context.MODE_PRIVATE);

        return userAcountPreference.getBoolean(key, false);

    }

    public static void setSharedPrefLongData(Context context, String key,
                                             Long value) {
        SharedPreferences appInstallInfoSharedPref = context
                .getSharedPreferences(AppConstants.PREF_APP, Context.MODE_PRIVATE);
        SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
        appInstallInfoEditor.putLong(key, value);
        appInstallInfoEditor.commit();
    }

    public static void setSharedPrefBooleanData(Context context, String key,
                                                boolean value) {
        SharedPreferences appInstallInfoSharedPref = context
                .getSharedPreferences(AppConstants.PREF_APP, Context.MODE_PRIVATE);
        SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
        appInstallInfoEditor.putBoolean(key, value);
        appInstallInfoEditor.commit();
    }

    public static int getSharedPrefIntData(Context context, String key) {

        SharedPreferences userAcountPreference = context.getSharedPreferences(
                AppConstants.PREF_APP, Context.MODE_PRIVATE);

        return userAcountPreference.getInt(key, 0);

    }

    public static void setSharedPrefIntData(Context context, String key,
                                            int value) {
        SharedPreferences appInstallInfoSharedPref = context
                .getSharedPreferences(AppConstants.PREF_APP, Context.MODE_PRIVATE);
        SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
        appInstallInfoEditor.putInt(key, value);
        appInstallInfoEditor.commit();
    }

    public static double getSharedPrefDoubleData(Context context, String key) {

        SharedPreferences userAcountPreference = context.getSharedPreferences(
                AppConstants.PREF_APP, Context.MODE_PRIVATE);
        return Double.longBitsToDouble(userAcountPreference.getLong(key,
                Double.doubleToLongBits(0)));

    }

    public static void setSharedPrefDoubleData(Context context, String key,
                                               double value) {
        SharedPreferences appInstallInfoSharedPref = context
                .getSharedPreferences(AppConstants.PREF_APP, Context.MODE_PRIVATE);
        SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
        appInstallInfoEditor.putLong(key, Double.doubleToRawLongBits(value));
        appInstallInfoEditor.commit();
    }

    public static String getSharedPrefStringData(Context context, String key) {
        if (context == null)
            return "";
        SharedPreferences userAcountPreference = context.getSharedPreferences(
                AppConstants.PREF_APP, Context.MODE_PRIVATE);


        return userAcountPreference.getString(key, "");

    }

    public static void setSharedPrefStringData(Context context, String key,
                                               String value) {
        try {
            SharedPreferences appInstallInfoSharedPref = context
                    .getSharedPreferences(AppConstants.PREF_APP,
                            Context.MODE_PRIVATE);
            SharedPreferences.Editor appInstallInfoEditor = appInstallInfoSharedPref.edit();
            appInstallInfoEditor.putString(key, value);
            appInstallInfoEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
