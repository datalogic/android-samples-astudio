package com.sampleapp.packageinstallersample;

import static com.datalogic.device.app.AppManagerException.SUCCESS;

import android.content.Context;
import android.util.Log;

import com.datalogic.device.app.PackageInstaller;
import com.datalogic.device.app.PackageInstallerListener;
import com.datalogic.device.app.PackageInstallerResult;

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
public class PackageInstallerHelper {
    public static final String TAG = "PackageInstallerHelper";
    private PackageInstallerSample mPackageInstallerSample;
    private final PackageInstaller mPackageInstaller;
    private final PackageInstallerListener mInstallerListener = results -> {
        StringBuilder summaryLog = new StringBuilder();
        for (PackageInstallerResult result : results) {
            summaryLog.append("=========================\n");
            String log = "data = " + result.data + "\n\n" +
                    "event = " + result.event + "\n\n" +
                    "result = " + (result.result == SUCCESS ? "SUCCESS" : "FAILURE " + result.result + "\n\n");
            summaryLog.append(log);
        }
        mPackageInstallerSample.showLog("Installation result: ", summaryLog.toString());
        Log.d(TAG, "Installation result: " + summaryLog);
    };

    /**
     * Constructor for ManagerHelper.
     * <p>
     * Initializes the `PackageInstaller` instances for managing applications and packages.
     * </p>
     *
     * @param context The application context used to initialize the helper.
     */
    public PackageInstallerHelper(Context context) {
        mPackageInstallerSample = (PackageInstallerSample) context;
        mPackageInstaller = new PackageInstaller(context);
    }

    /**
     * Installs an application package.
     * <p>
     * Uses the `PackageInstaller` API to install a package and logs the result.
     * </p>
     *
     * @param packageName The name of the package to install.
     * @param force A boolean indicating whether to force upgrade the package if existed.
     */
    public void installPackage(String packageName, boolean force) {
        int result = mPackageInstaller.install(packageName, force, mInstallerListener);
        String log = "\n\n" + "package_name: " + packageName +
                     "\n\n" + "force: " + force +
                     "\n\n" + "result: " + (result == SUCCESS ? "SUCCESS" : "FAILURE " + result);
        Log.d(TAG, "Installing package: " + log);
    }

}
