package com.sampleapp.applicationmanagersample;

import static com.datalogic.device.app.AppManagerException.SUCCESS;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import com.datalogic.device.app.AppManager;
import com.datalogic.device.app.PackageInstaller;
import com.datalogic.device.app.PackageInstallerListener;
import com.datalogic.device.app.PackageInstallerResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ManagerHelper is a utility class that provides methods to manage application packages and permissions.
 * <p>
 * This class simplifies interactions with the Datalogic SDK's `AppManager` and `PackageInstaller` APIs.
 * It includes methods for installing packages, granting/revoking permissions, managing battery optimization exemptions,
 * and controlling application visibility. The class also provides a method to retrieve all available permissions
 * from the Android `Manifest.permission` class.
 * </p>
 * <p>
 * Key functionalities include:
 * <ul>
 *   <li>Installing application packages using the `PackageInstaller` API.</li>
 *   <li>Granting or revoking permissions for specific applications.</li>
 *   <li>Managing battery optimization exemptions for applications.</li>
 *   <li>Hiding or unhidden applications on the device.</li>
 *   <li>Enabling or disabling notification channels for specific applications.</li>
 * </ul>
 * </p>
 */
public class AppManagerHelper {
    public static final String TAG = "ManagerHelper";
    private static final List<String> mPermissionList = new ArrayList<>();
    private ApplicationManagerSample mApplicationManagerSample;
    private final AppManager mAppManager;

    /**
     * Constructor for ManagerHelper.
     * <p>
     * Initializes the `AppManager` and `PackageInstaller` instances for managing applications and packages.
     * </p>
     *
     * @param context The application context used to initialize the helper.
     */
    public AppManagerHelper(Context context) {
        mApplicationManagerSample = (ApplicationManagerSample) context;
        mAppManager = new AppManager(context);
    }

    /**
     * Retrieves all permissions defined in the Android `Manifest.permission` class.
     * <p>
     * This method uses reflection to access all fields in the `Manifest.permission` class and
     * adds them to a static list. If the list is already populated, it returns the cached list.
     * </p>
     *
     * @return A list of all available permissions.
     */
    public static List<String> getAllPermissions() {
        if (mPermissionList.isEmpty()) {
            Field[] fields = Manifest.permission.class.getFields();
            for (Field field : fields) {
                try {
                    String permission = (String) field.get(null);
                    mPermissionList.add(permission);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return mPermissionList;
    }

    /**
     * Grants all permissions to an application.
     * <p>
     * This method uses the `AppManager` API to grant all available permissions
     * to the specified package. It logs the result of the operation and displays
     * it in the application's log view.
     * </p>
     *
     * @param packageName The name of the package to grant all permissions to.
     */
    public void grantAllPermissions(String packageName) {
        int result = mAppManager.grantAllPermissions(packageName);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        Log.d(TAG, "Granting all permissions result: " + log);
        mApplicationManagerSample.showLog("Granting all permissions result: ", log);
    }

    /**
     * Grants permissions to an application.
     * <p>
     * Uses the `AppManager` API to grant a list of permissions to a specific package.
     * Logs the result of the operation.
     * </p>
     *
     * @param packageName The name of the package to grant permissions to.
     * @param permissions A list of permissions to grant.
     */
    public void grantPermissions(String packageName, ArrayList<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            mApplicationManagerSample.showLog("Granting permissions result: ", "FAILURE: No permissions provided");
            return;
        }
        int result = mAppManager.grantPermissions(packageName, permissions);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "permissions: " + permissions +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        Log.d(TAG, "Granting permissions: " + log);
        mApplicationManagerSample.showLog("Granting permissions result: ", log);
    }

    /**
     * Revokes permissions from an application.
     * <p>
     * Uses the `AppManager` API to revoke a list of permissions from a specific package.
     * Logs the result of the operation.
     * </p>
     *
     * @param packageName The name of the package to revoke permissions from.
     * @param permissions A list of permissions to revoke.
     */
    public void revokePermissions(String packageName, ArrayList<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            mApplicationManagerSample.showLog("Revoking permissions result: ", "FAILURE: No permissions provided");
            return;
        }
        int result = mAppManager.revokePermissions(packageName, permissions);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "permissions: " + permissions +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        Log.d(TAG, "Revoking permissions: " + log);
        mApplicationManagerSample.showLog("Revoking permissions result: ", log);
    }

    /**
     * Adds a battery optimization exemption for an application.
     * <p>
     * Uses the `AppManager` API to exempt a specific package from battery optimization.
     * Logs the result of the operation.
     * </p>
     *
     * @param packageName The name of the package to exempt from battery optimization.
     */
    public void addBatteryOptimizationExemption(String packageName) {
        int result = mAppManager.addBatteryOptimizationExemption(packageName);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        Log.d(TAG, "Adding battery optimization exemption: " + log);
        mApplicationManagerSample.showLog("Adding battery optimization exemption result: ", log);
    }

    /**
     * Removes a battery optimization exemption for an application.
     * <p>
     * Uses the `AppManager` API to remove a specific package from battery optimization exemptions.
     * Logs the result of the operation.
     * </p>
     *
     * @param packageName The name of the package to remove from battery optimization exemptions.
     */
    public void removeBatteryOptimizationExemption(String packageName) {
        int result = mAppManager.removeBatteryOptimizationExemption(packageName);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        Log.d(TAG, "Removing battery optimization exemption: " + log);
        mApplicationManagerSample.showLog("Removing battery optimization exemption result: ", log);
    }

    /**
     * Sets the visibility of an application.
     * <p>
     * Uses the `AppManager` API to hide or unhidden a specific package.
     * Logs the result of the operation.
     * </p>
     *
     * @param packageName The name of the package to hide or unhidden.
     * @param hidden A boolean indicating whether to hide (true) or unhidden (false) the application.
     */
    public void setApplicationHidden(String packageName, boolean hidden) {
        int result = mAppManager.setApplicationHidden(packageName, hidden);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "hidden: " + hidden +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        Log.d(TAG, "Setting application hidden: " + log);
        mApplicationManagerSample.showLog("Setting " + (hidden ? "hide" : "unhidden") + " application result: ", log);
    }

    /**
     * Enables or disables notification channels for an application.
     * <p>
     * Uses the `AppManager` API to enable or disable specific notification channels for a package.
     * Logs the result of the operation.
     * </p>
     *
     * @param packageName The name of the package to modify notification channels for.
     * @param notificationChannels The notification channels identifier to enable or disable.
     * @param enable A boolean indicating whether to enable (true) or disable (false) the channels.
     */
    public void enableNotificationChannel(String packageName, String notificationChannels, boolean enable) {
        int result = mAppManager.enableNotificationChannels(packageName, notificationChannels, enable);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "notificationChannels: " + notificationChannels +
                     "\n\n" + "enable: " + enable +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        mApplicationManagerSample.showLog((enable ? "Enable" : "Disable") + " notification channel result: ", log);
    }

    /**
     * Enables or disables notifications for an application.
     * <p>
     * Uses the `AppManager` API to enable or disable all notifications for a specific package.
     * Logs the result of the operation.
     * </p>
     *
     * @param packageName The name of the package to modify notifications for.
     * @param enabled A boolean indicating whether to enable (true) or disable (false) notifications.
     */
    public void setNotificationsEnabledForPackage(String packageName, boolean enabled) {
        int result = mAppManager.setNotificationsEnabledForPackage(packageName, enabled);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "enabled: " + enabled +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        Log.d(TAG, "Setting notification " + (enabled ? "enable" : "disable") + ": " + log);
        mApplicationManagerSample.showLog("Setting notification " + (enabled ? "enable" : "disable") + " for package result: ", log);
    }
}
