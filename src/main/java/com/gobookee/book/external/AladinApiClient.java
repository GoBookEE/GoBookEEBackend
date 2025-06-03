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
    private static final String API_SEARCH_URL = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String API_LOOKUP_URL = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx";
    private static final String TTB_KEY = "ttbhanck951505001";

    private static final AladinApiClient CLIENT = new AladinApiClient();
    private AladinApiClient() {}
    public static AladinApiClient aladinApiClient() {return CLIENT;}

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
    public String getBookByIdJson(String bookId) {
        try {
//            http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbhanck951505001&itemIdType=ItemId&ItemId=346891281&output=js&Version=20131101
            String urlStr = API_LOOKUP_URL +
                    "?ttbkey=" + TTB_KEY +
                    "&itemIdType=ItemId"+
                    "&ItemId="+ bookId+
                    "&output=js&Version=20131101";

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

    public String getBookSearchJson(String queryType, String query,int categoryId, int start) {
        try {
            String urlStr = API_SEARCH_URL +
                    "?ttbkey=" + TTB_KEY +
                    "&QueryType=" + queryType +
                    "&Query=" + query +
                    "&MaxResults=5" +
                    "&start=" + start +
                    "&SearchTarget=Book&output=js&Version=20131101";

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
    public String getBookDummyListJson(int categoryId, int Start) {
        try {
            String urlStr = API_URL +
                    "?ttbkey=" + TTB_KEY +
                    "&QueryType=Bestseller"+
                    "&CategoryId="+categoryId+
                    "&MaxResults=50"+
                    "&Start="+Start+
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
