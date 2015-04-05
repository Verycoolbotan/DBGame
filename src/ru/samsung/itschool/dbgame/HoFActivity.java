package ru.samsung.itschool.dbgame;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;

public class HoFActivity extends Activity {

	protected static final String TAG = "HoFActivity";
	private DBManager dbManager;
	ArrayList<Result> results = new ArrayList<Result>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ho_f);
		dbManager = DBManager.getInstance(this);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setDivider(getResources().getDrawable(android.R.color.transparent));

		// TextView restv = (TextView)this.findViewById(R.id.results);
		results.clear();
		results.addAll(dbManager.getAllResults());
		ArrayList<String> resStr = new ArrayList<String>();
		for (Result res : results) {
			resStr.add(res.name + ": " + res.score + "\n");
		}
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, resStr));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(HoFActivity.this, PlayerActivity.class);
				i.putExtra("playerID", new Integer(dbManager.getPlayerIDByName(results.get(position).name)).toString());
				Log.d(TAG, "Position " + position);
				HoFActivity.this.startActivity(i);
			}
		});
	}	
	
	public void dropDB(View v) {
		dbManager = DBManager.getInstance(this);
		dbManager.clearResults();
	}
	
	//==========================================================================================================================
	
}
