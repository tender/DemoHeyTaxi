package com.ta.demo.heytaxi.message;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by IT-0002993 on 16/4/28.
 */
public class MessageHelper {


    //private static final String URL="tcp://127.0.0.1:1883";
    private static final String URL="tcp://192.168.0.104:1883";

    private Producer _producer;
    private Consumer _consumer;
    Queue<String> store;
    private boolean isStopReceive=false;


    public MessageHelper(Producer _producer,Queue<String> store){
        this._producer=_producer;
        this.store=store;
    }

    public MessageHelper(Consumer _consumer,Queue<String> store){

        this._consumer=_consumer;
        this.store=store;
    }

    public MessageHelper(Producer _producer,Consumer _consumer,Queue<String> store){
        this._producer=_producer;
        this._consumer=_consumer;
        this.store=store;
    }

    public MessageHelper(Queue<String> store){
        this.store=store;
    }


    public void send(String topic,String content) throws Exception{
        getProducer().createConnection(URL,false);
        getProducer().send(topic,content);

    }

    public void receive(String topic) throws Exception{
        getConsumer().createConnection(URL, false);
        getConsumer().receive(topic);
        System.out.println("Receive");
        //return getConsumer().getMessages();
    }

    public List<String> getMessages() {
        List<String> result=new ArrayList<String>();
        while(store.peek() != null) {
            String message = (String)store.poll();
            System.out.println("Helper Receive Message:"+message);
            result.add(message);
        }
        return result;
    }


    public Consumer getConsumer() {
        return _consumer;
    }

    public void setConsumer(Consumer _consumer) {
        this._consumer = _consumer;
    }

    public Producer getProducer() {
        return _producer;
    }

    public void setProducer(Producer _producer) {
        this._producer = _producer;
    }

    public Queue<String> getStore() {
        return store;
    }

    public void setStore(Queue<String> store) {
        this.store = store;
    }


}

