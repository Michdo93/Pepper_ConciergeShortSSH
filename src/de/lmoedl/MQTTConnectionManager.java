package de.lmoedl;

import de.lmoedl.interfaces.MQTTSubscriberCallbackInterface;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTConnectionManager implements MqttCallbackExtended {
  private String topicPublishBase = "/messages/commands/";
  
  private String topicSubscribeBase = "/messages/states/";
  
  private int qos = 2;
  
  private String broker = "tcp://192.168.0.5:1883";
  
  private String clientId = "Pepper";
  
  private MemoryPersistence persistence = new MemoryPersistence();
  
  private MqttClient client;
  
  private MQTTSubscriberCallbackInterface delegate;
  
  public MQTTConnectionManager(MQTTSubscriberCallbackInterface delegate) {
    this.delegate = delegate;
    try {
      this.client = new MqttClient(this.broker, this.clientId, (MqttClientPersistence)this.persistence);
      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      System.out.println("Connecting to broker: " + this.broker);
      this.client.setCallback((MqttCallback)this);
      this.client.connect(connOpts);
      System.out.println("Connected");
    } catch (MqttException ex) {
      Logger.getLogger(MQTTConnectionManager.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }
  
  public void publishToItem(String item, String payload) {
    try {
      MqttMessage message = new MqttMessage(payload.getBytes());
      message.setQos(this.qos);
      this.client.publish(this.topicPublishBase + item, message);
      System.out.println("Message published");
    } catch (MqttException me) {
      System.out.println("reason " + me.getReasonCode());
      System.out.println("msg " + me.getMessage());
      System.out.println("loc " + me.getLocalizedMessage());
      System.out.println("cause " + me.getCause());
      System.out.println("excep " + me);
      me.printStackTrace();
    } 
  }
  
  public void subscribeToItem(String item) {
    try {
      this.client.subscribe(this.topicSubscribeBase + item, this.qos);
    } catch (MqttException ex) {
      Logger.getLogger(MQTTConnectionManager.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }
  
  public void subscribeToItems(List<String> items) {
    try {
      for (String item : items)
        this.client.subscribe(this.topicSubscribeBase + item, this.qos); 
    } catch (MqttException ex) {
      Logger.getLogger(MQTTConnectionManager.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }
  
  public void unsubscribeOfItem(String item) {
    try {
      this.client.unsubscribe(this.topicSubscribeBase + item);
    } catch (MqttException ex) {
      Logger.getLogger(MQTTConnectionManager.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }
  
  public void unsubscribeOfItems(List<String> items) {
    try {
      for (String item : items)
        this.client.unsubscribe(this.topicSubscribeBase + item); 
    } catch (MqttException ex) {
      Logger.getLogger(MQTTConnectionManager.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }
  
  public void disconnect() {
    try {
      this.client.disconnect();
      System.out.println("Disconnected");
    } catch (MqttException ex) {
      Logger.getLogger(MQTTConnectionManager.class.getName()).log(Level.SEVERE, (String)null, (Throwable)ex);
    } 
  }
  
  public void connectComplete(boolean bln, String string) {
    System.out.println("Connection complete");
  }
  
  public void connectionLost(Throwable thrwbl) {}
  
  public void messageArrived(String string, MqttMessage mm) throws Exception {
    System.out.println("Message Arrived: " + string + " payload: " + new String(mm.getPayload()));
    this.delegate.onSubscription(string, new String(mm.getPayload()));
  }
  
  public void deliveryComplete(IMqttDeliveryToken imdt) {}
}
