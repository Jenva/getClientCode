package getClientCode;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;


/**
 * This class echoes a string called from JavaScript.
 */
public class getClientCode extends CordovaPlugin {

    private static final String TAG = "test";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getBuildConfig")) {
            String buildConfigClassName = null;
            String config = args.getString(0);
            this.getBuildConfig(buildConfigClassName, config, callbackContext);
            return true;
        }
        return false;
    }

    private void getBuildConfig(String buildConfigClassName, String message, CallbackContext callbackContext) {
        String prefix = null;
        // Load PackageInfo
		Activity activity = cordova.getActivity();
		String packageName = activity.getPackageName();
		String basePackageName = packageName;
        // Load BuildConfig class
        Class c = null;

        if (null == buildConfigClassName) {
            buildConfigClassName = packageName + ".BuildConfig";
        }

        try {
            c = Class.forName(buildConfigClassName);
        } catch (ClassNotFoundException e) {
        }

        if (null == buildConfigClassName) {
            basePackageName = activity.getClass().getPackage().getName();
            buildConfigClassName = basePackageName + ".BuildConfig";

            try {
                c = Class.forName(buildConfigClassName);
            } catch (ClassNotFoundException e) {
                callbackContext.error("BuildConfig ClassNotFoundException: " + e.getMessage());
                return;
            }
        }
        Log.d(TAG, message+ ":\""+ c + "\"");
        prefix = getClassFieldString(c, message, "");
        Log.d(TAG, message+ ": \""+ prefix + "\"");
        if (prefix != null) {
            callbackContext.success(prefix);
        } else {
            callbackContext.error("buildConfig 获取失败");
        }
    }
    /**
     * Get string of field from Class
     * @param c
     * @param fieldName
     * @param defaultReturn
     * @return
     */
    private static String getClassFieldString(Class c, String fieldName, String defaultReturn) {
        String ret = defaultReturn;
        Field field = getClassField(c, fieldName);

        if (null != field) {
            try {
                ret = (String)field.get(c);
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
        }

        return ret;
    }

    /**
     * Get field from Class
     * @param c
     * @param fieldName
     * @return
     */
    private static Field getClassField(Class c, String fieldName) {
        Field field = null;

        try {
            field = c.getField(fieldName);
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();
        }

        return field;
    }
}
