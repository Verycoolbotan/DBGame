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
		stats.setText("����� ���� �����: " + dbManager.sumScores() + "\n"
				+ "������ ����: " + dbManager.maxScore() + "\n"
				+ "���������� �������: " + dbManager.numOfPlayers() + "\n");

	}
}
