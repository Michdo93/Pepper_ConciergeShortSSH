/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lmoedl;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALAnimationPlayer;
import com.aldebaran.qi.helper.proxies.ALBehaviorManager;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.vmichalak.sonoscontroller.SonosDevice;
import com.vmichalak.sonoscontroller.exception.SonosControllerException;
import de.lmoedl.interfaces.MQTTSubscriberCallbackInterface;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
/**
 *
 * @author lothar
 */
public class BasicBehaviour implements MQTTSubscriberCallbackInterface {

    private Session session;
    private Application application;
    //for testing true, else false

    private ALMemory memory;
    private ALMotion motion;
    private ALTextToSpeech textToSpeech;
    private ALAnimatedSpeech animatedSpeech;
    private ALBehaviorManager behaviorManager;
    private ALAnimationPlayer animationPlayer;

    private MQTTConnectionManager mQTTConnectionManager;
    private SonosDevice sonos;

    private Logger logger;
    private FileHandler fh;
    
    private BufferedReader reader;
    String line;
    
    public BasicBehaviour(Application application) {
        if (Constants.Config.DEBUG) {
            logger = Logger.getLogger(BasicBehaviour.class.getName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
            try {
                fh = new FileHandler("pepper_" + sdf.format(new Date()) + ".log");
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
            } catch (IOException | SecurityException ex) {
                log(Level.SEVERE, ex);
            }
        }

        this.session = application.session();
        this.application = application;

        try {
            memory = new ALMemory(session);
            motion = new ALMotion(session);
            textToSpeech = new ALTextToSpeech(session);
            animatedSpeech = new ALAnimatedSpeech(session);
            animationPlayer = new ALAnimationPlayer(session);
            behaviorManager = new ALBehaviorManager(session);
            mQTTConnectionManager = new MQTTConnectionManager(this);
            sonos = new SonosDevice(Constants.Config.SONOS_URL); 
            reader = new BufferedReader(new FileReader("Text.txt"));
            config();
        } catch (Exception ex) {
            log(Level.SEVERE, ex);
        }
    }

    public void start() {
        try {
            presentation();
        } catch (Exception ex) {
            Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void config() throws CallError, InterruptedException, Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                disconnectAll();
                System.out.println("shutdownhook called");
            }
        }));

        sonos.setMute(false);
        textToSpeech.setLanguage(Constants.LANGUAGE);        
        motion.setExternalCollisionProtectionEnabled("All", true);
        motion.setWalkArmsEnabled(Boolean.TRUE, Boolean.TRUE);

        motion.setOrthogonalSecurityDistance(0.15f);
        motion.wakeUp();      

    }


    public void presentation() throws Exception {
        log("Presentation");        
        putHeadUp();        
           
        welcome();
        music(); 
        alexa();             
        shutter();
        runGamingScene();
        kitchen();
        farewell();

        System.exit(0);
    }
    
    public void welcome() {
        log("Welcome"); 
        try {
            line = readLine();
            animatedSpeech.say(line);
            line = readLine();
            animatedSpeech.say(line);
            line = readLine();
            animatedSpeech.say(line);
            line = readLine();
            animatedSpeech.say(line);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Gestures/Excited_1) Hallo zusammen! Ich freue mich, Sie hier im Smart Home Labor der Fakultät Informatik an der Hochschule Furtwangen begrüßen zu dürfen. ^wait(animations/Stand/Gestures/Excited_1)");
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_1) Wir haben einige Demonstrationen vorbereitet, die Ihnen einen Eindruck von unserer Arbeit in diesem Labor vermitteln. Aber zunächst darf ich Ihnen einige Informationen dazu geben. ^wait(animations/Stand/Gestures/Explain_1)");
            //animatedSpeech.say("^start(animations/Stand/Waiting/ShowSky_10)Das Labor soll der Forschung und der Lehre in einigen Teilbereichen der angewandten Informatik dienen.^wait(animations/Stand/Waiting/ShowSky_10)");
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_3)^start(animations/Stand/Gestures/Explain_4)Das sind alles spannende Themen, die mit der Digitalisierung, der Vernetzung, der Sensorik und Automatisierung zu tun haben. Alles das ist sehr wichtig für unsere Absolventinnen und Absolventen, wenn sie später im Umfeld der Industrie 4 Punkt 0 arbeiten wollen.^wait(animations/Stand/Gestures/Explain_3)^wait(animations/Stand/Gestures/Explain_4)");
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_5)^start(animations/Stand/Gestures/Explain_6)Das Labor wurde eigenständig von Mitarbeitenden unserer Fakultät geplant. Mit der Ausführung waren lokale Handwerksbetriebe betraut worden. Nun erfolgt Schritt für Schritt die Inbetriebnahme des Labors: Jedes einzelne Gerät muss installiert und in die Netzwerk-Architektur integriert werden. Dann erst können innovative Anwendungsszenarien entwickelt werden.^wait(animations/Stand/Gestures/Explain_5)^wait(animations/Stand/Gestures/Explain_6)");            
                       
        } catch (CallError | InterruptedException ex) {
            Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void music() {
        log("Music"); 
        try {
            line = readLine();
            animatedSpeech.say(line);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Emotions/Positive/Happy_4) Netterweise hat eine Projektgruppe mir vor kurzem beigebracht, wie man die Geräte hier bedient. ^wait(animations/Stand/Emotions/Positive/Happy_4)");
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_7) Seitdem ist mir nicht mehr so furchtbar langweilig, weil ich zum Beispiel Musik hören kann. ^wait(animations/Stand/Gestures/Explain_7)");
         
            sonos.playUri(Constants.Config.MUSIC_URL, null);  
            sonos.setMute(false);
            sonos.setVolume(50);
            sonos.setBass(3);  
            Thread.sleep(5000);
            behaviorManager.runBehavior("Headbang");
            behaviorManager.runBehavior("Headbang");
            Thread.sleep(2000);
            sonos.setMute(true);
            Thread.sleep(2000);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Emotions/Negative/Exhausted_1) Puuh, das war jetzt aber ganz schön anstrengend. ^wait(animations/Stand/Emotions/Negative/Exhausted_1)");
            putHeadUp();
             
        } catch (SonosControllerException | IOException | CallError | InterruptedException ex) {
            Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);        
        }
    }
    
    public void alexa() {
        log("Alexa"); 
        try {
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_5) Zur Entspannung unterhalte ich mich erstmal ein bisschen mit einer guten Freundin.^wait(animations/Stand/Gestures/Explain_5) ");
            Thread.sleep(1000);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("Ruhe bitte!");
            Thread.sleep(2000);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("Alexa ^start(animations/Stand/Waiting/Think_1)");   
            Thread.sleep(500);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("wie ist das Wetter?");
            animationPlayer.run("animations/Stand/BodyTalk/Listening/Listening_1");
            Thread.sleep(2000);
            animationPlayer.run("animations/Stand/Gestures/Yes_1");
            Thread.sleep(2000);
            animationPlayer.run("animations/Stand/BodyTalk/Listening/Listening_7");
            Thread.sleep(3000);    
            animationPlayer.run("animations/Stand/Gestures/Yes_2");
            Thread.sleep(2000); 
            animationPlayer.run("animations/Stand/BodyTalk/Listening/Listening_4");
            Thread.sleep(1000);
            
            //String temp = connectionManager.getRequestString("http://192.168.0.11:8080/rest/items/NAInnenraumsensorBad_Temperature/state");
            //String temp = connectionManager.getRequestString("iIoT_Netatmo_WetterInnenmodul_Temperatur");
            //String temp_round = temp.substring(0, Math.min(temp.length(), 2));            
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_3) Danke für die Infos. Hier drinnen ist es mit" + temp_round + "Grad schön angenehm. ^wait(animations/Stand/Gestures/Explain_3)");
            line = readLine();
            animatedSpeech.say(line); 
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_3) Vielen Dank für die Infos! Du bist wie immer topp informiert!");
            putHeadUp();
            
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Emotions/Positive/Happy_4) Ja, so eine Freundin ist schon toll!^wait(animations/Stand/Emotions/Positive/Happy_4)"); 
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Gestures/Thinking_4) Aber sie hört immer mit. Manchmal mache ich mir dann Sorgen um meine Privats-Fähre.^wait(animations/Stand/Gestures/Thinking_4)");
            Thread.sleep(1000);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Emotions/Neutral/Confused_1) Ach, da fällt mir ein – ich bin ja ein Roboter, da kann mir das ja egal sein!^wait(animations/Stand/Emotions/Neutral/Confused_1)");
            putHeadUp();    
        } catch (CallError | InterruptedException ex) {
            Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);        
        }
    }
    
    public void shutter() {
        log("Shutter"); 
        try {
           /*
           line = readLine();
           animatedSpeech.say(line); 
           //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_7)Natürlich kann ich nicht nur die Musikboxen, sondern auch andere Geräte bedienen.^start(animations/Stand/Gestures/Explain_8)");
           motion.moveTo(0f, 0f, -0.785f); 
           
           behaviorManager.runBehavior("WTF");
           line = readLine();
           animatedSpeech.say(line);
           //animatedSpeech.say("Rollladen: Bitte halb herunterfahren.");       
           
           mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Shutter.MAIN_SHUTTER_3, "DOWN"); 
           Thread.sleep(12000);
           mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Shutter.MAIN_SHUTTER_3, "STOP"); 
           
           
           motion.moveTo(0f, 0f, 0.785f); 
           line = readLine();
           animatedSpeech.say(line);
           //animatedSpeech.say("^start(animations/Stand/Gestures/Excited_1)Cool, oder? ^wait(animations/Stand/Gestures/Excited_1)");
           */
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_8)Ja, das ist schon alles sehr beeindruckend! Aber besonders stolz bin ich auf unsere Studierenden.^wait(animations/Stand/Gestures/Explain_8)");
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_9)Sie leisten bereits jetzt wichtige Beiträge für unser Labor, wenn sie in Projekten, in Abschlussarbeiten oder sogar als Hie-Wies schwierige Aufgabenstellungen bearbeiten und manchmal auch richtig schöne Lösungen vorweisen können.^wait(animations/Stand/Gestures/Explain_9)");
        } catch (CallError | InterruptedException ex) {
            Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);        
        }
    }
    
    private void runGamingScene() throws IOException, SonosControllerException, InterruptedException, CallError {
        log("Car Race"); 
        line = readLine();
        animatedSpeech.say(line);
        //animatedSpeech.say("^start(Stand/Gestures/Excited_1) Eine Anwendung gefällt mir besonders gut, nämlich unser Autofahrer-Training. Hierbei kann ich immer prima entspannen. Ich zeige es Ihnen mal kurz ^wait(Stand/Gestures/Excited_1)");
        
        //ProcessBuilder pb = new ProcessBuilder(Constants.Config.VLC_PATH, "src/resources/CUTRace_v4.mp4");
        //Process start = pb.start();
        
        Thread thread = new Thread(){
            @Override
            public void run(){
                System.out.println("Thread Running");

                String user = Constants.Config.SSH_USER;
                String password = Constants.Config.SSH_PASSWORD;
                String host = Constants.Config.SSH_HOST;
                int port = Constants.Config.SSH_PORT;
                String command = String.valueOf(Constants.Config.VLC_PATH) + " " + Constants.Config.MOVIE_PATH;
                StringBuilder outputBuffer = new StringBuilder();
                try {
                    JSch jsch = new JSch();
                    com.jcraft.jsch.Session sshSession = jsch.getSession(user, host, port);
                    sshSession.setPassword(password);
                    sshSession.setConfig("StrictHostKeyChecking", "no");
                    System.out.println("Establishing Connection...");
                    sshSession.connect();
                    System.out.println("Connection established.");
                    System.out.println("Crating EXEC Channel.");
                    Channel channel = sshSession.openChannel("exec");
                    ((ChannelExec)channel).setCommand(command);
                    InputStream commandOutput = channel.getInputStream();
                    channel.connect();
                    int readByte = commandOutput.read();
                while (readByte != -1) {
                    outputBuffer.append((char)readByte);
                    readByte = commandOutput.read();
                }
                
                channel.disconnect();
                
                } catch (JSchException|IOException e) {
                    e.printStackTrace();
                }
                
            }
        };
        thread.start();

        Thread.sleep(1000);
        
        animationPlayer.run("animations/Stand/Waiting/DriveCar_1");
        animationPlayer.run("animations/Stand/Waiting/DriveCar_1");
        
        //start.destroy();
        thread.stop();
        
        line = readLine();
        animatedSpeech.say(line);
        //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_2) Meine Güte, ganz schön aufregend – aber gut, dass das alles nur virtu-ell ist und nicht echt! ^wait(animations/Stand/Gestures/Explain_2)");
        line = readLine();
        animatedSpeech.say(line);
        //animatedSpeech.say("^start(animations/Stand/Emotions/Positive/Happy_4)  Ach - ich liebe die Informatik – alles nur Software, da geht so schnell nichts kaputt! ^wait(animations/Stand/Emotions/Positive/Happy_4)");
              
    }
        
    
    public void kitchen() {
        log("Kitchen"); 
        try {
            putHeadUp();
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_10) Gleich zeige ich Ihnen noch die Küche. ^wait(animations/Stand/Gestures/Explain_10)");
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_7) Bitte lassen Sie mir kurz Zeit hinzufahren. Ich bin ja kein Hochgeschwindigkeits-Roboter ^wait(animations/Stand/Gestures/Explain_7)");
            //mQTTConnectionManager.publishToItem("SomfyHauptrollo_3_SomfyDeviceControl", "UP"); 
            motion.moveTo(0f, 0f, 0.785f); 
            Thread.sleep(500);
            motion.moveTo(3.3f, 0f, 0f);
            Thread.sleep(500);
            motion.moveTo(0f, 0f, -1.5709f);
            Thread.sleep(500);
            motion.moveTo(0.5f, 0f, 0f);
            Thread.sleep(500);
            putHeadUp();
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("^start(animations/Stand/Gestures/Explain_9) Hier sehen Sie einen wichtigen Raum für die ganzen Menschen hier im Labor - die Küche. ^wait(animations/Stand/Gestures/Explain_9)");
            line = readLine();
            animatedSpeech.say(line);
            //Licht anschalten
            mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Lights.Kitchen.HUE_1, "ON"); 
            mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Lights.Kitchen.HUE_2, "ON"); 
            mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Lights.Kitchen.HUE_3, "ON"); 
            mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Lights.Kitchen.HUE_4, "ON"); 
            Thread.sleep(2000);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("Während ich mich an meiner Ladestation ausruhe, ^start(animations/Stand/Waiting/Drink_1) trinken die Studierenden hier literweise Kaffee! ^wait(animations/Stand/Waiting/Drink_1)"); 
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("Die Geräte sind alle im Netzwerk verfügbar, und wir lernen gerade, sie vernünftig zu steuern."); 
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("Sie können sich vorstellen: dabei muss natürlich die Sicherheit ganz im Vordergrund stehen.");    
        } catch (CallError | InterruptedException ex) {
            Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);        
        }        
    }
    
    public void farewell() {
        log("Farewell"); 
        try {
            Thread.sleep(2000);
            line = readLine();
            animatedSpeech.say(line);
            //animatedSpeech.say("Wir haben hier noch weitere Räume, aber die sollen Ihnen meine menschlichen Kolleginnen und Kollegen zeigen. Ich mache jetzt erst einmal Pause.");
            //motion.moveTo(0f, 0f, 0.78f);
            //Thread.sleep(500);
            motion.moveTo(-1.0f, 0f, 0f);
            mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Lights.Kitchen.HUE_1, "OFF"); 
            mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Lights.Kitchen.HUE_2, "OFF"); 
            mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Lights.Kitchen.HUE_3, "OFF"); 
            mQTTConnectionManager.publishToItem(Constants.MQTTTopics.Lights.Kitchen.HUE_4, "OFF");
            motion.rest();
        } catch (CallError | InterruptedException ex) {
            Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);        
        }  
    }
    
    private void putHeadUp() {
        try {
            motion.angleInterpolationWithSpeed("Head", new ArrayList<>(Arrays.asList(0.0f, -0.3f)), 0.3f);
        } catch (CallError | InterruptedException ex) {
            Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void log(String message) {
        if (Constants.Config.DEBUG) {
            logger.info(message);
        }
    }

    private void log(Level loglevel, Exception ex) {
        if (Constants.Config.DEBUG) {
            logger.log(loglevel, null, ex);
        }
    }    

    @Override
    public void onSubscription(String item, String value) {
        String itemDescription = item.split("\\/")[3];

        System.out.println("onSubscription: " + itemDescription);

        switch (itemDescription) {
            case Constants.MQTTTopics.Window.WINDOW_1:
            case Constants.MQTTTopics.Window.WINDOW_2:
            case Constants.MQTTTopics.Window.WINDOW_3:
            case Constants.MQTTTopics.Window.WINDOW_4:
            case Constants.MQTTTopics.Window.WINDOW_5:
            case Constants.MQTTTopics.Window.WINDOW_6:
            case Constants.MQTTTopics.Window.WINDOW_7:
            case Constants.MQTTTopics.Window.WINDOW_8:
            case Constants.MQTTTopics.Window.WINDOW_9:

                try {
                    if (value.equals("OPEN")) {
                        System.out.println("onSubscription: " + itemDescription + " " + value);
                        memory.raiseEvent("WindowOpend", itemDescription);
                    } else if (value.equals("CLOSED")) {
                        System.out.println("onSubscription: " + itemDescription + " " + value);
                        memory.raiseEvent("WindowClosed", itemDescription);
                    }
                } catch (CallError | InterruptedException ex) {
                    Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            default:

                try {
                    memory.insertData(item, value);
                } catch (CallError | InterruptedException ex) {
                    Logger.getLogger(BasicBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                }

        }
    }    
    
    private void disconnectAll(){
        mQTTConnectionManager.disconnect();
        application.stop();
    }
    
    private String readLine() {
        String newline = "";
        try {
            newline = reader.readLine();
            while (newline.substring(0, 2).equals("//"))
                newline = reader.readLine();
        } catch (IOException ex) {
            log(Level.SEVERE, ex);
        }
        return newline;
    }

}

