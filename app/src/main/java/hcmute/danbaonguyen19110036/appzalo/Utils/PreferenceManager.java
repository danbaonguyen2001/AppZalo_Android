package hcmute.danbaonguyen19110036.appzalo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private AllConstants allConstants;
    private SharedPreferences sharedPreferences;
    public PreferenceManager(Context context){
        sharedPreferences= context.getSharedPreferences(allConstants.KEY_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    public void putBoolean(String key,Boolean value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public Boolean getBoolean(String key){
        return  sharedPreferences.getBoolean(key,false);
    }
    public void putString(String key,String value){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public String getString(String key){
        return  sharedPreferences.getString(key,null);
    }
    public void clearPreferences(){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
