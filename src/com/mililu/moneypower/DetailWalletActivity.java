package com.mililu.moneypower;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.mililu.moneypower.classobject.Diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailWalletActivity extends Activity {
	static AdapterDetailWalletArray adtDeatailWalletArr = null;
	private Button btnBack, btnDelete;
	private static DataBaseAdapter dbAdapter;
	private List<Diary>list_diary = new ArrayList<Diary>();
	public static int id_current_wallet;
	private TextView tvTittle, tvTittle1 ;
	private static TextView tvAmount;
	private static TextView tvOriginalAmount;
	private static TextView tvNotice;
	private ListView lvDiary;
	private Cursor cursorDiary;
	private Typeface tf, tf1 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_detail_wallet);
		
		// Set font
		tf = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
		tf1 = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
//        txtTittle.setTypeface(font_light);
		
		// Get The Reference Of View
	    tvTittle=(TextView)findViewById(R.id.tv_detailwallet_title);
	    tvTittle.setTypeface(tf);
	    tvTittle1 =(TextView)findViewById(R.id.tv_detailwallet_titlebalance);
	    tvTittle1.setTypeface(tf);
	    tvAmount=(TextView)findViewById(R.id.tv_detailwallet_amount);
	    tvAmount.setTypeface(tf1);
	    tvOriginalAmount=(TextView)findViewById(R.id.tv_detailwallet_originalamount);
	    tvOriginalAmount.setTypeface(tf);
	    tvNotice=(TextView)findViewById(R.id.tv_detailwallet_notice);
	    tvNotice.setTypeface(tf);
	    btnBack=(Button)findViewById(R.id.btn_detailwallet_back);
	    btnDelete=(Button)findViewById(R.id.btn_detailwallet_delete);
	    lvDiary = (ListView)findViewById(R.id.lv_detailwallet_listhistory);
	
        
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnDelete.setOnClickListener(new MyEvent());
	    
	    // Set OnItemClick Listener on listview
	    //lvDiary.setOnItemClickListener(new MyEventItemOnClick());
	    
	    // Get data of Bundle
	    Intent intent = getIntent();
	    Bundle bundle = intent.getBundleExtra("DATA");
	    id_current_wallet = bundle.getInt("ID_WALLET");
	    tvTittle.setText(bundle.getString("NAME_WALLET"));
	    
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		ShowListDiary();
		ShowInforWallet();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_detailwallet_back)
			{
				DetailWalletActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_detailwallet_delete)
			{
				//// do something here
				AlertDialog.Builder dialogmess = new Builder(DetailWalletActivity.this);
				dialogmess.setTitle("Remove Wallet");
				dialogmess.setMessage("Do you wanna delete " + tvTittle.getText() + "?");
				dialogmess.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dbAdapter.deleteWallet(id_current_wallet);
						Toast.makeText(DetailWalletActivity.this, "Delete Success", Toast.LENGTH_LONG).show();
						DetailWalletActivity.this.finish();
					}
				});
				dialogmess.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				dialogmess.show();
			}
		}
	}
	
	private void ShowListDiary(){
		cursorDiary=dbAdapter.getDiaryOfWallet(id_current_wallet);
		if (cursorDiary.getCount()<1){
			Toast.makeText(DetailWalletActivity.this, "You don't have any history !!", Toast.LENGTH_LONG).show();
		}
		else{
			list_diary.clear();
			cursorDiary.moveToFirst();
			while(!cursorDiary.isAfterLast()){
				Diary data=new Diary();
				data.setId_diary(cursorDiary.getInt(cursorDiary.getColumnIndexOrThrow("ID_DIARY")));
				data.setAmount(cursorDiary.getInt(cursorDiary.getColumnIndexOrThrow("AMOUNT")));
				data.setName_income(cursorDiary.getString(cursorDiary.getColumnIndexOrThrow("NAME_INCOME")));
				data.setName_expen(cursorDiary.getString(cursorDiary.getColumnIndexOrThrow("NAME_EXP_DET")));
				data.setDay(cursorDiary.getInt(cursorDiary.getColumnIndexOrThrow("DAY")));
				data.setMonth(cursorDiary.getInt(cursorDiary.getColumnIndexOrThrow("MONTH")));
				data.setYear(cursorDiary.getInt(cursorDiary.getColumnIndexOrThrow("YEAR")));
				data.setTime(cursorDiary.getString(cursorDiary.getColumnIndexOrThrow("TIME")));
				data.setType(cursorDiary.getInt(cursorDiary.getColumnIndexOrThrow("TYPE")));
				data.setNotice(cursorDiary.getString(cursorDiary.getColumnIndexOrThrow("NOTICE")));
				list_diary.add(data);
				cursorDiary.moveToNext();
		 	}
			cursorDiary.close();
			adtDeatailWalletArr = new AdapterDetailWalletArray(DetailWalletActivity.this, R.layout.layout_for_detail_wallet, list_diary);
			lvDiary.setAdapter(adtDeatailWalletArr);
		}
	}
	public static void ShowInforWallet(){
		String money = NumberFormat.getCurrencyInstance().format(dbAdapter.getAmountOfWallet(id_current_wallet));
		String originalamount = NumberFormat.getCurrencyInstance().format(dbAdapter.getOriginalAmountOfWallet(id_current_wallet));
		String notice = dbAdapter.getDecriptionOfWallet(id_current_wallet);
		
		tvAmount.setText(money);
		tvOriginalAmount.setText("Original Amount: " + originalamount);
		tvNotice.setText(notice);
	}
}
