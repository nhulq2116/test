package com.mililu.moneypower;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Typeface;

public class LoginActivity extends Activity {
	private Button btnLogin, btnRegister;
	private EditText txtUserName, txtPassword;
	private DataBaseAdapter dbAdapter;
	private Cursor mAccountCorsor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		
	    // Get The Reference In View
	    txtUserName = (EditText)findViewById(R.id.txt_login_username);
	    txtPassword = (EditText)findViewById(R.id.txt_login_password);
	    btnLogin=(Button)findViewById(R.id.btn_login_submit);
	    btnRegister=(Button)findViewById(R.id.btn_login_register);
	    
	    //Thiet lap font de su dung tu assets
        Typeface font_light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUE.TTF");
        Typeface font_italic = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHTITALIC.TTF");
        
        //Thiet lap font
        txtUserName.setTypeface(font_light);
        txtPassword.setTypeface(font_light);
	    btnLogin.setTypeface(font);
	    btnRegister.setTypeface(font_italic);
	    
	    // Set OnClick Listener on SignUp button 
	    btnLogin.setOnClickListener(new MyEvent());
	    btnRegister.setOnClickListener(new MyEvent());
	}

	private class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_login_register)
			{
				Intent intent = new Intent (LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
			else if(v.getId()==R.id.btn_login_submit) {
				Login();
			}
		}
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		// Create a instance of SQLite Database
		CreateDB();
	}
	
	private void CreateDB() {
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	}

	public void Login(){
		String username = txtUserName.getText().toString(); 
		String password = txtPassword.getText().toString(); 
		
		// Get validation
		if(username.equals("")||password.equals(""))
		{
				Toast.makeText(getApplicationContext(), "Please insert username and password", Toast.LENGTH_LONG).show();
				return;
		}
		else{ 
			mAccountCorsor = dbAdapter.getAccountInfor(username);
			if(mAccountCorsor.getCount()<1) // UserName Not Exist
	        {
	        	mAccountCorsor.close();
	        	Toast.makeText(getApplicationContext(), "This account isn't exit", Toast.LENGTH_LONG).show();
	        }
			else {
				mAccountCorsor.moveToFirst();
				String passindatabase = mAccountCorsor.getString(mAccountCorsor.getColumnIndex("PASSWORD"));
				if (password.equals(passindatabase)){
					// Get id of account
					int id_account = mAccountCorsor.getInt(mAccountCorsor.getColumnIndex("ID_ACCOUNT")); //dbAdapter.getAccountId(username);

					// create Intend 
					Intent intent = new Intent (LoginActivity.this, HomeActivity.class);
					//Set data into intent
					intent.putExtra("ID_ACCOUNT", id_account);
					// Start Home Activity
					startActivity(intent);
					
					// End LoginActivity
					finish();
				}
				else{
					Toast.makeText(LoginActivity.this, "Password is not correct", Toast.LENGTH_LONG).show();
				}
			}
			mAccountCorsor.close();
		}
	}

	public void DeleteDB(){
		String msg = "";
		if (deleteDatabase("DARFTMONEYPOWER.db")==true){
			msg = "Delete database successful!";
		}
		else{
			msg = "Failed!";
		}
		Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	public void ResetDatabase() {
		AlertDialog.Builder b = new Builder(LoginActivity.this);
		b.setTitle("Reset Database");
		b.setMessage("Do you wanna Reset Database?");
		b.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DeleteDB();
				CreateDB();
				Toast.makeText(LoginActivity.this, "Database has been reset 😎", Toast.LENGTH_SHORT).show();
			}
		});
		b.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		b.show();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbAdapter.close();
	}
	
	//// create menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.login_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	//// Set even click for Menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.reset_database:
	            ResetDatabase();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
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
