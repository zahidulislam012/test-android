package telvoterminal.telvo.com.terminal.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import telvoterminal.telvo.com.terminal.model.kyc.PersonalInfo;

/**
 * Created by Invariant on 11/6/17.
 */

public class HttpMultipartRequest {
    public String multipartRequest(String urlTo, PersonalInfo personalInfo, String token, String[] filepath, String[] filefield, String[] fileMimeType)  {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


        File file;
        FileInputStream fileInputStream;


        Map<String, String> params = new HashMap<String, String>(2);
        params.put("userId", personalInfo.getUserId());
        params.put("fatherName", personalInfo.getFatherName());
        params.put("motherName", personalInfo.getMotherName());
        params.put("spouseName", personalInfo.getSpouseName());
        params.put("nationality", personalInfo.getNationality());
        params.put("dob", personalInfo.getDob());
        params.put("gender", personalInfo.getGender());
        params.put("nid", personalInfo.getNid());
        params.put("nidType", personalInfo.getNidType());

        try {


            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization","Bearer "+token);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            outputStream = new DataOutputStream(connection.getOutputStream());

            for (int i=0 ; i<filepath.length ; i++){
                if(filepath[i]!=null){
                    String[] q = filepath[i].split("/");
                    int idx = q.length - 1;


                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield[i] + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
                    outputStream.writeBytes("Content-Type: " + fileMimeType[1] + lineEnd);
                    outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

                    outputStream.writeBytes(lineEnd);

                    file = new File(filepath[i]);
                    fileInputStream = new FileInputStream(file);

                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        outputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    outputStream.writeBytes(lineEnd);
                    fileInputStream.close();
                }

            }





            // Upload POST Data
            Iterator<String> keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = params.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            if (200 != connection.getResponseCode()) {
                throw new Exception("Server returned HTTP" + " response code: " + connection.getResponseCode() + " for URL: " + url.toString());

            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);


            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
