package com.bsr.bsrcoin;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefmanager {

    private static SharedPrefmanager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "useremail";
    private static final String KEY_ID = "userid";
    private static final String KEY_AGENT = "agent";
    private static final String KEY_ADMIN = "admin";
    private static final String KEY_ACCOUNT = "account";
    private static String KEY_ImgUri = "imageuri";
    private static final String KEY_last_login = "lastLogin";
//    private static final String Key_Img_Display_Name = "imagename";

//    private static final ArrayList<ArrayList<String>> display_names = new ArrayList<>(3);


    private static final String KEY_TOKEN = "token";

    private SharedPrefmanager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefmanager getInstance(Context context) {
        if(mInstance == null)
            mInstance = new SharedPrefmanager(context);
//        display_names.add(new ArrayList<>());
//        display_names.add(new ArrayList<>());
//        display_names.add(new ArrayList<>());

        return mInstance;
    }

    public boolean userLogin(int id, String name, String email, String agent, String account, String admin)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID,id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, name);
        editor.putString(KEY_AGENT, agent);
        editor.putString(KEY_ACCOUNT, account);
        editor.putString(KEY_ADMIN, admin);
        editor.apply();
        return true;
    }
    public boolean userDP(String imgUri,String userId){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ImgUri+userId, imgUri);
        editor.apply();

        return true;
    }
    public String dpUri(String userId){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ImgUri+userId,"");
    }

    public void setToken(String token) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN+getKeyId(),token);
        editor.apply();
    }

    public String getKeyToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN+getKeyId(), null);
    }

    public boolean isAgent() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AGENT, null).equalsIgnoreCase("yes");
    }

    public boolean isAdmin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ADMIN, null).equalsIgnoreCase("yes");
    }

    public String getKeyId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return String.valueOf(sharedPreferences.getInt(KEY_ID, 0));
    }

    public String getKeyAccount() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCOUNT, null);
    }

    public String getKeyUsernameName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }
    public String getKeyEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL,null);
    }

    public boolean isLoggedIN() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUserID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID,null);

    }

    public void setLl(String userId,String ll) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastLogin"+userId,ll);
        editor.apply();
    }
    public String getLl(String userId){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("lastLogin"+userId,"");
    }
//    public ArrayList<ArrayList<String>> setImgNames(int callFlag, String name){
//
//
//    }
}

