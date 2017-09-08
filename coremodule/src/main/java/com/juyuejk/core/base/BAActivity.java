package com.juyuejk.core.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by JulyYu on 2017/4/6.
 */

public class BAActivity extends AppCompatActivity{


    protected Activity thisContext;

    protected FragmentManager fragmentManager;
    protected Fragment showFragment;

    public static void killAll() {
        List<Activity> clone;
        synchronized (BFActivity.getmActivities()) {
            clone = new LinkedList<>(BFActivity.getmActivities());
        }
        for (Activity baseActivity : clone) {
            baseActivity.finish();
            BFActivity.getmActivities().remove(baseActivity);
        }
    }

    /**
     * 替换Fragment
     * @param fragment
     */
    protected void replaceShow(Fragment fragment){
        try {
            if (showFragment == null) {
                fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
                showFragment = fragment;
            } else {
                fragmentManager.beginTransaction().hide(showFragment).show(fragment).commitAllowingStateLoss();
                showFragment = fragment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换Fragment
     * @param layoutId
     * @param fragment
     */
    protected void replaceShow(Fragment fragment, int layoutId){
        try {
            fragmentManager.beginTransaction().replace(layoutId, fragment).commitAllowingStateLoss();
            showFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换Fragment
     * @param layoutId
     * @param fragment
     */
    protected void replaceShow(Fragment fragment, int layoutId, String tag){
        try {
            fragmentManager.beginTransaction().replace(layoutId, fragment, tag).commitAllowingStateLoss();
            showFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        synchronized (BFActivity.getmActivities()) {
            BFActivity.getmActivities().add(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        thisContext = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (BFActivity.getmActivities()) {
            BFActivity.getmActivities().remove(this);
        }
    }
}
