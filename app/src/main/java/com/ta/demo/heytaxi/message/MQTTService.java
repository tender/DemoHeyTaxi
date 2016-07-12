package com.ta.demo.heytaxi.message;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MQTTService extends Service {

    private static String TAG="MQTTService";
    private final IBinder messageBinder = new MessageBinder();

    MessageHelper messageHelper;
    Queue<String> store=new LinkedList<String>();
    Consumer consumer;
    String clientId;
    String topic;

    private Handler handler = new Handler();
    private Runnable receiverMessage = new Runnable() {
        public void run() {
            try {
                messageHelper.receive(topic);
                handler.postDelayed(this, 5000);
                getStoreMessages();
            }catch(Exception e){
                Log.e(TAG,e.getMessage());
            }

        }
    };

    public MQTTService() {
    }

    // 實作 Binder
    public class MessageBinder extends Binder {
        public MQTTService getService() {
            return MQTTService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messageBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("mylog", "onRebind()");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("mylog", "onUnbind()");
        //handler.removeCallbacks(showTime);
        return super.onUnbind(intent);
    }



    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clientId=intent.getStringExtra("clientId");
        topic=intent.getStringExtra("topic");
        consumer=new ConsumerPahoImpl(clientId,store);
        messageHelper=new MessageHelper(consumer,store);
//        try {
            handler.post(receiverMessage);
//
//        }catch(Exception e){
//            Log.e(TAG,e.getMessage());
//        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getStoreMessages() {
        Log.i(TAG,"getStoreMessage().size:"+store.size());
        //List<String> result=new ArrayList<String>();
        List<String> vos=(List<String>)consumer.getMessages();
        for(String vo:vos) {
            Log.i(TAG,"getMessage() is :"+vo);
        }
        //return result;
    }


    public MessageHelper getMessageHelper() {
        return messageHelper;
    }

    public Queue<String> getStore() {
        return store;
    }
}
