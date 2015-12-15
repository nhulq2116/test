package com.mililu.moneypower;

import com.mililu.moneypower.classobject.Diary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditExpenseActivity extends Activity implements OnItemSelectedListener{
	private Spinner spnWallet, spnCategoryExpenDetail;
    private Cursor walletsCursor, categoryExpenDetailCursor, diaryCursor;
    private DataBaseAdapter dbAdapter;
    private Button btnSubmit, btnCreateCategory, btnBack, btnDate, btnTime;
    private EditText txtAmount, txtNotice, txtDate, txtTime;
    private int id_wallet, id_expen_parent, id_user, id_expen_detail, id_diary;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private long oldamount; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_expenditure);
        // Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    Intent intent = getIntent();
	    id_diary = intent.getIntExtra("id_diary", 0);
	    // Get id user
	    id_user = HomeActivity.id_user;
        // Get Reference 
        spnWallet = (Spinner) findViewById(R.id.spn_expen_wallet);
        spnCategoryExpenDetail = (Spinner)findViewById(R.id.spn_expen_danhmuccon);
        btnSubmit = (Button)findViewById(R.id.btn_expen_submit);
        btnBack = (Button)findViewById(R.id.btn_expen_back);
        btnCreateCategory = (Button)findViewById(R.id.btn_expen_createcategory);
        btnDate = (Button)findViewById(R.id.btn_expen_date);
        btnTime = (Button)findViewById(R.id.btn_expen_time);
        txtAmount = (EditText)findViewById(R.id.txt_expen_amount);
        txtNotice = (EditText)findViewById(R.id.txt_expen_notice);
        txtDate = (EditText)findViewById(R.id.txt_expen_date);
        txtTime = (EditText)findViewById(R.id.txt_expen_time);
        // Spinner click listener
        spnWallet.setOnItemSelectedListener(this);
        spnCategoryExpenDetail.setOnItemSelectedListener(this);
        
        // Button click listener
        btnSubmit.setOnClickListener(new MyEvent()); 
        btnBack.setOnClickListener(new MyEvent());
        btnCreateCategory.setOnClickListener(new MyEvent());
        btnDate.setOnClickListener(new MyEvent());
        btnTime.setOnClickListener(new MyEvent());
    }
    
    @Override
	protected void onStart() {
		super.onStart();
		loadSpinnerDataWallet();
        loadSpinnerDataCategoryExpenDetail();
        getInforDiary();
	}

	private void getInforDiary(){
    	diaryCursor = dbAdapter.getInforDiary(id_diary);
    	diaryCursor.moveToFirst();
    	oldamount = diaryCursor.getLong(diaryCursor.getColumnIndexOrThrow("AMOUNT"));
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
        // Spinner Drop down cursor
        walletsCursor = dbAdapter.getListWalletOfUser(id_user);
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_WALLET" };
        int[] to = { android.R.id.text1 };
        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_dropdown_item,walletsCursor, from, to, 0);
        // attaching data adapter to spinner
        spnWallet.setAdapter(dataAdapter);
    }
    private void loadSpinnerDataCategoryExpenDetail() {
        // Spinner Drop down cursor
        categoryExpenDetailCursor = dbAdapter.getAllCategoryExpenDetail();
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_EXP_DET" };
        int[] to = { android.R.id.text1 };
        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, 
              android.R.layout.simple_spinner_dropdown_item,
              categoryExpenDetailCursor, from, to, 0);
        // attaching data adapter to spinner
        spnCategoryExpenDetail.setAdapter(dataAdapter);
    }
    private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_expen_submit)
			{
				String mAmount = txtAmount.getText().toString();
		    	String mNotice = txtNotice.getText().toString();
		    	String mTime = txtTime.getText().toString();
		    	if (mAmount.equals("")){
		    		Notify("Please fill in money");
		    	}
		    	else if(Long.valueOf(mAmount)<=0){
		    		Notify("Invalid money");
		    	}
		    	else {
		    		DoUpdatetExpense(Long.valueOf(mAmount), mNotice, mTime);
		    	}
			}
			else if(v.getId() == R.id.btn_expen_back){
				EditExpenseActivity.this.finish();
			}
			else if(v.getId() == R.id.btn_expen_date){
				showDialog(1);
			}
			else if(v.getId() == R.id.btn_expen_time){
				showDialog(2);
			}
		}
	}
    private void DoUpdatetExpense(long amount, String notice, String time){
    	long amount_wallet = dbAdapter.getAmountOfWallet(id_wallet);
		if (amount_wallet < amount){
			Notify("You don't have enough money");
		}
		else {
			Diary diary = new Diary();
			diary.setId_diary(id_diary);
			diary.setAmount(amount);
			diary.setId_wallet(id_wallet);
			diary.setId_parent_category(id_expen_parent);
			diary.setId_category(id_expen_detail);
			diary.setId_account(id_user);
			diary.setDay(mDay);
			diary.setMonth(mMonth);
			diary.setYear(mYear);
			diary.setTime(time);
			diary.setType(2); // 2 = chi
			diary.setNotice(notice);
			if (dbAdapter.updateDiary(diary)){
				long newamount_wallet = 0;
				long balancemoney = amount - oldamount;
				if (balancemoney == 0){
					newamount_wallet = amount_wallet;
				}
				else {
					newamount_wallet = amount_wallet - balancemoney;
				}
				if (dbAdapter.updateWallet(id_wallet, newamount_wallet)){
					Notify("Succes");
					ClearTextBox();
				}
				else {
					Notify("Can't update Wallet");
				}
			}
			else {
				Notify("Can't update diary");
			}
		}
    }
    private void ClearTextBox(){
    	txtAmount.setText("");
    	txtNotice.setText("");
    }
    private void Notify(String mess){
    	Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
    }
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner spinner = (Spinner) parent;
		if(spinner.getId() == R.id.spn_expen_wallet)
		{
			walletsCursor.moveToPosition(position);
			id_wallet = walletsCursor.getInt(walletsCursor.getColumnIndexOrThrow("_id"));
		}
		else if(spinner.getId() == R.id.spn_expen_danhmuccon)
		{
			categoryExpenDetailCursor.moveToPosition(position);
			id_expen_detail = categoryExpenDetailCursor.getInt(categoryExpenDetailCursor.getColumnIndexOrThrow("_id"));
			id_expen_parent = categoryExpenDetailCursor.getInt(categoryExpenDetailCursor.getColumnIndexOrThrow("ID_EXP"));
		}
	}
	
	@Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
	private DatePickerDialog.OnDateSetListener dateChange = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear=year;
			mMonth=monthOfYear + 1;
			mDay=dayOfMonth;
			txtDate.setText(mDay+"-"+(mMonth)+"-"+mYear);
		}
	};
	private TimePickerDialog.OnTimeSetListener timeChange = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			txtTime.setText(mHour + ":" + mMinute);
		}
	};
	@Override
	protected Dialog onCreateDialog(int id) {
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
