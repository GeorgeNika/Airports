package ua.george_nika.airports.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ua.george_nika.airports.R;

public class HTTPActivity extends Activity {

    private String viewState;
    private String codeKey;
    private String codeValue;

    private static final String WIZZAIR_URL = "http://wizzair.ua/";
    private static final String WIZZAIR_URL1 = "https://wizzair.com/";
    private final Executor requestExecutor = Executors.newFixedThreadPool(3);
    private DefaultHttpClient httpClient;
    private HttpGet request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_http);

            makeRequest();
            showResponse();
    }

    private void makeRequest(){
        StringBuilder requestString = new StringBuilder(WIZZAIR_URL);

        request = new HttpGet(requestString.toString());

        httpClient = new DefaultHttpClient();

        ClientConnectionManager mgr = httpClient.getConnectionManager();

        HttpParams params = httpClient.getParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        httpClient = new DefaultHttpClient(
                new ThreadSafeClientConnManager(params,
                        mgr.getSchemeRegistry()), params);

        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpResponse response = httpClient.execute(request);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    StringBuilder stringResponseBuilder = new StringBuilder();

                    int temp = 0;
                    String stringResponse="";
                    for (String line; (line = reader.readLine()) != null;) {
                        temp = line.indexOf("viewState");
                        if (temp>0) {
                            stringResponse  = line.substring(temp+32,temp+1000);
                            //stringResponseBuilder.append(line).append("\n");
                        }
                    }

                    viewState = stringResponse.substring(0,stringResponse.indexOf("\""));
                    temp = stringResponse.indexOf("pageToken");
                    temp = temp+33;
                    codeKey = stringResponse.substring(temp,temp+36);
                    temp = temp + 36 +23;
                    codeValue = stringResponse.substring(temp,temp+36);


                    System.out.println("Initial set of cookies:");


                    List<Cookie> cookies;
                    cookies = httpClient.getCookieStore().getCookies();
                    if (cookies.isEmpty()) {
                        System.out.println("None");
                    } else {
                        for (int i = 0; i < cookies.size(); i++) {
                            System.out.println("- " + cookies.get(i).toString());
                        }
                    }

                    sendPostRequest2();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void showResponse(){

    }

    private void sendPostRequest1() {
        final Executor requestExecutor = Executors.newFixedThreadPool(3);
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String server ="";
                String photo ="";
                String hash ="";
                try {
                    String uploadServer=WIZZAIR_URL;
                    // Create a new HttpClient and Post Header
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost=new HttpPost(uploadServer);
                        // Add your data
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
                        nameValuePairs.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$ButtonSubmit", "Поиск"));
                        nameValuePairs.add(new BasicNameValuePair(codeKey,codeValue));
                        nameValuePairs.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$DepartureDate", "30.04.2015"));
                        nameValuePairs.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$DestinationStation", "CGN"));
                        nameValuePairs.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$OriginStation", "IEV"));
                        nameValuePairs.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountADT", "1"));
                        nameValuePairs.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountCHD", "0"));
                        nameValuePairs.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountINFANT", "0"));
                        nameValuePairs.add(new BasicNameValuePair("__EVENTTARGET", "ControlGroupRibbonAnonHomeView_AvailabilitySearchInputRibbonAnonHomeView_ButtonSubmit"));
                        nameValuePairs.add(new BasicNameValuePair("__VIEWSTATE", viewState));
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        // Execute HTTP Post Request
                        //HttpResponse response = httpClient.execute(httpPost);

//                    MultipartEntity albumArtEntity = new MultipartEntity();
//                    albumArtEntity.addPart("photo", new FileBody(fileToUpload));
//                    httpPost.setEntity(albumArtEntity);

                    HttpResponse response=httpClient.execute(httpPost);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line; (line = reader.readLine()) != null;) {
                        builder.append(line).append("\n");
                    }
                    JSONObject photoObject = new JSONObject(builder.toString());
                    server =photoObject.get("server").toString();
                    photo = photoObject.get("photo").toString();
                    hash = photoObject.get("hash").toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void sendPostRequest2(){
        final Executor requestExecutor = Executors.newFixedThreadPool(3);
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String uploadServer="https://wizzair.com/ru-RU/Searching";
                    String uploadServer1= "/ru-RU/Searching";


                    HttpParams param = new BasicHttpParams();
                    param.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                    HttpClient mHttpClient = new DefaultHttpClient(param);
                    final HttpPost httpPostRequest = new HttpPost(WIZZAIR_URL);

                    httpPostRequest.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    httpPostRequest.setHeader("Origin","https://wizzair.com");
                    httpPostRequest.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.118 Safari/537.36");
                    httpPostRequest.setHeader("Content-Type","application/x-www-form-urlencoded");
                    httpPostRequest.setHeader("Referer","https://wizzair.com/ru-RU/Search");
                    httpPostRequest.setHeader("Accept-Encoding","gzip, deflate");
                    httpPostRequest.setHeader("Accept-Language","ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
                    httpPostRequest.setHeader("Cookie","HomePageSelector=Searching; cookiesAccepted=true; _hjIncludedInSample=0; _hjUserId=3268abab-3440-4cdf-b1ba-4e6965f83830; cookie_settings=necessary=1,functionality=1,performance=1,advertising=1");

                    List<NameValuePair> params = new ArrayList<NameValuePair>(11);
                    params.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$ButtonSubmit",  "Поиск"));
                    params.add(new BasicNameValuePair(codeKey, codeValue));
                    params.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$DepartureDate", "30.04.2015"));
                    params.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$DestinationStation", "CGN"));
                    params.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$OriginStation", "IEV"));
                    params.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountADT", "1"));
                    params.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountCHD", "0"));
                    params.add(new BasicNameValuePair("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountINFANT", "0"));
                    params.add(new BasicNameValuePair("__EVENTTARGET", "ControlGroupRibbonAnonHomeView_AvailabilitySearchInputRibbonAnonHomeView_ButtonSubmit"));
                    params.add(new BasicNameValuePair("__VIEWSTATE", viewState));
                    params.add(new BasicNameValuePair("cookiePolicyDismissed", "true"));

                    httpPostRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                    //final MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.STRICT);
//                    reqEntity.addPart("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$ButtonSubmit", new StringBody( "Поиск"));
//                    reqEntity.addPart(codeKey, new StringBody(codeValue));
//                    reqEntity.addPart("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$DepartureDate", new StringBody("30.04.2015"));
//                    reqEntity.addPart("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$DestinationStation", new StringBody("CGN"));
//                    reqEntity.addPart("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$OriginStation", new StringBody("IEV"));
//                    reqEntity.addPart("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountADT", new StringBody("1"));
//                    reqEntity.addPart("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountCHD", new StringBody("0"));
//                    reqEntity.addPart("ControlGroupRibbonAnonHomeView$AvailabilitySearchInputRibbonAnonHomeView$PaxCountINFANT", new StringBody("0"));
//                    reqEntity.addPart("__EVENTTARGET", new StringBody("ControlGroupRibbonAnonHomeView_AvailabilitySearchInputRibbonAnonHomeView_ButtonSubmit"));
//                    reqEntity.addPart("__VIEWSTATE", new StringBody(viewState));
//                    reqEntity.addPart("cookiePolicyDismissed",new StringBody("true"));
//                    httpPostRequest.setEntity(reqEntity);

                    final HttpResponse response = mHttpClient.execute(request);
//                    HttpEntity httpEntity = response.getEntity();
//                    InputStream is = httpEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    int temp;
                    String stringResponse;
                    for (String line; (line = reader.readLine()) != null;) {
                        builder.append(line).append("\n");
                        Log.i("WIZZ2",line);
                        temp = line.indexOf("price original");
                        if (temp>0) {
                            stringResponse  = line.substring(temp,temp+1000);
                            //stringResponseBuilder.append(line).append("\n");
                        }
                    }
//                    JSONObject photoObject = new JSONObject(builder.toString());
//                    server =photoObject.get("server").toString();
//                    photo = photoObject.get("photo").toString();
//                    hash = photoObject.get("hash").toString();
                    WebView webView = (WebView)findViewById(R.id.web_view);
                    webView.loadData(builder.toString(),"text/html", "utf8");



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
