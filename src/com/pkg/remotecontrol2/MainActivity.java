package com.pkg.remotecontrol2;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//Remote Control with preferences (settings)
public class MainActivity extends Activity {

	private static final int RESULT_SETTINGS = 1;
	
	private String server_ip;
	private String server_port;
	private Button send_btn;
	private Button volup_btn;
	private Button voldown_btn;
	private EditText command;
	private String command_str = "";
	private boolean is_connected = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		command = (EditText) findViewById(R.id.editText1);
		send_btn = (Button) findViewById(R.id.button1);
		volup_btn = (Button) findViewById(R.id.volup);
		voldown_btn = (Button) findViewById(R.id.voldown);
		send_btn.setOnClickListener(connectListener);
		volup_btn.setOnClickListener(connectListener);
		voldown_btn.setOnClickListener(connectListener);
		
		
		showUserSettings();
	}
	
	private OnClickListener connectListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(arg0.getId()==volup_btn.getId()){
				command_str = "volup";
			} else if(arg0.getId()==voldown_btn.getId()){
				command_str = "voldown";
			} else {
				command_str = command.getText().toString();
			}
			if (!is_connected) {
				Thread clientThread = new Thread(new ClientThread());
				clientThread.start();
			}
		}
    };
    
    public class ClientThread implements Runnable {
    	 
        @Override
		public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(MainActivity.this.server_ip);
                Log.d("ClientActivity", "C: Connecting...");

                Socket socket = new Socket(serverAddr, Integer.parseInt(MainActivity.this.server_port));
                is_connected = true;
                if(is_connected) {
                	try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                            out.println(command_str);
                            Log.d("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                        e.printStackTrace();
                    }
                }
                
                socket.close();
                is_connected = false;
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                e.printStackTrace();
                is_connected = false;
            }
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.action_settings:
            Intent i = new Intent(this, RemoteControlSettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break;
        }
 
        return true;
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case RESULT_SETTINGS:
            showUserSettings();
            break;
 
        }
 
    }
	
	private void showUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
 
        StringBuilder builder = new StringBuilder();
 
        builder.append("\n Server Ip: "
                + sharedPrefs.getString("serverIp", "NULL"));
 
        builder.append("\n Server port: "
                + sharedPrefs.getString("serverPort", "NULL"));
        
        //Set vars for settings
        this.setServer_ip(sharedPrefs.getString("serverIp", "NULL"));
        this.setServer_port(sharedPrefs.getString("serverPort", "NULL"));
        Log.d("ClientActivity", builder.toString());
    }

	public String getServer_ip() {
		return server_ip;
	}

	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}

	public String getServer_port() {
		return server_port;
	}

	public void setServer_port(String server_port) {
		this.server_port = server_port;
	}
}
