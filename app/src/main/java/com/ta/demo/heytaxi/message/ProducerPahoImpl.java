package com.ta.demo.heytaxi.message;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Queue;

public class ProducerPahoImpl implements Producer{
	private Queue<String> messages;
	private String clientId;
	private int qos = 0;
	
	private MqttClient mqtt;
	
	public ProducerPahoImpl(String clientId,Queue<String> messages){
		this.clientId=clientId;
		this.messages=messages;
	}

	public void send(String topic, String content) throws Exception{
        
       MqttMessage message = new MqttMessage(content.getBytes());
       message.setQos(qos);
       message.setRetained(true);
       mqtt.publish(topic, message);
//       mqtt.publish(topic,content.getBytes(), qos, true);
       
	}

	public void createConnection(String url,boolean clean) throws Exception {
		MemoryPersistence persistence = new MemoryPersistence();
		mqtt = new MqttClient(url, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(clean);
		System.out.println("Connecting to broker: "+url);
		mqtt.connect(connOpts);
		System.out.println("Connected");
	}

	@Override
	public void disconnection() throws Exception {
		if(mqtt.isConnected()){
			mqtt.close();
			mqtt.disconnect();
		}
	}

	public String provideClientId() {
		return clientId;
	}
	public String getClientId() {
		return clientId;
	}

	public MqttClient getMqtt() {
		return mqtt;
	}

	public void setMqtt(MqttClient mqtt) {
		this.mqtt = mqtt;
	}

	public Queue<String> getMessages() {
		return messages;
	}

	public void setMessages(Queue<String> messages) {
		this.messages = messages;
	}
	
	
}
