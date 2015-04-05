package ru.samsung.itschool.dbgame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerActivity extends Activity {
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	String userPics_path="";
	String username = "";
	String uid;
	private DBManager dbManager;
	private int playerID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dbManager = DBManager.getInstance(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		TextView pName = (TextView)this.findViewById(R.id.playerName);
		uid = getIntent().getExtras().getString("playerID");
		pName.setText(dbManager.getUserName(Integer.parseInt(uid)));
		
		userPics_path = this.getExternalFilesDir(null).getAbsolutePath() + "/userpics";
		Intent i = this.getIntent();
		playerID = (i.getExtras()).getInt("playerID");
		dbManager = DBManager.getInstance(this);
		playerID = Integer.parseInt(uid);
		showPlayerData();
	}
	
	private void showPlayerData() {
		String playerName = dbManager.getUserName(playerID);
		String playerPic = dbManager.getUserPicName(playerID);
		TextView tv = (TextView) findViewById(R.id.playerName);
		tv.setText(playerName);
		TextView tvstats = (TextView) findViewById(R.id.playerStats);
		tvstats.setText("Лучший счёт: " + dbManager.maxScoreByPlayer(playerID) + "\n"
				+ "Количество сыгранных игр: " + dbManager.numOfGamesByPlayer(playerID) + "\n");
		if (!playerPic.equals(""))
		{
			ImageView userPic = (ImageView) this.findViewById(R.id.userPic);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(userPics_path+"/"+playerPic+".png", options);
			userPic.setImageBitmap(bitmap);
		}
	}
	
	public void deleteMe(View v) {
		dbManager = DBManager.getInstance(this);
		dbManager.deletePData(playerID);
	}
	
//============================================================================================================================
	public void getPicture(View v) {
		//Task.showMessage(this, "Реализуйте метод PlayerActivity.getPicture"); 
		// http://developer.android.com/training/camera/photobasics.html
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// http://developer.android.com/training/camera/photobasics.html
		//сохранить:
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        String pic = savePic(imageBitmap);
	        dbManager.userUpdate(playerID, dbManager.getUserName(playerID), pic);
	    }
	}

	private String savePic(Bitmap userpic) {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File dir = new File(userPics_path);
		if (!dir.exists()) dir.mkdirs();
		File file = new File(dir, timeStamp + ".png");
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);

			userpic.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
			fOut.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
        return timeStamp;
	}
}
