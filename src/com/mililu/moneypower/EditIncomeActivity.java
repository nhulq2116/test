package com.mililu.moneypower;

import com.mililu.moneypower.classobject.Diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditIncomeActivity extends Activity implements OnItemSelectedListener{
	private Spinner spinnerWallet, spinnerCategoryIncome;
	private Cursor walletsCursor, categoryIncomeCursor, diaryCursor;
	private DataBaseAdapter dbAdapter;
	private Button btnSubmit, btnCreateCategory, btnBack, btnDate, btnTime;
	private EditText txtAmount, txtNotice, txtDate, txtTime;
	private int oldamount;
	private int  id_diary, id_wallet, id_income, id_curent_user;
	private int mYear, mMonth, mDay, mHour, mMinute;
	private TextView tvTittle, tvAmount, tvDes, tvWallet, tvCate, tvDate, tvTime;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_income);
        
        id_curent_user = HomeActivity.id_user;
        
        Intent intent = getIntent();
	    //Bundle bundle = intent.getBundleExtra("DATA_INCOME");
	    id_diary = intent.getIntExtra("id_diary", 0);
	    // Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // khoi tao font
	    Typeface light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    
        // Set Reference 
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
	    spinnerWallet = (Spinner) findViewById(R.id.spn_income_wallet);
        spinnerCategoryIncome = (Spinner)findViewById(R.id.spn_income_danhmuc);
        
        // Set click listener
        spinnerWallet.setOnItemSelectedListener(this);
        spinnerCategoryIncome.setOnItemSelectedListener(this);
        btnSubmit.setOnClickListener(new MyEvent()); 
        btnBack.setOnClickListener(new MyEvent());
        btnCreateCategory.setOnClickListener(new MyEvent());
        btnDate.setOnClickListener(new MyEvent());
        btnTime.setOnClickListener(new MyEvent());
        // Set font
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
	}
    
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadSpinnerDataWallet();
        loadSpinnerDataCategoryIncome();
        getInforDiary();
	}
    
    private void getInforDiary(){
    	diaryCursor = dbAdapter.getInforDiary(id_diary);
    	diaryCursor.moveToFirst();
    	oldamount = diaryCursor.getInt(diaryCursor.getColumnIndexOrThrow("AMOUNT"));
    	String notice = diaryCursor.getString(diaryCursor.getColumnIndexOrThrow("NOTICE"));
    	mDay = diaryCursor.getInt(diaryCursor.getColumnIndexOrThrow("DAY"));
    	mMonth = diaryCursor.getInt(diaryCursor.getColumnIndexOrThrow("MONTH"));
    	mYear = diaryCursor.getInt(diaryCursor.getColumnIndexOrThrow("YEAR"));
    	String time = diaryCursor.getString(diaryCursor.getColumnIndexOrThrow("TIME"));
    	
    	txtAmount.setText(String.valueOf(oldamount));
    	txtDate.setText(mDay+"/"+mMonth+"/"+mYear);
    	txtTime.setText(time);
    	txtNotice.setText(notice);
    }
    private void loadSpinnerDataWallet() {
    	//dbAdapter = new DataBaseAdapter(getApplicationContext());

        // Spinner Drop down cursor
    	walletsCursor = dbAdapter.getListWalletOfUser(id_curent_user);
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_WALLET" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item,walletsCursor, from, to, 0);

        // attaching data adapter to spinner
        spinnerWallet.setAdapter(dataAdapter);
        
    }
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
				DoUpdateIncome();
			}
			else if(v.getId() == R.id.btn_income_back){
				EditIncomeActivity.this.finish();
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
    
    private void DoUpdateIncome(){
    	String newAmount = txtAmount.getText().toString();
    	String newNotice = txtNotice.getText().toString();
    	String newTime = txtTime.getText().toString();
    	
    	//dbAdapter.open();
    	if(newAmount.equals("")||txtDate.getText().equals("")||txtTime.getText().equals("")){
    		Toast.makeText(getApplicationContext(), "Please insert amount, date and hour", Toast.LENGTH_LONG).show();
    	}
    	else {
    		long curentmoney = dbAdapter.getAmountOfWallet(id_wallet);
    		long newmoney = 0;
    		long balancemoney = Integer.valueOf(newAmount) - oldamount;
    		if (balancemoney == 0){
    			newmoney = curentmoney;
    		}
    		else {
    			newmoney = curentmoney + balancemoney;
    		}
    		
    		Diary diary = new Diary();
    		diary.setId_diary(id_diary);
    		diary.setAmount(Integer.valueOf(newAmount));
    		diary.setId_parent_category(id_income);
    		diary.setId_wallet(id_wallet);
    		diary.setId_category(id_income);
    		diary.setId_account(id_curent_user);
    		diary.setDay(mDay);
    		diary.setMonth(mMonth);
    		diary.setYear(mYear);
    		diary.setTime(newTime);
    		diary.setType(1);
    		diary.setNotice(newNotice);
    		
    		if(dbAdapter.updateDiary(diary)){
    			dbAdapter.updateWallet(id_wallet, newmoney);
    			Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
    			EditIncomeActivity.this.finish();
    		}
    		else{
    			Toast.makeText(getApplicationContext(), "Something wrong!!!", Toast.LENGTH_LONG).show();
    			EditIncomeActivity.this.finish();
    		}
    	}
    }
    
    private void DoCreateCategoryIncome(){
    	Intent intent = new Intent (EditIncomeActivity.this, CreateCategoryIncomeActivity.class);
		startActivity(intent);
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
	
}
