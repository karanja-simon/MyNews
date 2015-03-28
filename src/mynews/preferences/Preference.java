/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.preferences;

import java.util.prefs.Preferences;

/**
 *
 * @author RESEARCH2
 */
public class Preference {

    public static String proxyHost;
    public static String proxyPort;
    public static String proxyUser;
    public static String proxyPass;
    public static String proxySet;
    public static boolean authenticate = false;
    
    private static final Preferences userPrefs;

    static {
        userPrefs = Preferences.userNodeForPackage(Preference.class);
    }

    public static String getProxyHost() {
        return proxyHost;
    }

    public static boolean isAuthenticate() {
        return authenticate;
    }

    public static String getProxyPort() {
        return proxyPort;
    }

    public static String getProxyUser() {
        return proxyUser;
    }

    public static String getProxyPass() {
        return proxyPass;
    }

    public static String isProxySet() {
        return proxySet;
    }

    public static void setPreferences() {
        System.out.println("authentication status:::" + authenticate);
        try {
            userPrefs.put("proxy_name", proxyHost);
            userPrefs.put("proxy_port", proxyPort);
            userPrefs.put("proxy_set", proxySet);
            if (authenticate) {
                userPrefs.putBoolean("proxy_auth", authenticate);
                userPrefs.put("proxy_user", proxyUser);
                userPrefs.put("proxy_pass", proxyPass);
            } else {
                userPrefs.put("proxy_user", "");
                userPrefs.put("proxy_pass", "");
            }

            System.out.println("prefernce saved!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getPreferences() {
        proxyHost = userPrefs.get("proxy_name", null);
        proxyPort = userPrefs.get("proxy_port", null);
        proxyUser = userPrefs.get("proxy_user", null);
        proxyPass = userPrefs.get("proxy_pass", null);
        proxySet = userPrefs.get("proxy_set", "false");
        authenticate = userPrefs.getBoolean("proxy_auth", false);
    }

}
