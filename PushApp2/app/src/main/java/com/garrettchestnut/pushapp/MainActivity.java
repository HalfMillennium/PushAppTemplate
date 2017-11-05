package com.garrettchestnut.pushapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
	}

	public void getSignUp(View view)
	{
		Intent intent = SignUp.makeIntent(MainActivity.this);
		startActivity(intent);
	}

	public void getLogIn(View view)
	{
		Intent intent = LogIn.makeIntent(MainActivity.this);
		startActivity(intent);
	}
}
