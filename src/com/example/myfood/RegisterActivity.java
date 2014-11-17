package com.example.myfood;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utils.UsersDBManager;
import com.example.utils.myapplication;

public class RegisterActivity extends Activity {
	private UsersDBManager UsersDBManager1;
	private myapplication myapplication1;
	private EditText EditText1;
	private EditText EditText2;
	private EditText EditText3;
	private EditText EditText4;
	private Button Button1;
	private Button Button2;
	private Button Button3;
	private int result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		UsersDBManager1 = new UsersDBManager(this);
		myapplication1 = (myapplication) getApplication();
		myapplication1.getInstance().addActivity(this);
		if (myapplication1.ifpass()) {
			Intent Intent1 = new Intent();
			Intent1.setClass(RegisterActivity.this, UsercenterActivity.class);
			startActivity(Intent1);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			finish();
		}
		EditText1 = (EditText) findViewById(R.register.editText1);
		EditText2 = (EditText) findViewById(R.register.editText2);
		Button1 = (Button) findViewById(R.register.button1);
		Button2 = (Button) findViewById(R.register.button2);
		Button3 = (Button) findViewById(R.register.button3);
		
		Button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();

			}
		});
		
		Button3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setContentView(R.layout.activity_zhuce);	
				check();
			}
			
		});
	}

	public void check() {
		EditText1 = (EditText) findViewById(R.zhuce.editText1);
		EditText2 = (EditText) findViewById(R.zhuce.editText2);
		EditText3 = (EditText) findViewById(R.zhuce.editText3);
		EditText4 = (EditText) findViewById(R.zhuce.editText4);
		Button1 = (Button) findViewById(R.zhuce.button1);
		Button1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean isValid = validate();
				if(isValid){
					Toast.makeText(RegisterActivity.this,
							"Register Success", Toast.LENGTH_SHORT).show();
					UsersDBManager1.login(EditText1.getText().toString());
					finish();
					
				}
				else{
					Toast.makeText(RegisterActivity.this,
							"Register Failed", Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
	}
	
	
	public void login() {
		final HttpClient Client = new DefaultHttpClient();
		String URL = "http://172.31.121.81:8080/Order_Test/servlet/LoginServlet?loginid="
				+ EditText1.getText().toString()
				+ "&password="
				+ EditText2.getText().toString();

		String SetServerString = "";
		HttpGet httpget = new HttpGet(URL);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			SetServerString = Client.execute(httpget, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(SetServerString.equals("Login Successful"))
			result = 1;
		else
			result = 0;

		// if (result == 1) {
		if (result==1) {
			Toast.makeText(RegisterActivity.this,
					EditText1.getText().toString(), Toast.LENGTH_SHORT).show();
			UsersDBManager1.login(EditText1.getText().toString());
			finish();
		}
		Toast.makeText(RegisterActivity.this, Integer.toString(result),
				Toast.LENGTH_SHORT).show();

		
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		finish();
	}
	
	public boolean validate() {
		String message = new String();
		String password1 = EditText2.getText().toString();
		String password2 = EditText3.getText().toString();
		String email = EditText4.getText().toString();
		if(!password1.equals(password2)){
			message = "The two entered passwords do not match, please try again to enter!";
			Toast.makeText(RegisterActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
		else if(email == null) {
			message = "Invalid email address";
			Toast.makeText(RegisterActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}		
		else if(password1 == null) {
			message = "Invalid password";
			Toast.makeText(RegisterActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}		
		else if(!email.matches("\\w+@\\w+\\.\\w+")) {
			message = "Invalid email address";
			Toast.makeText(RegisterActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}		
		else if(password1.length() < 8) {
			message = "Password must be at least 8 characters.";
			Toast.makeText(RegisterActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
		else if(password1.matches("\\w*\\s+\\w*")) {
			message = "Password cannot contain space.";
			Toast.makeText(RegisterActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
			return false;
		}
		else{
			message = "welcome!";
			Toast.makeText(RegisterActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
		}

		return true;
	}

//	private int doLogin(final String userName, final String userPwd) {
//			final String res = null;
//		
//            Toast.makeText(getBaseContext(),
//                    "Please wait, connecting to server.",
//                    Toast.LENGTH_SHORT).show();
//
//
//            // Create Inner Thread Class
//            Thread background = new Thread(new Runnable() {
//                 
//                private final HttpClient Client = new DefaultHttpClient();
//                private String URL = "http://172.31.121.81:8080/Order_Test/servlet/LoginServlet?loginid=" + userName + "&password=" + userPwd;
//                 
//                // After call for background.start this run method call
//                public void run() {
//                    try {
//
//                        String SetServerString = "";
//                        HttpGet httpget = new HttpGet(URL);
//                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
//                        SetServerString = Client.execute(httpget, responseHandler);
//                        threadMsg(SetServerString);
//
//                    } catch (Throwable t) {
//                        // just end the background thread
//                        Log.i("Animation", "Thread  exception " + t);
//                    }
//                }
//
//                private void threadMsg(String msg) {
//
//                    if (!msg.equals(null) && !msg.equals("")) {
//                        Message msgObj = handler.obtainMessage();
//                        Bundle b = new Bundle();
//                        b.putString("message", msg);
//                        msgObj.setData(b);
//                        handler.sendMessage(msgObj);
//                    }
//                }
//
//                // Define the Handler that receives messages from the thread and update the progress
//                private final Handler handler = new Handler() {
//
//                    public void handleMessage(Message msg) {
//                         
//                        String aResponse = msg.getData().getString("message");
//                        res = aResponse;
//                        if ((null != aResponse)) {
//
//                        	
//                            // ALERT MESSAGE
//                            Toast.makeText(
//                                    getBaseContext(),
//                                    "Server Response: "+aResponse,
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//
//                                // ALERT MESSAGE
//                                Toast.makeText(
//                                        getBaseContext(),
//                                        "Not Got Response From Server.",
//                                        Toast.LENGTH_SHORT).show();
//                        }    
//
//                    }
//                };
//
//            });
//            
//            // Start Thread
//            background.start();  //After call start method thread called run Method
//	}

}
