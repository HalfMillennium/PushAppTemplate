package com.garrettchestnut.pushapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

	int signInReqCode;
	static String LogInStatus;
	DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);

		signInReqCode = 200;
		LogInStatus = "LI";
	}

	public void logIn(View view)
	{
		EditText mail = (EditText) findViewById(R.id.eMail);
		EditText pass = (EditText) findViewById(R.id.passWord);

		String email = mail.getText().toString();
		String password = pass.getText().toString();

		Intent intent = User.makeIntent(LogIn.this);
		intent.putExtra("email", email);
		intent.putExtra("password", password);
		intent.putExtra("key", "signIn");

		startActivityForResult(intent, signInReqCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == signInReqCode && resultCode == RESULT_OK && data != null) {
			String stat = data.getStringExtra(LogInStatus);

			if(stat.equals("success")) {
				// enter Objective menu
				User.user.finish();
				Log.i(" ", "nice");

				DatabaseReference getTeam = ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child("teams");
				getTeam.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {
						if(dataSnapshot.hasChildren())
						{

						} else {
							Intent intent = no_team.makeIntent(LogIn.this);
							startActivity(intent);
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
			} else if(stat.equals("fail")) {
				User.user.finish();
				Toast.makeText(this, "Log In Failed. Username/Password May Be Incorrect.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void back(View view)
	{
		finish();
	}

	public static Intent makeIntent(Context context) { return new Intent(context, LogIn.class); }

}
