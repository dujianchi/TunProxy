package tun.proxy;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class MyApplication extends Application {
    private final static String PREF_VPN_MODE = "pref_vpn_connection_mode";
    private final static String PREF_APP_KEY[] = {"pref_vpn_disallowed_application", "pref_vpn_allowed_application"};

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (loadVPNMode() != VPNMode.ALLOW) {
            storeVPNMode(VPNMode.ALLOW);
            Set<String> ids = new HashSet<>();
            String name = MyApplication.class.getPackage().getName();
            Log.d("-----------------", "package = " + name);
            ids.add(name);
            storeVPNApplication(VPNMode.ALLOW, ids);
        }
    }

    public enum VPNMode {DISALLOW, ALLOW};
    public enum AppSortBy {APPNAME, PKGNAME};
    public enum AppOrderBy {ASC, DESC};
    public enum AppFiltertBy {APPNAME, PKGNAME};

    public VPNMode loadVPNMode() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String vpn_mode = sharedPreferences.getString(PREF_VPN_MODE, MyApplication.VPNMode.DISALLOW.name());
        return VPNMode.valueOf(vpn_mode);
    }

    public void storeVPNMode(VPNMode mode) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_VPN_MODE, mode.name()).apply();
        return;
    }

    public Set<String> loadVPNApplication(VPNMode mode) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final Set<String> preference = prefs.getStringSet(PREF_APP_KEY[mode.ordinal()], new HashSet<String>());
        return preference;
    }

    public void storeVPNApplication(VPNMode mode, final Set<String> set) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(PREF_APP_KEY[mode.ordinal()], set).apply();
        return;
    }

}
