package telvoterminal.telvo.com.terminal.service;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class HttpRequest {
    private HttpURLConnection urlConnection;
    private DataOutputStream outputStream;

    public String getRequest(String url, String request,String requestMethod,String token) throws Exception {
        if(requestMethod.equals("POST")){
            urlConnection = postHttpURLConnection(url,request,requestMethod,token);
        }else if(requestMethod.equals("GET")){
            urlConnection = getHttpURLConnection(url,request,requestMethod,token);
        }
        if(urlConnection!=null){
            if(urlConnection.getResponseCode()== 200){
               return getResponse(urlConnection.getInputStream());
            }else if(urlConnection.getResponseCode() >=400){
                String errorMessage = getResponse(urlConnection.getErrorStream());
                throw new Exception("Server returned HTTP" + " response code: " + urlConnection.getResponseCode() + " for URL: " + url.toString()+" Reason: "+errorMessage);
            }else{
                String errorMessage = getResponse(urlConnection.getErrorStream());
                throw new Exception("Server returned HTTP" + " response code: " + urlConnection.getResponseCode() + " for URL: " + url.toString()+" Reason: "+errorMessage);
            }
        }else{
            throw new RuntimeException("Stub!");
        }
    }

    private String getResponse(InputStream inputStream) throws IOException {
        InputStream in = new BufferedInputStream(inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    protected HttpURLConnection postHttpURLConnection(String url, String request,String requestMethod,String token) throws IOException {
            urlConnection = (HttpURLConnection) (new URL(url).openConnection());
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod(requestMethod);
            urlConnection.setRequestProperty("Authorization","Bearer "+token);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setReadTimeout(40000);
            urlConnection.setConnectTimeout(45000);
            urlConnection.connect();
            outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(request);
            outputStream.flush();
            outputStream.close();
            return urlConnection;
    }



    protected HttpURLConnection getHttpURLConnection(String url, String request,String requestMethod,String token) throws IOException {
        urlConnection = (HttpURLConnection) (new URL(url).openConnection());
        //urlConnection.setDoOutput(true);
      //  urlConnection.setRequestMethod(requestMethod);
        urlConnection.setRequestProperty("Authorization","Bearer "+token);
        //urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setReadTimeout(40000);
        urlConnection.setConnectTimeout(45000);
        ///urlConnection.connect();
//        outputStream = new DataOutputStream(urlConnection.getOutputStream());
//        outputStream.writeBytes(request);
//        outputStream.flush();
//        outputStream.close();
        return urlConnection;
    }


}
