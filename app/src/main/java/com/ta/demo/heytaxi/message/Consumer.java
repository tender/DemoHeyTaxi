package com.ta.demo.heytaxi.message;


import java.util.List;

/**
 * Created by IT-0002993 on 16/4/28.
 */
public interface Consumer {

   public void receive(String topic) throws Exception;

    public void createConnection(String url, boolean clean) throws Exception;

    public List<?> getMessages();

    public String provideClientId();

    public void disconnection() throws Exception;
}
