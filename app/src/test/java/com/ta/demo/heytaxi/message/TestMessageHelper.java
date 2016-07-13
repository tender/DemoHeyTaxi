package com.ta.demo.heytaxi.message;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMessageHelper {

	static MessageHelper helper;
	static Queue<String> store=new LinkedList<String>();
	
	@BeforeClass
	public static void init(){
		helper=new MessageHelper(store);
	}
	
	@Test
	public void testSend() {
		//fail("Not yet implemented");
		//Producer producer=new ProducerImpl("producer");

		Producer producer=new ProducerPahoImpl("producer",helper.getStore());
		helper.setProducer(producer);
		String topic="HeyTaxi Message Server";
		String message="Test Message";
		try{
			for(int i=0;i<100;i++){
				String timestamp=String.valueOf(System.currentTimeMillis());
				System.out.println(message+i+"_"+timestamp);
				//Thread.sleep(100);
				helper.send(topic, message+i+"_"+timestamp);
			}
			List<String> storeMessage=helper.getMessages();
			System.out.println("store message size:"+storeMessage.size());
			for(String vo:storeMessage){
				System.out.println("Return:"+vo);
			}

		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRecive() {
		//fail("Not yet implemented");
		//Consumer consumer=new ConsumerImpl("consumer");
		Consumer consumer=new ConsumerPahoImpl("consumer",helper.getStore());
		helper.setConsumer(consumer);
		//MQTT client=null;
		String topic="HeyTaxi Message Server";
		//List<Message> messages=new ArrayList<Message>();
		try{
			//client=helper.createConnection(false);
			helper.receive(topic);
		}catch(Exception e){
			System.out.println(e);
		}
	}
//	
//	@Test 
//    public void testSendAndReceiveAtLeastOnce() throws Exception { 
//        MQTT consumerClient = createMQTTTcpConnection("consumer", true); 
//        consumerClient.setConnectAttemptsMax(0); 
//        consumerClient.setReconnectAttemptsMax(0); 
//        BlockingConnection consumeConnection = consumerClient.blockingConnection(); 
//        consumeConnection.connect(); 
// 
//        String topic = "foo"; 
// 
//        Topic[] topics = {new Topic(utf8(topic), QoS.values()[1])}; 
//        consumeConnection.subscribe(topics); 
// 
//        MQTT producerClient = createMQTTTcpConnection("producer", true); 
// 
//        producerClient.setConnectAttemptsMax(0); 
//        producerClient.setReconnectAttemptsMax(0); 
//        BlockingConnection producerConnection = producerClient.blockingConnection(); 
//        producerConnection.connect(); 
// 
//        for (int i = 0; i < 100; i++) { 
//            String payload = "Test Message: " + i; 
//            producerConnection.publish(topic, payload.getBytes(), QoS.values()[1], false); 
//            Message message = consumeConnection.receive(2, TimeUnit.SECONDS); 
//            //assertNotNull("Should get a message", message); 
//            //assertEquals(payload, new String(message.getPayload()));
//            System.out.println(new String(message.getPayload()));
//        } 
//        consumeConnection.disconnect(); 
//        producerConnection.disconnect(); 
//    } 
// 
//    private MQTT createMQTTTcpConnection(String clientId, boolean clean) throws Exception { 
//        MQTT mqtt = new MQTT(); 
//        mqtt.setConnectAttemptsMax(1); 
//        mqtt.setReconnectAttemptsMax(0); 
//        if (clientId != null) { 
//            mqtt.setClientId(clientId); 
//        } 
//        mqtt.setCleanSession(clean); 
//        mqtt.setHost("localhost", 1883); 
//        return mqtt; 
//    } 

}
