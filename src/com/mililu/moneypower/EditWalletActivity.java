package com.mililu.moneypower;

import com.mililu.moneypower.classobject.Wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditWalletActivity extends Activity {
	private Cursor walletCursor;
	private DataBaseAdapter dbAdapter;
	private Button btnBack, btnUpdateWallet;
	private EditText txtNameWallet, txtMoney, txtDescription;
	private TextView tvTittle;
	private int id_wallet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_create_wallet);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    Intent intent = getIntent();
	    id_wallet = intent.getIntExtra("id_wallet", 0);
	   
	    Typeface light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    Typeface bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
	    
	    // Get The Reference Of Views
	    tvTittle = (TextView) findViewById(R.id.tv_createwallet_title);
	    txtNameWallet = (EditText)findViewById(R.id.txt_createwallet_namewallet);
	    txtMoney = (EditText)findViewById(R.id.txt_createwallet_money);
	    txtDescription = (EditText)findViewById(R.id.txt_createwallet_description);
	    btnBack=(Button)findViewById(R.id.btn_createwallet_back);
	    btnUpdateWallet=(Button)findViewById(R.id.btn_createwallet_submit);
	    // Set face
	    tvTittle.setTypeface(light);
	    txtNameWallet.setTypeface(light);
	    txtMoney.setTypeface(light);
	    txtDescription.setTypeface(light);
	    btnUpdateWallet.setTypeface(bold);
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnUpdateWallet.setOnClickListener(new MyEvent());
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getWalletInfo(id_wallet);
	}


	public class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_createwallet_back)
			{
				EditWalletActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_createwallet_submit) {
					String namewallet = txtNameWallet.getText().toString();
					String money = txtMoney.getText().toString();
					String descrip = txtDescription.getText().toString();
					// check if any of the fields are vaccant
					if(namewallet.equals("")||money.isEmpty()||Long.valueOf(money)<0)
					{
							Toast.makeText(getApplicationContext(), "Please write your wallet's name and money ", Toast.LENGTH_LONG).show();
							return;
					}
					else {
						Wallet wallet = new Wallet();
						wallet.setId_wallet(id_wallet);
						wallet.setName(namewallet);
						wallet.setOrg_money(Long.valueOf(money));
						wallet.setDescrip(descrip);
						UpdateWallet(wallet);
					}
			}
		}
	}
	
	private void getWalletInfo(int id_wallet){
		walletCursor = dbAdapter.getInforWallet(id_wallet);
		walletCursor.moveToFirst();
    	String name = walletCursor.getString(walletCursor.getColumnIndexOrThrow("NAME_WALLET"));
		long oldamount = walletCursor.getLong(walletCursor.getColumnIndexOrThrow("ORIGINAL_AMOUNT"));
    	String notice = walletCursor.getString(walletCursor.getColumnIndexOrThrow("DESCRIPTION"));
    	
    	txtNameWallet.setText(name);
    	txtMoney.setText(String.valueOf(oldamount));
    	txtDescription.setText(notice);
	}
	
	private void UpdateWallet(Wallet wallet){
		// Save the Data in Database
		dbAdapter.updateWallet(wallet);
		Toast.makeText(getApplicationContext(), "Wallet has been updated", Toast.LENGTH_LONG).show();
		EditWalletActivity.this.finish();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
	    View v = getCurrentFocus();

	    if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && 
	            v instanceof EditText && !v.getClass().getName().startsWith("android.webkit.")) {
	        int scrcoords[] = new int[2];
	        v.getLocationOnScreen(scrcoords);
	        float x = ev.getRawX() + v.getLeft() - scrcoords[0];
	        float y = ev.getRawY() + v.getTop() - scrcoords[1];

	        if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
	            hideKeyboard(this);
	    }
	    return super.dispatchTouchEvent(ev);
	}

	public static void hideKeyboard(Activity activity) {
	    if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
	        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
	    }
	}
}
