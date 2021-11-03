package de.lmoedl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
  public static final String APP_NAME = "Pepper_Concierge";
  
  public static final String LANGUAGE = "German";
  
  public static class Config {
    public static String ROBOT_URL = "192.168.0.41";
    public static final String ROBOT_PORT = "9559";
    public static boolean HEADLESS = true;
    public static boolean DEBUG = true;
    public static String MOVIE_PATH = "/home/pi/CUTRace_v4.mp4";
    public static String SONOS_URL = "192.168.0.30";
    public static String RATING_URL = "http://192.168.0.65/";
    public static String MUSIC_URL = "https://archive.org/download/cd_smells-like-teen-spirit_nirvana/disc1/01. Nirvana - Smells Like Teen Spirit (Edit)_sample.mp3";
    public static String VLC_PATH = "/usr/bin/vlc -f";
    public static String SSH_USER = "pi";
    public static String SSH_PASSWORD = "raspberry";
    public static String SSH_HOST = "192.168.0.231";
    public static int SSH_PORT = 22;
  }
  
  public static class Steps {
    public static final String STEP_STARUP = "STEP_STARTUP";
    public static final String STEP_MOVE_AROUND = "STEP_MOVE_AROUND";
    public static final String STEP_END = "STEP_END";
    public static final String STEP_FACERECOGNITION = "STEP_FACERECOGNITION";
    public static final String STEP_SOUNDLOCALIZATION = "STEP_SOUNDLOCALIZATION";
    public static final String STEP_DIALOG = "STEP_DIALOG";
    public static final String STEP_MQTT = "STEP_MQTT";
    public static final String STEP_TRAJECTORY = "STEP_TRAJECTORY";
  }
  
  public static class BasicAwareness {
    public static class Stimulus {
      public static final String SOUND = "Sound";
      public static final String MOVEMENT = "Movement";
      public static final String PEOPLE = "People";
      public static final String TOUCH = "Touch";
    }
    
    public static class EngagementMode {
      public static final String UNENGAGED = "Unengaged";
      public static final String FULLY_ENGAGED = "FullyEngaged";
      public static final String SEMI_ENGAGED = "SemiEngaged";
    }
    
    public static class TrackingModes {
      public static final String HEAD = "Head";
      public static final String BODY_ROTATION = "BodyRotation";
      public static final String WHOLE_BODY = "WholeBody";
      public static final String MOVE_CONTEXTUALLY = "MoveContextually";
    }
  }
  
  public static class MQTTTopics {
    public static class Lights {
      public static class TVRoom {
        public static final String HUE_1 = "iMultimedia_Hue_Lampe1_Schalter";
        public static final String HUE_2 = "iMultimedia_Hue_Lampe2_Schalter";
        public static final String HUE_3 = "iMultimedia_Hue_Lampe3_Schalter";
        public static final String HUE_4 = "iMultimedia_Hue_Lampe4_Schalter";
        public static final String HUE_5 = "iMultimedia_Hue_Lampe5_Schalter";
        public static final String HUE_6 = "iMultimedia_Hue_Lampe6_Schalter";
        public static final String HUE_1_COLOR = "iMultimedia_Hue_Lampe1_Farbe";
        public static final String HUE_2_COLOR = "iMultimedia_Hue_Lampe2_Farbe";
        public static final String HUE_3_COLOR = "iMultimedia_Hue_Lampe3_Farbe";
        public static final String HUE_4_COLOR = "iMultimedia_Hue_Lampe4_Farbe";
        public static final String HUE_5_COLOR = "iMultimedia_Hue_Lampe5_Farbe";
        public static final String HUE_6_COLOR = "iMultimedia_Hue_Lampe6_Farbe";
      }
      
      public static class Bathroom {
        public static final String HUE_1 = "iBad_Hue_Lampe1_Schalter";
        public static final String HUE_2 = "iBad_Hue_Lampe2_Schalter";
        public static final String HUE_3 = "iBad_Hue_Lampe3_Schalter";
        public static final String HUE_4 = "iBad_Hue_Lampe4_Schalter";
        public static final String HUE_5 = "iBad_Hue_Lampe5_Schalter";
        public static final String HUE_6 = "iBad_Hue_Lampe6_Schalter";
        public static final String HUE_1_COLOR = "iBad_Hue_Lampe1_Farbe";
        public static final String HUE_2_COLOR = "iBad_Hue_Lampe2_Farbe";
        public static final String HUE_3_COLOR = "iBad_Hue_Lampe3_Farbe";
        public static final String HUE_4_COLOR = "iBad_Hue_Lampe4_Farbe";
        public static final String HUE_5_COLOR = "iBad_Hue_Lampe5_Farbe";
        public static final String HUE_6_COLOR = "iBad_Hue_Lampe6_Farbe";
      }
      
      public static class Kitchen {
        public static final String HUE_1 = "iKueche_Hue_Lampe1_Schalter";
        public static final String HUE_2 = "iKueche_Hue_Lampe2_Schalter";
        public static final String HUE_3 = "iKueche_Hue_Lampe3_Schalter";
        public static final String HUE_4 = "iKueche_Hue_Lampe4_Schalter";
        public static final String HUE_1_COLOR = "iKueche_Hue_Lampe1_Farbe";
        public static final String HUE_2_COLOR = "iKueche_Hue_Lampe2_Farbe";
        public static final String HUE_3_COLOR = "iKueche_Hue_Lampe3_Farbe";
        public static final String HUE_4_COLOR = "iKueche_Hue_Lampe4_Farbe";
      }
      
      public static class Doors {
        public static final String TVROOM_DOOR_1 = "iMultimedia_Homematic_Schaltglas1_Schalten";
        public static final String TVROOM_DOOR_2 = "iMultimedia_Homematic_Schaltglas2_Schalten";
      }
    }
    
    public static class Window {
      public static final String WINDOW_1 = "iMultimedia_Homematic_Fenste1_Kommunikation1";
      public static final String WINDOW_2 = "iMultimedia_Homematic_Fenster2_Kommunikation1";
      public static final String WINDOW_3 = "iMultimedia_Homematic_Fenster3_Kommunikation1";
      public static final String WINDOW_4 = "iKonferenz_Homematic_Fenster1_Kommunikation1";
      public static final String WINDOW_5 = "iKonferenz_Homematic_Fenster2_Kommunikation1";
      public static final String WINDOW_6 = "iKonferenz_Homematic_Fenster3_Kommunikation1";
      public static final String WINDOW_7 = "iKonferenz_Homematic_Fenster4_Kommunikation1";
      public static final String WINDOW_8 = "iKonferenz_Homematic_Fenster5_Kommunikation1";
      public static final String WINDOW_9 = "iKonferenz_Homematic_Fenster6_Kommunikation1";
      public static final List<String> WINDOWS = new ArrayList<>(Arrays.asList(new String[] { "iMultimedia_Homematic_Fenste1_Kommunikation1", "iMultimedia_Homematic_Fenster2_Kommunikation1", "iMultimedia_Homematic_Fenster3_Kommunikation1", 
              "iKonferenz_Homematic_Fenster1_Kommunikation1", "iKonferenz_Homematic_Fenster2_Kommunikation1", "iKonferenz_Homematic_Fenster3_Kommunikation1", "iKonferenz_Homematic_Fenster4_Kommunikation1", "iKonferenz_Homematic_Fenster5_Kommunikation1", "iKonferenz_Homematic_Fenster6_Kommunikation1" }));
    }
    
    public static class Shutter {
      public static final String MAIN_SHUTTER_1 = "iMultimedia_Somfy_Rollladen_Steuerung";
      public static final String MAIN_SHUTTER_2 = "iKonferenz_Somfy_Rollladen1_Steuerung";
      public static final String MAIN_SHUTTER_3 = "iKonferenz_Somfy_Rollladen2_Steuerung";
      public static final String INDOOR_SHUTTER_1 = "iBad_Jalousie_Steuerungvorne";
      public static final String INDOOR_SHUTTER_2 = "iKueche_Jalousie_Steuerungvorne";
      public static final String INDOOR_SHUTTER_3 = "iBad_Jalousie_Steuerunghinten";
      public static final String INDOOR_SHUTTER_4 = "iKueche_Jalousie_Steuerunghinten";
    }
    
    public static class Kitchen {
      public static final String RANGE_HOOD_LIGHT = "iKueche_Miele_Abzugshaube_Licht";
      public static final String RANGE_HOOD_STATE = "iKueche_Miele_Abzugshaube_Status";
      public static final String RANGE_HOOD_STOP = "iKueche_Miele_Abzugshaube_Stop";
      public static final String RANGE_HOOD_STEP = "iKueche_Miele_Abzugshaube_Ventilatorstufe";
      public static final String REFRIGERATOR_COOLING_STATE = "iKueche_Miele_Kuehlschrank_Kuehlstatus";
      public static final String REFRIGERATOR_TEMPERATURE = "iKueche_Miele_Kuehlschrank_AktGefrierTemperatur";
      public static final String REFRIGERATOR_STATE = "iKueche_Miele_Kuehlschrank_Status";
      public static final String REFRIGERATOR_SUPERCOOL = "iKueche_Miele_Kuehlschrank_ExtraKaelte";
      public static final String REFRIGERATOR_SUPERFREEZE = "iKueche_Miele_Kuehlschrank_ExtraFrost";
      public static final String REFRIGERATOR_SWITCH = "iKueche_Miele_Kuehlschrank_Start";
    }
  }
}
