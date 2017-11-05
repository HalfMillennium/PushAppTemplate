package com.garrettchestnut.pushapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class no_team extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_team);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setBackgroundColor(Color.parseColor("#2b2b2b"));
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.toolbar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_sign_out: {
				FirebaseAuth.getInstance().signOut();
				Toast.makeText(this, "Goodbye!", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("EXIT", true);
				startActivity(intent);
				break;
			}

			case R.id.action_account: {
				Toast.makeText(this, "This button doesn't do anything yet!", Toast.LENGTH_SHORT).show();
			}
			// case blocks for other MenuItems (if any)
		}
		return false;
	}

	public void joinTeam(View view)
	{
		// launch joinTeam activity
	}

	public static Intent makeIntent(Context context) { return new Intent(context, no_team.class); }

}
