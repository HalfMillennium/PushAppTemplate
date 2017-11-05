package com.garrettchestnut.pushapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

	DatabaseReference ref;
	private boolean filledOut;
	int signUpReqCode;
	static String SignUpStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		ref = FirebaseDatabase.getInstance().getReference();
		SignUpStatus = "SU";
		signUpReqCode = 100;
		filledOut = true;
	}

	public void signUp(View view)
	{
		EditText fName = (EditText) findViewById(R.id.firstname);
		EditText lName = (EditText) findViewById(R.id.lastname);
		EditText display = (EditText) findViewById(R.id.eMail);
		EditText mail = (EditText) findViewById(R.id.email);
		EditText pass = (EditText) findViewById(R.id.passWord);

		final String first = fName.getText().toString();
		final String last = lName.getText().toString();
		final String username = display.getText().toString();
		final String email = mail.getText().toString();
		final String password = pass.getText().toString();

		if(first.equals(null))
			filledOut = false;
		if(last.equals(null))
			filledOut = false;
		if(username.equals(null))
			filledOut = false;
		if(email.equals(null))
			filledOut = false;
		if(password.equals(null))
			filledOut = false;

		if(filledOut) {
			Query getEmail = ref.child("users").orderByChild("email").equalTo(email);
			getEmail.addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					if (dataSnapshot.exists()) {
						Toast.makeText(SignUp.this, "Email Address Already In Use!", Toast.LENGTH_SHORT).show();
					} else {

						Query getUser = ref.child("users").child(username);
						getUser.addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot) {
								if (dataSnapshot.exists()) {
									Toast.makeText(SignUp.this, "Username Unavailable!", Toast.LENGTH_SHORT).show();
								} else {

									Intent intent = User.makeIntent(SignUp.this);
									intent.putExtra("username", username);
									intent.putExtra("password", password);
									intent.putExtra("name", first + " " + last);
									intent.putExtra("email", email);
									intent.putExtra("key", "signUp");

									startActivityForResult(intent, signUpReqCode);
								}
							}

							@Override
							public void onCancelled(DatabaseError databaseError) {
							}
						});
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
				}
			});
		} else {
			Toast.makeText(this, "All Forms Are Required!", Toast.LENGTH_SHORT).show();
		}
	}

	public void back(View view)
	{
		finish();
	}

	public void onResume()
	{
		super.onResume();

		if(User.active) {
			Toast.makeText(this, "You're All Set!", Toast.LENGTH_SHORT).show();
			finish();
			User.active = false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == signUpReqCode && resultCode == RESULT_OK && data != null) {
			String stat = data.getStringExtra(SignUpStatus);

			if(stat.equals("success")) {
				Toast.makeText(this, "You're All Set!", Toast.LENGTH_SHORT).show();
				User.user.finish();
			} else if(stat.equals("fail")) {
				Toast.makeText(this, "Sign Up Failed. Try Again.", Toast.LENGTH_SHORT).show();
				User.user.finish();
			}
		}
	}

	public static Intent makeIntent(Context context) { return new Intent(context, SignUp.class); }
}
