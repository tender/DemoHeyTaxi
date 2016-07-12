package com.ta.demo.heytaxi.message;


/**
 * Created by IT-0002993 on 16/4/28.
 */
public interface Producer {

    public void send(String topic, String content) throws Exception;

    public void createConnection(String url, boolean clean) throws Exception;

    public String provideClientId();

    public void disconnection() throws Exception;
}

