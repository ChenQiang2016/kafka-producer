package com.chen.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class Http {

    public static String get(String url) {
        return sendGet(url, "UTF-8", null);
    }

    public static String getAndCookie(String url, String cookie) {
        return sendGet(url, "UTF-8", cookie);
    }

    public static String get(String url, String charset) {
        return sendGet(url, charset, null);
    }

    public static String sendGet(String url, String result, String cookie) {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            if (cookie != null && !cookie.equals("")) {
                get.addHeader(new BasicHeader("Cookie", cookie));
            }
            response = client.execute(get);
            result = EntityUtils.toString(response.getEntity(), result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (client != null)
                    client.close();
                if (response != null)
                    response.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}