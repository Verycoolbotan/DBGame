package ru.samsung.itschool.dbgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity {

	static DBManager dbManager;

	EditText playerName;
	TextView gameResult;
	ImageButton playButton;

	@Override
	protected void onCreate(Bundle  savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		dbManager = DBManager.getInstance(this);
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.fadin);
		gameResult = (TextView) this.findViewById(R.id.GameResult);
		playButton = (ImageButton) this.findViewById(R.id.playButton);
		playerName = (EditText) this.findViewById(R.id.playerName);
	}

	public void play(View v) {

		Animation play = AnimationUtils.loadAnimation(this, R.anim.fadin);

		play.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				playButton.setClickable(false);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				playButton.setClickable(true);
				dbManager.addResult(MainActivity.this.playerName.getText().toString(),
						            Integer.parseInt(MainActivity.this.gameResult.getText().toString()));
			}
		});

		gameResult.setText(((int) (Math.random() * 1001))+"");
		int r = Integer.parseInt(gameResult.getText().toString());
		if(r%2==0){
			gameResult.setTextColor(Color.GREEN);
		}
		else{
			gameResult.setTextColor(Color.RED);
		}
		gameResult.startAnimation(play);

	}

	public void showHoF(View v) {
		startActivity(new Intent(this, HoFActivity.class));
	}
	
	public void showStats(View v) {
		startActivity(new Intent(this, Stats.class));
	}
	
	public void clear(View v) {
		dbManager.clearResults();
	}
}
