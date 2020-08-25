package com.ecodemo.magic.box.app;
import android.app.Activity;
import android.os.Build;

public class NotchManager {

    /**
     * 是否有刘海
     */
    private static boolean mHasNotch = false;
    /**
     * 判断刘海区域是否隐藏
     */
    private static boolean mNotchUsable = true;
    /**
     * 计算获取刘海尺寸：width、height
     */
    public static int[] mNotchSize = new int[2];

    private static boolean isJudgeNotch = false;

    /**
     * 监听虚拟按键的初次判断及同一屏幕方向下的变化
     * <p>
     * 结果存储在静态变量
     */
    public static void initNotchListener(final Activity activity) {
        if (ActivityUtils.assertActivityDestroyed(activity)) {
            return;
        }

        if (isJudgeNotch) {
            listenNotchOnChange(activity);
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            isJudgeNotch = true;
            mHasNotch = false;
            mNotchUsable = false;
            mNotchSize = new int[2];
            return;
        }

        NotchJudgementUtil.assertNotch(activity, new NotchJudgementUtil.NotchJudgementListener() {
				@Override
				public void end(boolean hasNotch, boolean notchUsable, int[] notchSize) {
					isJudgeNotch = true;
					mHasNotch = hasNotch;
					mNotchUsable = notchUsable;
					mNotchSize = notchSize;

					if (mHasNotch) {
						listenOnChange(activity);
					}
				}
			});
    }

    /**
     * true：有刘海、且刘海区域显示
     *
     * @return
     */
    private static boolean getNotchAble() {
        return mHasNotch && mNotchUsable;
    }

    /**
     * true：有刘海
     *
     * @return
     */
    private static boolean getHasNotch() {
        return mHasNotch;
    }

    /**
     * true：刘海区域显示
     *
     * @return
     */
    private static boolean getNotchUsable() {
        return mNotchUsable;
    }

    /**
     * 监听到刘海区域是否隐藏发生变化。如果是，需要做如重新布局等相关操作
     *
     * @param activity
     */
    private static void listenNotchOnChange(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || !mHasNotch) {
            return;
        }

        boolean notchUsable = NotchJudgementUtil.isNotchUsable(activity);
        if (mNotchUsable != notchUsable) {
            mNotchUsable = notchUsable;
        }

        listenOnChange(activity);
    }

    /**
     * 动态地增加/清除相关的flag，请求扩展到刘海区显示/不扩展到刘海区显示
     *
     * @param activity
     */
    private static void listenOnChange(Activity activity) {
        if (mNotchUsable) {
            NotchJudgementUtil.addNotchFlag(activity);
        } else {
            NotchJudgementUtil.clearNotchFlag(activity);
        }
    }

    /**
     * 旋转屏幕时动态的去清除或加上相关的flag
     *
     * @param activity
     * @param orientation
     */
    public static void listenScreenOrientation(Activity activity, boolean orientation) {
        if (mNotchUsable && orientation) {
            NotchJudgementUtil.addNotchFlag(activity);
        } else {
            NotchJudgementUtil.clearNotchFlag(activity);
        }
    }
}

