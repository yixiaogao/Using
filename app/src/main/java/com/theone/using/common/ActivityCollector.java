package com.theone.using.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuan on 2016/4/20.
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        System.out.println("finish所有Activity");
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
