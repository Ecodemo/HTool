package com.ecodemo.magic.box.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.ecodemo.magic.box.app.utils.DisplayUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings({"SimplifiableIfStatement", "ConstantConditions", "unused", "unchecked"})
public class NotchJudgementUtil
{

    public static void assertNotch(final Activity activity, final NotchJudgementListener listener)
	{
        if (listener == null || ActivityUtils.assertActivityDestroyed(activity))
		{
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
		{
            if (activity != null && activity.getWindow() != null)
			{
                View decorView = activity.getWindow().getDecorView();
                if (decorView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
				{
                    decorView.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
							@Override
							public void onWindowAttached()
							{
								listener.end(hasNotchInAndroidP(activity), true, getNotchSizeInAndroidP(activity));
							}

							@Override
							public void onWindowDetached()
							{

							}
						});
                    return;
                }
            }
			else
			{
                listener.end(false, false, new int[2]);
                return;
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
		{
            listener.end(false, false, new int[2]);
            return;
        }

        if (OSJudgementUtil.isHuaWei())
		{
            listener.end(hasNotchInHuaWei(activity), isNotchUsableInHuawei(activity), getNotchSizeInHuaWei(activity));
            return;
        }
        if (OSJudgementUtil.isOPPO())
		{
            listener.end(hasNotchInOPPO(activity), isNotchUsableInOPPO(activity), getNotchSizeInOPPO(activity));
            return;
        }
        if (OSJudgementUtil.isVIVO())
		{
            listener.end(hasNotchInVIVO(activity), isNotchUsableInVIVO(activity), getNotchSizeInVIVO(activity));
            return;
        }
        if (OSJudgementUtil.isXiaoMi())
		{
            listener.end(hasNotchInXiaoMi(activity), isNotchUsableInXiaoMi(activity), getNotchSizeInXiaoMi(activity));
            return;
        }
        listener.end(false, false, new int[2]);
    }


    /**
     * int[0]值为刘海宽度 int[1]值为刘海高度
     */
    private static int[] getNotchSizeInAndroidP(Activity activity)
	{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
		{
            if (activity != null && activity.getWindow() != null)
			{
                View decorView = activity.getWindow().getDecorView();
                if (decorView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
				{
                    WindowInsets windowInsets = decorView.getRootWindowInsets();
                    if (windowInsets != null && windowInsets.getDisplayCutout() != null)
					{
                        DisplayCutout cutout = windowInsets.getDisplayCutout();
                        return new int[]{getScreenWidthInPx(activity) - cutout.getSafeInsetLeft() - cutout.getSafeInsetRight(), cutout.getSafeInsetTop()};
                    }
                }
            }
        }
        return new int[2];
    }

    /**
     * 因为小米获取不到刘海区的size，所以认为不可绘制
     *
     * @param context
     * @return
     */
    private static int[] getNotchSizeInXiaoMi(Context context)
	{
        return new int[2];
    }

    /**
     * VIVO获取刘海的尺寸
     *
     * @param context
     * @return
     */
    private static int[] getNotchSizeInVIVO(Context context)
	{
        return new int[]{DisplayUtil.dip2px(context, 100), DisplayUtil.dip2px(context, 27)};
    }

    /**
     * OPPO获取刘海的尺寸
     *
     * @param context
     * @return
     */
    private static int[] getNotchSizeInOPPO(Context context)
	{
        return new int[]{324, 80};
    }

    /**
     * 华为获取刘海的尺寸
     *
     * @param context
     * @return
     */
    private static int[] getNotchSizeInHuaWei(Context context)
	{
        int[] ret = new int[]{0, 0};
        try
		{
            ClassLoader cl = context.getClassLoader();
            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = hwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(hwNotchSizeUtil);
        }
		catch (ClassNotFoundException e)
		{
            Log.e("test", "getNotchSize ClassNotFoundException");
        }
		catch (NoSuchMethodException e)
		{
            Log.e("test", "getNotchSize NoSuchMethodException");
        }
		catch (Exception e)
		{
            Log.e("test", "getNotchSize Exception");
        }
        return ret;
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean hasNotchInAndroidP(Activity activity)
	{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
		{
            try
			{
                if (activity == null || activity.getWindow() == null || activity.getWindow().getDecorView() == null)
				{
                    return false;
                }

                if (OSJudgementUtil.isVIVO())
				{
                    // AndroidP-VIVO的displayCutout为null，默认按有刘海区域处理
                    return true;
                }

                View decorView = activity.getWindow().getDecorView();
                if (decorView != null && decorView.getRootWindowInsets() != null)
				{
                    DisplayCutout displayCutout = decorView.getRootWindowInsets().getDisplayCutout();
                    if (displayCutout != null)
					{
                        List<Rect> rectList = displayCutout.getBoundingRects();
                        if (rectList == null || rectList.size() == 0)
						{
                            return false;
                        }
						else
						{
                            return true;
                        }
                    }
                }
            }
			catch (Exception e)
			{
                e.printStackTrace();
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private static boolean hasNotchInVIVO(Context context)
	{
        boolean ret = false;
        try
		{
            ClassLoader cl = context.getClassLoader();
            @SuppressLint("PrivateApi") Class feature = cl.loadClass("android.util.FtFeature");
            Method get = feature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) get.invoke(feature, 0x00000020);

        }
		catch (ClassNotFoundException e)
		{
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        }
		catch (NoSuchMethodException e)
		{
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        }
		catch (Exception e)
		{
            Log.e("test", "hasNotchInScreen Exception");
        }
        return ret;
    }

    private static boolean hasNotchInXiaoMi(Context context)
	{
        return PropertyUtils.get("ro.miui.notch", "0").equalsIgnoreCase("1");
    }

    private static boolean hasNotchInOPPO(Context context)
	{
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    @SuppressWarnings("unchecked")
    private static boolean hasNotchInHuaWei(Context context)
	{
        boolean ret = false;
        try
		{
            ClassLoader cl = context.getClassLoader();
            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = hwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(hwNotchSizeUtil);
        }
		catch (ClassNotFoundException e)
		{
            Log.e("test", "hasNotchInScreen ClassNotFoundException");
        }
		catch (NoSuchMethodException e)
		{
            Log.e("test", "hasNotchInScreen NoSuchMethodException");
        }
		catch (Exception e)
		{
            Log.e("test", "hasNotchInScreen Exception");
        }
        return ret;
    }


    /**
     * 刘海区域是否可以隐藏的场景
     *
     * @param activity
     * @return
     */
    public static boolean isNotchUsable(Activity activity)
	{
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || ActivityUtils.assertActivityDestroyed(activity))
		{
            return false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
		{
            return isNotchUsableInAndroidP(activity);
        }
        if (OSJudgementUtil.isHuaWei())
		{
            return isNotchUsableInHuawei(activity);
        }
        if (OSJudgementUtil.isOPPO())
		{
            return isNotchUsableInOPPO(activity);
        }
        if (OSJudgementUtil.isVIVO())
		{
            return isNotchUsableInVIVO(activity);
        }
        if (OSJudgementUtil.isXiaoMi())
		{
            return isNotchUsableInXiaoMi(activity);
        }
        return false;
    }

    /**
     * 默认AndroidP+刘海屏不可用
     *
     * @param activity
     * @return
     */
    private static boolean isNotchUsableInAndroidP(Activity activity)
	{
        return false;
    }

    /**
     * vivo刘海屏可用
     *
     * @param activity
     * @return
     */
    private static boolean isNotchUsableInVIVO(Activity activity)
	{
        return true;
    }

    /**
     * oppo刘海屏可用
     *
     * @param activity
     * @return
     */
    private static boolean isNotchUsableInOPPO(Activity activity)
	{
        return true;
    }

    /**
     * 判断Huawei刘海屏是否可用
     *
     * @param activity
     * @return
     */
    private static boolean isNotchUsableInHuawei(Activity activity)
	{
        // 0表示“默认”，1表示“隐藏显示区域”
        return Settings.Secure.getInt(activity.getContentResolver(), "display_notch_status", 0) == 0;
    }

    /**
     * 判断XiaoMi刘海屏是否可用
     *
     * @param activity
     * @return
     */
    private static boolean isNotchUsableInXiaoMi(Activity activity)
	{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
		{
            // 0表示“默认”，1表示“隐藏显示区域”
            return Settings.Global.getInt(activity.getContentResolver(), "force_black", 0) == 0;
        }
		else
		{
            return false;
        }
    }


    /**
     * 在notchUsable=true时，使用刘海区显示（小米和华为动态配置）
     *
     * @param activity
     */
    public static void addNotchFlag(Activity activity)
	{
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || ActivityUtils.assertActivityDestroyed(activity))
		{
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
		{
            return;
        }

        if (OSJudgementUtil.isHuaWei())
		{
            addNotchFlagInHuaWei(activity);
            return;
        }
        if (OSJudgementUtil.isOPPO())
		{
            return;
        }
        if (OSJudgementUtil.isVIVO())
		{
            return;
        }
        if (OSJudgementUtil.isXiaoMi())
		{
            addNotchFlagInXiaoMi(activity);
            return;
        }
    }

    /**
     * 在notchUsable=false时，不使用刘海区显示（小米和华为动态配置）
     *
     * @param activity
     */
    public static void clearNotchFlag(Activity activity)
	{
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || ActivityUtils.assertActivityDestroyed(activity))
		{
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
		{
            return;
        }

        if (OSJudgementUtil.isHuaWei())
		{
            clearNotchFlagInHuaWei(activity);
            return;
        }
        if (OSJudgementUtil.isOPPO())
		{
            return;
        }
        if (OSJudgementUtil.isVIVO())
		{
            return;
        }
        if (OSJudgementUtil.isXiaoMi())
		{
            clearNotchFlagInXiaoMi(activity);
        }
    }

    /**
     * Window级别的控制接口-华为不使用刘海区显示
     *
     * @param activity
     */
    private static void clearNotchFlagInHuaWei(Activity activity)
	{
        if (activity.getWindow() == null)
		{
            return;
        }

        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        try
		{
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("clearHwFlags", int.class);
            method.invoke(layoutParamsExObj, 0x00010000);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
    }

    /**
     * Window级别的控制接口-华为使用刘海区显示
     *
     * @param activity
     */
    private static void addNotchFlagInHuaWei(Activity activity)
	{
        if (activity.getWindow() == null)
		{
            return;
        }
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        try
		{
            Class layoutParamsExCls = Class.forName("com.huawei.android.view.LayoutParamsEx");
            Constructor con = layoutParamsExCls.getConstructor(WindowManager.LayoutParams.class);
            Object layoutParamsExObj = con.newInstance(layoutParams);
            Method method = layoutParamsExCls.getMethod("addHwFlags",
														int.class);
            method.invoke(layoutParamsExObj, 0x00010000);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
    }

    /**
     * Window级别的控制接口-小米不使用刘海区显示
     *
     * @param activity
     */
    private static void clearNotchFlagInXiaoMi(Activity activity)
	{
        if (activity.getWindow() == null)
		{
            return;
        }

        // 横竖屏都绘制到耳朵区
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try
		{
            Method method = Window.class.getMethod("clearExtraFlags", int.class);
            method.invoke(activity.getWindow(), flag);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
    }

    /**
     * Window级别的控制接口-小米使用刘海区显示
     *
     * @param activity
     */
    private static void addNotchFlagInXiaoMi(Activity activity)
	{
        if (activity.getWindow() == null)
		{
            return;
        }

        // 横竖屏都绘制到耳朵区
        int flag = 0x00000100 | 0x00000200 | 0x00000400;
        try
		{
            Method method = Window.class.getMethod("addExtraFlags", int.class);
            method.invoke(activity.getWindow(), flag);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
    }

    /**
     * Window级别的控制接口-Android P动态控制是否使用刘海区显示
     *
     * WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
     * WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
     * WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
     *
     * @param window
     */
    public static void setAndroidPDisplayCutoutMode(Window window, int cutoutMode)
	{
        if (window == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
		{
            return;
        }

        WindowManager.LayoutParams windowManagerDu = window.getAttributes();
        windowManagerDu.layoutInDisplayCutoutMode = cutoutMode;
        window.setAttributes(windowManagerDu);
    }

    public interface NotchJudgementListener
	{
        /**
         * 刘海信息回调
         *
         * @param hasNotch
         * @param notchUsable
         * @param notchSize
         */
        void end(boolean hasNotch, boolean notchUsable, int[] notchSize);
    }


	public static class PropertyUtils
	{
		private static volatile Method set = null;
		private static volatile Method get = null;

		public static void set(String prop, String value)
		{

			try
			{
				if (null == set)
				{
					synchronized (PropertyUtils.class)
					{
						if (null == set)
						{
							Class<?> cls = Class.forName("android.os.SystemProperties");
							set = cls.getDeclaredMethod("set", new Class<?>[]{String.class, String.class});
						}
					}
				}
				set.invoke(null, new Object[]{prop, value});
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}


		public static String get(String prop, String defaultvalue)
		{
			String value = defaultvalue;
			try
			{
				if (null == get)
				{
					synchronized (PropertyUtils.class)
					{
						if (null == get)
						{
							Class<?> cls = Class.forName("android.os.SystemProperties");
							get = cls.getDeclaredMethod("get", new Class<?>[]{String.class, String.class});
						}
					}
				}
				value = (String) (get.invoke(null, new Object[]{prop, defaultvalue}));
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
			return value;
		}
	}

	public static int getScreenWidthInPx(Context context)
	{
        if (context == null)
		{
            return 0;
        }

        return context.getResources().getDisplayMetrics().widthPixels;
    }
}

