package com.detzerg.sbadmin.Modules;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

public class Http {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String API_KEY= "$2y$10$iev36jyXXaAWKnvOpA9.8OHZGqsSh7aNEWe4I3LNCWrBXCUG6z0JC";

    private static String getFormatParams(Map<String, String> data) throws Exception{
        StringBuilder query= new StringBuilder();
        int i=0;
        for (Map.Entry<String, String> entry : data.entrySet())
        {
            if(i==0) {
                query.append(entry.getKey() + "=");
            }
            else{
                query.append("&" + entry.getKey() + "=");
            }
            query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            i++;
        }
        return query.toString();
        //return params.toString();
    }

    public static String post(String url, Map<String, String> data) throws Exception{
        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        String returnData = "";
        URLConnection connection = null;
        connection = new URL(url).openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("apikey", API_KEY);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        connection.setRequestProperty("apikey", API_KEY);

        try (OutputStream output = connection.getOutputStream()) {
            output.write(getFormatParams(data).getBytes(charset));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        InputStream response = connection.getInputStream();
        try (Scanner scanner = new Scanner(response)) {
            // System.out.println(responseBody);
            returnData = scanner.useDelimiter("\\A").next();
        }
        return returnData;
    }

    public static String getResponse(String result, int option){
        result = result.substring(1, result.length()-1);
        String []r= result.split(",");
        for (int i=0; i<r.length ; i++){
            String []msg = r[i].split(":");
            if(option==0) {
                if (msg[0].equalsIgnoreCase("\"response\"") || msg[0].equalsIgnoreCase("\"r\"")) {
                    return (msg[1].substring(1, msg[1].length() - 1));
                }
            }else{
                if (msg[0].equalsIgnoreCase("\"message\"") || msg[0].equalsIgnoreCase("\"m\"")) {
                    return (msg[1].substring(1, msg[1].length() - 1));
                }
            }
        }
        return "";
    }
}

        /*StringBuilder query=new StringBuilder("name=");
        query.append(URLEncoder.encode(name,"UTF-8"));
        query.append("&nick=");
        query.append(URLEncoder.encode(nick,"UTF-8"));
        query.append("&password=");
        query.append(URLEncoder.encode(pw,"UTF-8"));
        query.append("&email=");
        query.append(URLEncoder.encode(em,"UTF-8"));*/
        /*query = String.format("name=%s&nick=%s&password=%s&email=%s",
                URLEncoder.encode(name, charset),
                URLEncoder.encode(nick, charset),
                URLEncoder.encode(pw, charset),
                URLEncoder.encode(em, charset));*/
