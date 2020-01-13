package com.smile.box.app;

import android.app.Activity;
import android.os.Build;
import java.lang.ref.WeakReference;

public class ActivityUtils
 {
    /**
     * @param activity
     * @return true=Activity已经销毁,false=没有销毁
     */
    public static boolean assertActivityDestroyed(Activity activity) {
        if (activity == null) {
            return true;
        }

        WeakReference<Activity> weakReference = new WeakReference<>(activity);
        Activity weakActivity = weakReference.get();
        if (weakActivity == null) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && weakActivity.isDestroyed()) {
            return true;
        }
        if (weakActivity.isFinishing()) {
            return true;
        }
        return false;
    }
}

