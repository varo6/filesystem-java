package com.filetransfer.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    /**
     * Métodos para devolver la IP privada y pública.
     * NAT happens outside of the protocol.
     * There is no way for your machine's kernel to know how your NAT box is mapping from external to internal IP addresses.
     * Other answers here offer tricks involving methods of talking to outside web sites.
     */
    public static String getPrivateIP() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Couldn't get local IP.";
        }
    }

    public static String getPublicIP() {
        String publicIP = "Couldn't get public IP.";
        try {
            URL url = new URL("https://api.ipify.org");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            publicIP = in.readLine();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicIP;
    }

}
