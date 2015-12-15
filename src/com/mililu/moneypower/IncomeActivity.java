package com.mililu.moneypower;

import java.util.Calendar;

import com.mililu.moneypower.classobject.Diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class IncomeActivity extends Activity implements OnItemSelectedListener{
	private Spinner spinnerWallet, spinnerCategoryIncome;
	private Cursor walletsCursor, categoryIncomeCursor;
	private DataBaseAdapter dbAdapter;
	private Button btnSubmit, btnCreateCategory, btnBack, btnDate, btnTime;
	private EditText txtAmount, txtNotice, txtDate, txtTime;
	private int id_wallet, id_income, id_user;
	private int mYear, mMonth, mDay, mHour, mMinute;
	private TextView tvTittle, tvAmount, tvDes, tvWallet, tvCate, tvDate, tvTime;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_income);
	    
	    // Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    // Initializing font
	    Typeface light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    // Get id user
	    id_user = HomeActivity.id_user;
        // Get The Reference 
        spinnerWallet = (Spinner) findViewById(R.id.spn_income_wallet);
        spinnerCategoryIncome = (Spinner)findViewById(R.id.spn_income_danhmuc);
        btnSubmit = (Button)findViewById(R.id.btn_income_submit);
        btnBack = (Button)findViewById(R.id.btn_income_back);
        btnCreateCategory = (Button)findViewById(R.id.btn_income_createcategory);
        btnDate = (Button)findViewById(R.id.btn_income_date);
        btnTime = (Button)findViewById(R.id.btn_income_time);
        txtAmount = (EditText)findViewById(R.id.txt_income_amount);
        txtNotice = (EditText)findViewById(R.id.txt_income_notice);
        txtDate = (EditText)findViewById(R.id.txt_income_date);
        txtTime = (EditText)findViewById(R.id.txt_income_time);
        tvTittle = (TextView)findViewById(R.id.tv_income);
        tvAmount = (TextView)findViewById(R.id.tv_income_amount);
        tvDes = (TextView)findViewById(R.id.tv_income_notice);      
        tvWallet = (TextView)findViewById(R.id.tv_income_walletname);
        tvCate = (TextView)findViewById(R.id.tv_income_category);
        tvDate = (TextView)findViewById(R.id.tv_income_date);
        tvTime = (TextView)findViewById(R.id.tv_income_time);
        // Spinner click listener
        spinnerWallet.setOnItemSelectedListener(this);
        spinnerCategoryIncome.setOnItemSelectedListener(this);
        // Button click listener
        btnSubmit.setOnClickListener(new MyEvent()); 
        btnBack.setOnClickListener(new MyEvent());
        btnCreateCategory.setOnClickListener(new MyEvent());
        btnDate.setOnClickListener(new MyEvent());
        btnTime.setOnClickListener(new MyEvent());
        // Set Font
        txtAmount.setTypeface(light);
        txtNotice.setTypeface(light);
        txtDate.setTypeface(light);
        txtTime.setTypeface(light);
        tvTittle.setTypeface(light);
        tvAmount.setTypeface(light);
        tvDes.setTypeface(light);
        tvWallet.setTypeface(light);
        tvCate.setTypeface(light);
        tvDate.setTypeface(light);
        tvTime.setTypeface(light);
        // Get Date and Time
        getCurrentDate();
        getCurrentTime();
    }
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadSpinnerDataWallet();
        loadSpinnerDataCategoryIncome();
	}

	/**
     * thiet lap ngay thang nam hien tai
     */
	public void getCurrentDate(){
		Calendar cal=Calendar.getInstance();
		mDay=cal.get(Calendar.DAY_OF_MONTH);
		mMonth=(cal.get(Calendar.MONTH)+1);
		mYear=cal.get(Calendar.YEAR);
		// Set date to text box
		txtDate.setText(mDay+"-"+(mMonth)+"-"+mYear);
	}
    
	 /**
	 * thiet lap gio hien tai
	 */
	 public void getCurrentTime(){
		 Calendar cal=Calendar.getInstance();
		 mHour=cal.get(Calendar.HOUR_OF_DAY);
		 mMinute=cal.get(Calendar.MINUTE);
		 // Set time to text box
		 txtTime.setText(mHour+":"+(mMinute));
	 }
    /**
     * Function to load the spinner data from SQLite database
     * */    
    private void loadSpinnerDataWallet() {
        // Spinner Drop down cursor
        walletsCursor = dbAdapter.getListWalletOfUser(id_user);
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_WALLET" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item,walletsCursor, from, to, 0);

        // attaching data adapter to spinner
        spinnerWallet.setAdapter(dataAdapter);
        
    }
    /**
     * Load category income into spinner
     */
    private void loadSpinnerDataCategoryIncome() {
    	//dbAdapter = new DataBaseAdapter(getApplicationContext());
        // Spinner Drop down cursor
        categoryIncomeCursor = dbAdapter.getCategoryIncomeCursor();
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_INCOME" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, 
              android.R.layout.simple_spinner_dropdown_item,
              categoryIncomeCursor, from, to, 0);

        // attaching data adapter to spinner
        spinnerCategoryIncome.setAdapter(dataAdapter);
    }
    
    private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_income_submit)
			{
				DoInsertIncome();
			}
			else if(v.getId() == R.id.btn_income_back){
				IncomeActivity.this.finish();
			}
			else if(v.getId() == R.id.btn_income_date){
				showDialog(1);
			}
			else if(v.getId() == R.id.btn_income_time){
				showDialog(2);
			}
			else if(v.getId() == R.id.btn_income_createcategory){
				DoCreateCategoryIncome();
			}
		}
	}
    
    private void DoInsertIncome(){
    	String mAmount = txtAmount.getText().toString();
    	String mNotice = txtNotice.getText().toString();
    	String mTime = txtTime.getText().toString();
    	
    	//dbAdapter.open();
    	if(mAmount.equals("")||txtDate.getText().equals("")||txtTime.getText().equals("")){
    		Toast.makeText(getApplicationContext(), "Please insert amount, date and hour", Toast.LENGTH_LONG).show();
    	}
    	else {
    		long curentmoney = dbAdapter.getAmountOfWallet(id_wallet);
    		long newmoney = curentmoney + Long.valueOf(mAmount);
    		
    		Diary diary = new Diary();
    		diary.setAmount(Integer.valueOf(mAmount));
    		diary.setId_parent_category(id_income);
    		diary.setId_wallet(id_wallet);
    		diary.setId_category(id_income);
    		diary.setId_account(id_user);
    		diary.setDay(mDay);
    		diary.setMonth(mMonth);
    		diary.setYear(mYear);
    		diary.setTime(mTime);
    		diary.setType(1);
    		diary.setNotice(mNotice);
    		
    		dbAdapter.insertDiary(diary);
    		dbAdapter.updateWallet(id_wallet, newmoney);
    		Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
    		ClearTextBox();
    	}
    }
    private void DoCreateCategoryIncome(){
    	Intent intent = new Intent (IncomeActivity.this, CreateCategoryIncomeActivity.class);
		startActivity(intent);
    }
    
    private void ClearTextBox(){
    	txtAmount.setText("");
    	txtNotice.setText("");
    	getCurrentDate();
    	getCurrentTime();
    }
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner spinner = (Spinner) parent;
		if(spinner.getId() == R.id.spn_income_wallet)
		{
			walletsCursor.moveToPosition(position);
			id_wallet = walletsCursor.getInt(walletsCursor.getColumnIndexOrThrow("_id"));
		}
		else if(spinner.getId() == R.id.spn_income_danhmuc)
		{
			categoryIncomeCursor.moveToPosition(position);
			id_income = categoryIncomeCursor.getInt(categoryIncomeCursor.getColumnIndexOrThrow("_id"));
		}
	}
	
	@Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    
	/**
	 * xu ly DatePickerDialog
	 */
	 private DatePickerDialog.OnDateSetListener dateChange = new OnDateSetListener() {
		 @Override
		 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			 // TODO Auto-generated method stub
			 mYear=year;
			 mMonth=monthOfYear+1;
			 mDay=dayOfMonth;
			 txtDate.setText(mDay+"-"+(mMonth)+"-"+mYear);
		 }
	 };
	 private TimePickerDialog.OnTimeSetListener timeChange = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			mHour = hourOfDay;
			mMinute = minute;
			txtTime.setText(mHour + ":" + mMinute);
		}
	};
	 
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if(id==1){
			return new DatePickerDialog(this, dateChange, mYear, mMonth-1, mDay);
		}
		else if(id==2){
			return new TimePickerDialog(this, timeChange, mHour, mMinute, false );
		}
		return null;
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
