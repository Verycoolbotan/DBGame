package ru.samsung.itschool.dbgame;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HoFActivity extends Activity {

	private DBManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ho_f);
		dbManager = DBManager.getInstance(this);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setDivider(getResources().getDrawable(android.R.color.transparent));
		
		//TextView restv = (TextView)this.findViewById(R.id.results);
		ArrayList<Result> results = dbManager.getAllResults();
		ArrayList<String> resStr = new ArrayList<String>();
		for (Result res : results)
		{
			resStr.add(res.name + ": " + res.score + "\n");
		}	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resStr);
		listView.setAdapter(adapter);
	}
	
	public void dropDB(View v){
		dbManager = DBManager.getInstance(this);
		dbManager.clearResults();
	}
}
