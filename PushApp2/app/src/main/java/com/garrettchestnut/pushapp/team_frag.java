package com.garrettchestnut.pushapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.text.Layout;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by glc22 on 8/24/2017.
 */

public class team_frag extends Fragment {
	private static final String TAG = "team_frag";
	DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
	String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
	private int teamCount = 0;
	private String[] joinedTeams = new String[6];
	private LinearLayout layout;
	private String team_name;

	//array index
	private int index = 0;
	///////////////////

	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_team, container, false);

		layout = (LinearLayout) view.findViewById(R.id.teamLayout);

		DatabaseReference getTeams = ref.child("users").child(username).child("joinedTms");
		getTeams.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {

				if(!dataSnapshot.exists()) {
					Toast.makeText(getActivity(), "Unable To Find Joined Teams", Toast.LENGTH_SHORT).show();
				}

				for(DataSnapshot snap : dataSnapshot.getChildren())
				{
					teamCount++;
					joinedTeams[index] = snap.getValue(String.class);
				}

				if(teamCount > 0)
				{
					for(int count = 0; count < 6; count++)
					{
						if(joinedTeams[count].equals(null))
						{
							layout.removeViewAt(count);
						}
					}

					int tempId = R.id.team_obj1;

					for(int count = 0; count < teamCount; count++)
					{
						DatabaseReference getName = ref.child("users").child(username).child("joinedTeams").child(joinedTeams[count]);
						getName.addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot) {
								if(dataSnapshot.exists())
								{
									team_name = dataSnapshot.getValue(String.class);
								}
							}

							@Override
							public void onCancelled(DatabaseError databaseError) {}
						});

						final TextView team = (TextView) layout.findViewById(tempId + count);
						team.setText(team_name);

						DatabaseReference getColor = ref.child("teams").child(joinedTeams[count]).child("color");
						getColor.addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot) {
								String color = dataSnapshot.getValue(String.class);
								if (color.equals("red")) {
									team.setBackgroundResource(R.drawable.red_team_box);
									team.setTextColor(Color.parseColor("#d20000"));
								}
								if (color.equals("green")) {
									team.setBackgroundResource(R.drawable.green_team_box);
									team.setTextColor(Color.parseColor("#07a303"));
								}
								if(color.equals("blue")) {
									team.setBackgroundResource(R.drawable.blue_team_box);
									team.setTextColor(Color.parseColor("#1c38da"));
								}

								// no 'if' statement for Orange, since that's the default color

							}

							@Override
							public void onCancelled(DatabaseError databaseError) {}
						});
					}
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});



		return view;
	}
}
