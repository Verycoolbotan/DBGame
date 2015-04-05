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
		if(dbManager!=null){
			int percEven = (int)((double)(dbManager.numOfEven())/(double)(dbManager.numOfGames())*100);
			stats.setText("Сумма всех очков: " + dbManager.sumScores() + "\n"
				+ "Лучший счёт: " + dbManager.maxScore() + "\n"
				+ "Количество игроков: " + dbManager.numOfPlayers() + "\n" +
				"Количество сыгранных игр: " + dbManager.numOfGames() + "\n" +
				"Чётных чисел в %: " + (int)(percEven) + "\n"+
				"Нечётных чисел в %: " + (int)(100-percEven));
		}
	}
}
