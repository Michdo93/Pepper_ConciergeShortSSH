package de.lmoedl;

import com.aldebaran.qi.Application;

public class Pepper {
  public static void main(String[] args) {
    (new ConfigurationManager()).loadConfigFile();
    String robotUrl = "tcp://" + Constants.Config.ROBOT_URL + ":" + "9559";
    Application application = new Application(args, robotUrl);
    application.start();
    BasicBehaviour basicBehaviour = new BasicBehaviour(application);
    basicBehaviour.start();
    application.run();
  }
}
