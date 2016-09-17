package com.example.tvshow;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bakr- on 9/17/2016.
 */
public class HttpManager {

    public static String getData(String uri) {

        BufferedReader reader = null;

        try {
            URL url1 = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url1.openConnection();

            // To get content from the web, use a string builder
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // Read the code one at a time
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();

        } catch (Exception e) {
            return null;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getData(String uri, String userName, String password) {

        BufferedReader reader = null;
        HttpURLConnection con = null;
        // Send the username and password to the web server
        byte[] loginBytes = (userName + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder().append("Basic").append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        try {
            URL url1 = new URL(uri);
            con = (HttpURLConnection) url1.openConnection();

            // A header value and send the users credential
            con.addRequestProperty("Authorization", loginBuilder.toString());

            // To get content from the web, use a string builder
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // Read the code one at a time
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                int status = con.getResponseCode();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}