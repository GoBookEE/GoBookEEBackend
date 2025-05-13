package com.gobookee.book.external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AladinApiClient {
    //http://www.aladin.co.kr/ttb/api/ItemList.aspx?
    // ttbkey=ttbhanck951505001
    // &QueryType=Bestseller
    // &CategoryId=27660
    // &MaxResults=200
    // &start=1
    // &SearchTarget=Book
    // &output=JS
    // &Version=20131101
    private static final String API_URL = "https://www.aladin.co.kr/ttb/api/ItemList.aspx";
    private static final String TTB_KEY = "ttbhanck951505001";

    public String getBookListJson(String queryType, int maxResults) {
        try {
            String urlStr = API_URL +
                    "?ttbkey=" + TTB_KEY +
                    "&QueryType=" + queryType +
                    "&MaxResults=" + maxResults +
                    "&start=1&SearchTarget=Book&output=js&Version=20131101";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                return sb.toString();
            } else {
                System.out.println("알라딘 API 요청 실패: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public String getBookDummyListJson(String queryType, int maxResults) {
        try {
            String urlStr = API_URL +
                    "?ttbkey=" + TTB_KEY +
                    "&QueryType=Bestseller"+
                    "&CategoryId=27660"+
                    "&MaxResults=200"+
                    "&start=1"+
                    "&SearchTarget=Book"+
                    "&output=JS"+
                    "&Version=20131101";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                return sb.toString();
            } else {
                System.out.println("알라딘 API 요청 실패: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
