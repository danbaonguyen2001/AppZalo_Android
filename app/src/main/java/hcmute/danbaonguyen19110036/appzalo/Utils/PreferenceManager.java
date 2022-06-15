package hcmute.danbaonguyen19110036.appzalo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private AllConstants allConstants;      // class chứa các hằng được sử dụng trong project
    private SharedPreferences sharedPreferences;    //lưu trữ thông tin dưới dạng key-value

    //Hàm khởi tạo
    public PreferenceManager(Context context){
        sharedPreferences= context.getSharedPreferences(allConstants.KEY_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    // Hàm lưu dữ liệu dạng boolean
    public void putBoolean(String key,Boolean value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    // Hàm lấy dữ liệu dạng boolean
    public Boolean getBoolean(String key){
        return  sharedPreferences.getBoolean(key,false);
    }

    // Hàm lưu dữ liệu dạng String
    public void putString(String key,String value){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    // Hàm lấy dữ liệu dạng String
    public String getString(String key){
        return  sharedPreferences.getString(key,null);
    }
    public void clearPreferences(){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
