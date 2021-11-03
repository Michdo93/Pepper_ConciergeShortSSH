package de.lmoedl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationManager {
  public void loadConfigFile(String fileName) {
    String json = readConfigFile(fileName);
    if (!json.equals(""))
      parseJson(json); 
  }
  
  public void loadConfigFile() {
    loadConfigFile("./concierge.conf");
  }
  
  private String readConfigFile(String fileName) {
    String line = "";
    String result = "";
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      while ((line = br.readLine()) != null)
        result = result + line; 
    } catch (IOException ex) {
      Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    return result;
  }
  
  private void parseJson(String json) {
    try {
      JSONObject object = new JSONObject(json);
      String pepperIP = object.getString("pepperIP");
      boolean headless = object.getBoolean("headless");
      boolean debug = object.getBoolean("debug");
      String movieURL = object.getString("moviePath");
      String sonosIP = object.getString("sonosIP");
      String ratingUrl = object.getString("ratingUrl");
      String vlcPath = object.getString("vlcPath");
      String sshUser = object.getString("sshUser");
      String sshPassword = object.getString("sshPassword");
      String sshHost = object.getString("sshHost");
      String sshPort = object.getString("sshPort");
      
      Constants.Config.ROBOT_URL = pepperIP;
      Constants.Config.HEADLESS = headless;
      Constants.Config.DEBUG = debug;
      Constants.Config.MOVIE_PATH = movieURL;
      Constants.Config.SONOS_URL = sonosIP;
      Constants.Config.RATING_URL = ratingUrl;
      Constants.Config.VLC_PATH = vlcPath;
      Constants.Config.SSH_USER = sshUser;
      Constants.Config.SSH_PASSWORD = sshPassword;
      Constants.Config.SSH_HOST = sshHost;
      Constants.Config.SSH_PORT = Integer.parseInt(sshPort);
      
    } catch (JSONException ex) {
      Logger.getLogger(ConfigurationManager.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }
}
