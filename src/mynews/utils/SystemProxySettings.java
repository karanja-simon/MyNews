/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.utils;

import mynews.preferences.Preference;

/**
 *
 * @author RESEARCH2
 */
public class SystemProxySettings {

    public static void setProxySettings() {
        Preference.getPreferences();
        System.setProperty("proxySet", Preference.isProxySet());
        System.setProperty("http.proxyHost", Preference.getProxyHost());
        System.setProperty("http.proxyPort", Preference.getProxyPort());
        if(Preference.isAuthenticate()){
        System.getProperties().put("http.proxyUser", Preference.getProxyUser());
        System.getProperties().put("http.proxyPassword", Preference.getProxyPass());
        }
    }

}
