/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author RESEARCH2
 */
public class TestInternetConnection {
    public static boolean isNetAvailable() {
        SystemProxySettings.setProxySettings();
        try {
            final URL url = new URL("http://feeds.abcnews.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        //System.out.println("Online: " + TestInternetConnection.netIsAvailable());
    }
}
