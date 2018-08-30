package com.hash.include.presentationcontrol;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private ImageButton left;
    private ImageButton right;
    private ImageButton close;
    private ImageButton tab;
    private ImageButton web;
    private ImageButton presentationPlay;
    private ImageButton fullscreen;

    private boolean flagCtrl = false;
    private boolean flagAlt = false;
    private boolean flagShift = false;
    private boolean flagCmd = false;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(Util.spName, MODE_PRIVATE);
        editor = sp.edit();

        Util.protocol = sp.getString(Util.protocolKey, "http");
        Util.address = sp.getString(Util.addressKey, "0.0.0.0");
        Util.port = sp.getString(Util.portKey, "");

        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        close = findViewById(R.id.close);
        tab = findViewById(R.id.tab);
        web = findViewById(R.id.web);
        presentationPlay = findViewById(R.id.presentation);
        fullscreen = findViewById(R.id.fullscreen);

        tab.setOnClickListener(this);
        web.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        close.setOnClickListener(this);
        presentationPlay.setOnClickListener(this);
        fullscreen.setOnClickListener(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.getSocket().disconnect();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            keyTap(Util.left);
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            keyTap(Util.right);
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.web:
                keyWeb();
                break;
            case R.id.left:
                keyLeft();
                break;
            case R.id.right:
                keyRight();
                break;
            case R.id.close:
                keyClose();
                break;
            case R.id.tab:
                switchApp();
                break;
            case R.id.presentation:
                keyPresentation();
                break;
            case R.id.fullscreen:
                keyFullscreen();
                break;
        }
    }

    public void keyWeb() {
        showAddressDialog(this);
    }

    public void showAddressDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_address);
        dialog.setCancelable(true);
        final EditText protocolEditText = dialog.findViewById(R.id.protocol_edittext);
        final EditText addressEditText = dialog.findViewById(R.id.address_edittext);
        final EditText portEditText = dialog.findViewById(R.id.port_edittext);
        protocolEditText.setText(Util.protocol);
        addressEditText.setText(Util.address);
        portEditText.setText(Util.port);
        Button sendButton = dialog.findViewById(R.id.dialog_save);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(Util.protocolKey, protocolEditText.getText().toString());
                editor.putString(Util.addressKey, addressEditText.getText().toString());
                editor.putString(Util.portKey, portEditText.getText().toString());
                editor.apply();
                Util.protocol = protocolEditText.getText().toString();
                Util.address = addressEditText.getText().toString();
                Util.port = portEditText.getText().toString();
                connect();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void connect() {
        Util.getSocket().disconnect();
        Util.mSocket = null;
    }

    public void keyLeft() {
        keyTap(Util.left);
    }

    public void keyRight() {
        keyTap(Util.right);
    }

    public void keyPresentation() {
        keyTap(Util.f5);
    }

    public void keyFullscreen() {
        keyTap(Util.f11);
    }


    public void keyClose() {
        flagCtrl = false;
        flagAlt = true;
        flagCmd = false;
        flagShift = false;
        keyToggle(Util.f4);
        flagAlt = false;
    }

    public void switchApp() {
        flagAlt = true;
        flagCtrl = false;
        flagCmd = false;
        flagShift = false;
        keyToggle(Util.tab);
        flagAlt = false;
    }

    public void keyToggle(String key) {
        try {
            JSONObject object = new JSONObject();
            if (flagAlt) {
                object.put("alt", "alt");
            }
            if (flagCtrl) {
                object.put("ctrl", "ctrl");
            }
            if (flagShift) {
                object.put("shift", "shift");
            }
            if (flagCmd) {
                object.put("command", "command");
            }
            object.put("key", key);
            Util.getSocket().emit(Util.KEY_PRESS, object);
            Log.d("Single_key", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void keyTap(String text) {
        if (!flagAlt && !flagCtrl && !flagShift && !flagCmd ) {
            Util.getSocket().emit("single_key", text);
            Log.d("Single_key", text);
        } else {
            keyToggle(text);
        }
    }
}
