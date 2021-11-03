package de.lmoedl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionManager {
  private String basicUrl = "http://192.168.0.11:8080/rest/";
  
  public ConnectionManager(String basicUrl) {
    this.basicUrl = basicUrl;
  }
  
  public ConnectionManager() {}
  
  public void openHabPostRequest(String path, String data) {
    try {
      URL url = new URL(this.basicUrl + path);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestProperty("Content-Type", "text/plain");
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.connect();
      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(data.getBytes());
      InputStream inputStream = connection.getInputStream();
      System.out.println(connection.getResponseCode());
      inputStream.close();
      outputStream.close();
      connection.disconnect();
    } catch (MalformedURLException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (Exception ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }
  
  public void getRequest(String urlPath) {
    try {
      URL url = new URL(urlPath);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestProperty("Content-Type", "text/plain");
      connection.setRequestMethod("GET");
      connection.connect();
      InputStream inputStream = connection.getInputStream();
      System.out.println(connection.getResponseCode());
    } catch (IOException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
  }
  
  public String getRequestString(String urlPath) {
    try {
      String inputLine;
      URL url = new URL(urlPath);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestProperty("Content-Type", "text/plain");
      connection.setRequestMethod("GET");
      connection.connect();
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        StringBuilder content = new StringBuilder();
        inputLine = in.readLine();
      } 
      connection.disconnect();
      return inputLine;
    } catch (IOException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, (String)null, ex);
      return "";
    } 
  }
  
  public String[] openHabGetRequest(String path, String key) {
    try {
      URL url = new URL(this.basicUrl + "items/" + path);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestProperty("Content-Type", "text/plain");
      connection.setRequestMethod("GET");
      connection.connect();
      InputStream inputStream = connection.getInputStream();
      System.out.println(connection.getResponseCode());
      BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
      String result = "";
      String inputLine;
      while ((inputLine = in.readLine()) != null)
        result = result + inputLine; 
      inputStream.close();
      connection.disconnect();
      return parseRequest(result, key, path);
    } catch (MalformedURLException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return null;
  }
  
  private String[] parseRequest(String json, String key, String path) {
    try {
      JSONObject object = new JSONObject(json);
      String value = object.getString(key);
      return new String[] { path, value };
    } catch (JSONException ex) {
      Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
      return null;
    } 
  }
  
  public String getBasicUrl() {
    return this.basicUrl;
  }
  
  public void setBasicUrl(String basicUrl) {
    this.basicUrl = basicUrl;
  }
}
