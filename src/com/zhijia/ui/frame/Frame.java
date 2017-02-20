package com.zhijia.ui.frame;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.*;

/**
 * 每个tab界面的抽象基类，相当于View但是每个View都会拥有Activity的功能。
 */
public abstract class Frame implements TabClickListener {
    private FragmentActivity mActivity;
    private View contentView;

    public void setActivity(FragmentActivity act) {
        this.mActivity = act;
    }

    public void setContentView(View content) {
        this.contentView = content;
    }

    protected Intent getIntent() {
        return getActivity().getIntent();
    }

    public abstract View onCreateView(LayoutInflater inflater);

    protected void setContentView(int id) {
        this.contentView = LayoutInflater.from(mActivity).inflate(id,null);
    }

    public final FragmentActivity getActivity() {
        return mActivity;
    }
    
    public void onCreate() {
    }

    public void onResume() {

    }

    public void onPause() {
    	
    }

    public void onDestroy() {

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    }

    protected FragmentManager getSupportFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    protected Context getContext() {
        return mActivity;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public Resources getResources() {
        return mActivity.getResources();
    }

    public View findViewById(int id) {
        return contentView.findViewById(id);
    }

    public void startActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    public String getString(int resId) {
        return mActivity.getString(resId);
    }

    public void runOnUiThread(Runnable action) {
        mActivity.runOnUiThread(action);
    }

    public Object getSystemService(String name) {
        return mActivity.getSystemService(name);
    }

    public ContentResolver getContentResolver() {
        return mActivity.getContentResolver();
    }

    public void finish() {
        finishHint();
    }
    
    public boolean onBackPressed() {
        return false;
    }

    public void onContentChanged() {
    }

    public void finishHint(){
    }

    protected MenuInflater getMenuInflater() {
        return mActivity.getMenuInflater();
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    public boolean onCreateOptionsMenu(Menu menu){
        return false;
    }

    public boolean onPrepareOptionsMenu(Menu menu){
        return false;
    }

    public void onSaveInstanceState(Bundle outState) {

    }
    public void onWindowFocusChanged(boolean hasFocus){

    }
}