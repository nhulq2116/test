package com.mililu.moneypower;


import android.app.Activity;
import android.content.Context;
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

public class CreateWalletActivity extends Activity {
	private DataBaseAdapter dbAdapter;
	private Button btnBack, btnInsertWallet;
	private EditText txtNameWallet, txtMoney, txtDescription;
	private TextView tvTittle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_create_wallet);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	   
	    Typeface light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    Typeface bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
	    
	    // Get The Reference Of Views
	    tvTittle = (TextView) findViewById(R.id.tv_createwallet_title);
	    txtNameWallet = (EditText)findViewById(R.id.txt_createwallet_namewallet);
	    txtMoney = (EditText)findViewById(R.id.txt_createwallet_money);
	    txtDescription = (EditText)findViewById(R.id.txt_createwallet_description);
	    btnBack=(Button)findViewById(R.id.btn_createwallet_back);
	    btnInsertWallet=(Button)findViewById(R.id.btn_createwallet_submit);
	    // Set face
	    tvTittle.setTypeface(light);
	    txtNameWallet.setTypeface(light);
	    txtMoney.setTypeface(light);
	    txtDescription.setTypeface(light);
	    btnInsertWallet.setTypeface(bold);
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnInsertWallet.setOnClickListener(new MyEvent());
	}

	public class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_createwallet_back)
			{
				CreateWalletActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_createwallet_submit) {
					InsertWallet();
			}
		}
	}
	
	private void InsertWallet(){
		int id =  HomeActivity.id_user;
		String namewallet = txtNameWallet.getText().toString();
		String money = txtMoney.getText().toString();
		String descrip = txtDescription.getText().toString();

		// check if any of the fields are vaccant
		if(namewallet.equals("")||money.isEmpty()||Long.valueOf(money)<0)
		{
				Toast.makeText(this, "Please write your wallet's name and money ☝", Toast.LENGTH_LONG).show();
				return;
		}
		else{
			// Save the Data in Database
			dbAdapter.insertWallet(namewallet, Long.valueOf(money),  id, descrip);
			Toast.makeText(getApplicationContext(), "Wallet has been created 😏", Toast.LENGTH_LONG).show();
			CreateWalletActivity.this.finish();
		}
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