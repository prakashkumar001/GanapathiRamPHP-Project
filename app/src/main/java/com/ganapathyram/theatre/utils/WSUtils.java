package com.ganapathyram.theatre.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ashwin on 24/09/2015.
 */
public class WSUtils {

    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final String INVALIDCONNCETION = "Invalid connection";
    public static final String INVALIDPOSTPARAMS = "Invalid postParameters";
    public static final String BADRESPONSE = "Bad response received";


    private String getPostDataString(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return result.toString();
    }

    private HttpURLConnection connectToURL(String url, String method) {
        try {
            URL connectionURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectionURL.openConnection();
            conn.setRequestMethod(method);
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            if (method.equals(WSUtils.HTTP_POST)) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
            }
            return conn;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public String getResultFromHttpRequest(String url, String method, HashMap<String, String> params) {
        HttpURLConnection conn = connectToURL(url, method);
        String postData = getPostDataString(params);
        String response = "";
        if (conn == null) {
            return INVALIDCONNCETION;
        }
        if (postData == null) {
            return INVALIDPOSTPARAMS;
        }
        try {
            if (method.equals(WSUtils.HTTP_POST)) {
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
//                response = new String(new MCrypt().decrypt(response));
                return response;
            } else {

                return BADRESPONSE + getErrorMessage(responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public byte[] getImageFromURL(URL url) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = null;
        try {
            is = url.openStream();
            byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
            int n;

            while ((n = is.read(byteChunk)) > 0) {
                baos.write(byteChunk, 0, n);
            }
            if (is != null) {
                is.close();
            }

        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace();
            // Perform any other exception handling that's appropriate.
        }
        return baos.toByteArray();
    }


    private String getErrorMessage(int responseCode) {
        switch (responseCode) {
            case HttpsURLConnection.HTTP_ACCEPTED:
                return WSErrorCodes.HTTP_ACCEPTED;
            case HttpsURLConnection.HTTP_BAD_GATEWAY:
                return WSErrorCodes.HTTP_BAD_GATEWAY;
            case HttpsURLConnection.HTTP_BAD_METHOD:
                return WSErrorCodes.HTTP_BAD_METHOD;
            case HttpsURLConnection.HTTP_BAD_REQUEST:
                return WSErrorCodes.HTTP_BAD_REQUEST;
            case HttpsURLConnection.HTTP_CLIENT_TIMEOUT:
                return WSErrorCodes.HTTP_CLIENT_TIMEOUT;
            case HttpsURLConnection.HTTP_CONFLICT:
                return WSErrorCodes.HTTP_CONFLICT;
            case HttpsURLConnection.HTTP_CREATED:
                return WSErrorCodes.HTTP_CREATED;
            case HttpsURLConnection.HTTP_ENTITY_TOO_LARGE:
                return WSErrorCodes.HTTP_ENTITY_TOO_LARGE;
            case HttpsURLConnection.HTTP_FORBIDDEN:
                return WSErrorCodes.HTTP_FORBIDDEN;
            case HttpsURLConnection.HTTP_GATEWAY_TIMEOUT:
                return WSErrorCodes.HTTP_GATEWAY_TIMEOUT;
            case HttpsURLConnection.HTTP_VERSION:
                return WSErrorCodes.HTTP_VERSION;
            case HttpsURLConnection.HTTP_USE_PROXY:
                return WSErrorCodes.HTTP_USE_PROXY;
            case HttpsURLConnection.HTTP_GONE:
                return WSErrorCodes.HTTP_GONE;
            case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                return WSErrorCodes.HTTP_INTERNAL_ERROR;
            case HttpsURLConnection.HTTP_LENGTH_REQUIRED:
                return WSErrorCodes.HTTP_LENGTH_REQUIRED;
            case HttpsURLConnection.HTTP_MOVED_PERM:
                return WSErrorCodes.HTTP_MOVED_PERM;
            case HttpsURLConnection.HTTP_MOVED_TEMP:
                return WSErrorCodes.HTTP_MOVED_TEMP;
            case HttpsURLConnection.HTTP_NO_CONTENT:
                return WSErrorCodes.HTTP_NO_CONTENT;
            case HttpsURLConnection.HTTP_NOT_ACCEPTABLE:
                return WSErrorCodes.HTTP_NOT_ACCEPTABLE;
            case HttpsURLConnection.HTTP_NOT_AUTHORITATIVE:
                return WSErrorCodes.HTTP_NOT_AUTHORITATIVE;
            case HttpsURLConnection.HTTP_NOT_FOUND:
                return WSErrorCodes.HTTP_NOT_FOUND;
            case HttpsURLConnection.HTTP_NOT_IMPLEMENTED:
                return WSErrorCodes.HTTP_NOT_IMPLEMENTED;
            case HttpsURLConnection.HTTP_NOT_MODIFIED:
                return WSErrorCodes.HTTP_NOT_MODIFIED;
            case HttpsURLConnection.HTTP_MULT_CHOICE:
                return WSErrorCodes.HTTP_MULT_CHOICE;
            case HttpsURLConnection.HTTP_PARTIAL:
                return WSErrorCodes.HTTP_PARTIAL;
            case HttpsURLConnection.HTTP_PAYMENT_REQUIRED:
                return WSErrorCodes.HTTP_PAYMENT_REQUIRED;
            case HttpsURLConnection.HTTP_PRECON_FAILED:
                return WSErrorCodes.HTTP_PRECON_FAILED;
            case HttpsURLConnection.HTTP_PROXY_AUTH:
                return WSErrorCodes.HTTP_PROXY_AUTH;
            case HttpsURLConnection.HTTP_REQ_TOO_LONG:
                return WSErrorCodes.HTTP_REQ_TOO_LONG;
            case HttpsURLConnection.HTTP_RESET:
                return WSErrorCodes.HTTP_RESET;
            case HttpsURLConnection.HTTP_SEE_OTHER:
                return WSErrorCodes.HTTP_SEE_OTHER;
            case HttpsURLConnection.HTTP_UNAUTHORIZED:
                return WSErrorCodes.HTTP_UNAUTHORIZED;
            case HttpsURLConnection.HTTP_UNAVAILABLE:
                return WSErrorCodes.HTTP_UNAVAILABLE;
            case HttpsURLConnection.HTTP_UNSUPPORTED_TYPE:
                return WSErrorCodes.HTTP_UNSUPPORTED_TYPE;
            default:
                return null;
        }

    }

    /**
     * Asyncrhonous call to syncrhonize values
     */

    public String responsedetailsfromserver(String url, String json) throws IOException {


        URL connectionURL = null;
        String response="";
        try {
            connectionURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) connectionURL.openConnection();
            conn.setRequestMethod(HTTP_POST);
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(json);
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                 response = "";
                while ((line = br.readLine()) != null) {
                    response += line;
                }
//                response = new String(new MCrypt().decrypt(response));
                return response;

            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }


        return response;
    }






}
