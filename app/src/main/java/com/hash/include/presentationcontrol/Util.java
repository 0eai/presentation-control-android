package com.hash.include.presentationcontrol;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import static android.content.Context.MODE_PRIVATE;


public class Util {

    public static String protocol = "http";
    public static String address = "10.57.6.91";
    public static String port = "9999";

    public static String protocolKey = "protocol";
    public static String addressKey = "address";
    public static String portKey = "port";
    public static String spName = "presentationcontrol";

    public static String KEY_PRESS = "KEY_PRESS";

    public static String tab = "tab";
    public static String right = "right";
    public static String left = "left";
    public static String f4 = "f4";
    public static String f5 = "f5";
    public static String f11 = "f11";

    public static  Socket mSocket = null;

    public static Socket getSocket(){
        if(mSocket==null){
            try{
                String uri = protocol + "://" + address + ":" + port;
                mSocket= IO.socket(uri);
                if (!mSocket.connected()) {
                    mSocket.connect();
                    Log.i("Socket","connected");
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("error connecting","to server");
            }
        }
        return mSocket;
    }
}
