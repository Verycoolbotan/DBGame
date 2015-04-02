package ru.samsung.itschool.dbgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Stats extends Activity {

	private DBManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		TextView stats = (TextView) this.findViewById(R.id.stats);
		dbManager = DBManager.getInstance(this);
		float even = (float)(dbManager.percentOfEven()/dbManager.numOfGames()*100);
		stats.setText("Сумма всех очков: " + dbManager.sumScores() + "\n"
				+ "Лучший счёт: " + dbManager.maxScore() + "\n"
				+ "Количество игроков: " + dbManager.numOfPlayers() + "\n" +
				"Количество сыгранных игр: " + dbManager.numOfGames() + "\n" +
				"Процент чётных чисел: " + even + "\n" +
				"Процент чётных чисел: " + (100-even) + "\n");

	}
}
