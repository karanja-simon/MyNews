/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.utils;

/**
 *
 * @author RESEARCH2
 */
public class SystemProxySettings {

    public static void setProxySettings() {
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", "192.168.101.11");
        System.setProperty("http.proxyPort", "3128");
    }

}
