package com.garrettchestnut.pushapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by glc22 on 8/14/2017.
 */

public class User extends Activity
{
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private String TAG = "User";
	static boolean active = false, signIn = true, signUp = true;

	public static Activity user;

	//final String username, String password, final String name, String email

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.blank);
		user = this;

		mAuth = FirebaseAuth.getInstance();
		mAuthListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser user = firebaseAuth.getCurrentUser();
				if (user != null) {
					// User is signed in
					Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
				} else {
					// User is signed out
					Log.d(TAG, "onAuthStateChanged:signed_out");
				}
			}
		};

		if(getIntent().getExtras().getString("key").equals("signUp")) {
			final String email = getIntent().getExtras().getString("email");
			final String password = getIntent().getExtras().getString("password");
			final String name = getIntent().getExtras().getString("name");
			final String username = getIntent().getExtras().getString("username");

			Log.i("POINT", "REACHED");

			mAuth.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (task.isSuccessful()) {
								// Sign in success, update UI with the signed-in user's information
								Log.d("INFO", "createUserWithEmail:success");
								FirebaseUser user = mAuth.getCurrentUser();
								String displayName = username;

								UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
										.setDisplayName(username).build();

								user.updateProfile(profileUpdates);

								DatabaseReference base = FirebaseDatabase.getInstance().getReference();

								base.child("users").child(displayName).setValue(username);
								base.child("users").child(displayName).child("name").setValue(name);
								base.child("users").child(displayName).child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());

								base.push();
								active = true;

								Intent output = new Intent();
								output.putExtra(SignUp.SignUpStatus, "success");
								setResult(RESULT_OK, output);

								finish();

								Toast.makeText(User.this, "You're All Set!", Toast.LENGTH_SHORT).show();
							} else {
								// If sign in fails, display a message to the user.
								Log.w("INFO", "createUserWithEmail:failure", task.getException());

								Intent output = new Intent();
								output.putExtra(SignUp.SignUpStatus, "fail");
								setResult(RESULT_OK, output);
							}
						}
					});
		} else if(getIntent().getExtras().getString("key").equals("signIn")) {
			String email = getIntent().getExtras().getString("email");
			String password = getIntent().getExtras().getString("password");

			mAuth.signInWithEmailAndPassword(email, password)
					.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

							// If sign in fails, display a message to the user. If sign in succeeds
							// the auth state listener will be notified and logic to handle the
							// signed in user can be handled in the listener.
							if (!task.isSuccessful()) {
								Log.w(TAG, "signInWithEmail:failed", task.getException());
								signUp = false;

								Intent output = new Intent();
								output.putExtra(LogIn.LogInStatus, "fail");
								setResult(RESULT_OK, output);
								finish();
							} else {
								signIn = true;

								Intent output = new Intent();
								output.putExtra(LogIn.LogInStatus, "success");
								setResult(RESULT_OK, output);
								finish();
							}
						}
					});
		}
	}

	public static Intent makeIntent(Context context) { return new Intent(context, User.class); }

	@Override
	public void onStart() {
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAuthListener != null) {
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}
}
