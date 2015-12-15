package com.mililu.moneypower;

import java.util.Calendar;

import com.mililu.moneypower.classobject.DiaryDebt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DebtActivity extends Activity implements OnItemSelectedListener {

	private Spinner spinnerWallet, spinnerDebtor;
	private Cursor walletsCursor, debtorCursor;
	private DataBaseAdapter dbAdapter;
	private Button btnSubmit, btnCreateDebtor;
	private EditText txtAmount, txtNotice, txtDate;
	private RadioButton rdbtBorrow, rdbtLend;
	private RadioGroup rgType;
	private int id_wallet, id_debtor, id_user;
	private int mYear, mMonth, mDay;
	private TextView tvAmount, tvDes, tvWallet, tvDebtor, tvDate, tvType;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_debt);
	    
	    // Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    // Initializing font
	    Typeface light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    // Get id user
	    id_user = HomeActivity.id_user;
        // Get The Reference 
        spinnerWallet = (Spinner) findViewById(R.id.spn_debt_wallet);
        spinnerDebtor = (Spinner)findViewById(R.id.spn_debt_debtor);
        btnSubmit = (Button)findViewById(R.id.btn_debt_submit);
        btnCreateDebtor = (Button)findViewById(R.id.btn_debt_createdebtor);
        rgType = (RadioGroup)findViewById(R.id.rg_debt_type);
        rdbtBorrow = (RadioButton)findViewById(R.id.rdbt_debt_borrow);
        rdbtLend = (RadioButton)findViewById(R.id.rdbt_debt_lend);
        txtAmount = (EditText)findViewById(R.id.txt_debt_amount);
        txtNotice = (EditText)findViewById(R.id.txt_debt_notice);
        txtDate = (EditText)findViewById(R.id.txt_debt_date);
        tvAmount = (TextView)findViewById(R.id.tv_debt_amount);
        tvDes = (TextView)findViewById(R.id.tv_debt_notice);      
        tvWallet = (TextView)findViewById(R.id.tv_debt_wallet);
        tvDebtor = (TextView)findViewById(R.id.tv_debt_debtor);
        tvDate = (TextView)findViewById(R.id.tv_debt_date);
        tvType = (TextView)findViewById(R.id.tv_debt_type);
        // Spinner click listener
        spinnerWallet.setOnItemSelectedListener(this);
        spinnerDebtor.setOnItemSelectedListener(this);
        // Button click listener
        btnSubmit.setOnClickListener(new MyEvent());
        btnCreateDebtor.setOnClickListener(new MyEvent());
        txtNotice.setOnClickListener(new MyEvent());
        // Set Font
        txtAmount.setTypeface(light);
        txtNotice.setTypeface(light);
        txtDate.setTypeface(light);
        tvAmount.setTypeface(light);
        tvDes.setTypeface(light);
        tvWallet.setTypeface(light);
        tvDate.setTypeface(light);
        tvDebtor.setTypeface(light);
        tvType.setTypeface(light);
        // Get Date and Time
        getCurrentDate();
    }
    
    @Override
	protected void onStart() {
		super.onStart();
		loadSpinnerDataWallet();
        loadSpinnerDataDebtor();
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
    private void loadSpinnerDataDebtor() {
    	//dbAdapter = new DataBaseAdapter(getApplicationContext());
        // Spinner Drop down cursor
        debtorCursor = dbAdapter.getDebtorCursor(id_user);
        // map the cursor column names to the TextView ids in the layout
        String[] from = { "NAME_DEBTOR" };
        int[] to = { android.R.id.text1 };

        // Creating adapter for spinner
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, 
              android.R.layout.simple_spinner_dropdown_item,
              debtorCursor, from, to, 0);

        // attaching data adapter to spinner
        spinnerDebtor.setAdapter(dataAdapter);
    }
    
    private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if(v.getId() == R.id.btn_debt_submit){
				int selectedID = rgType.getCheckedRadioButtonId();
				
				if (selectedID == rdbtBorrow.getId()){
					DoInsertDebt(1); // vay
				}
				else if(selectedID == rdbtLend.getId()){
					DoInsertDebt(2); // cho vay
				}
				else {
					Toast.makeText(getApplicationContext(), "Please select type", Toast.LENGTH_LONG).show();
				}
			}
			else if(v.getId() == R.id.txt_debt_date){
				showDialog(1);
			}
			else if(v.getId() == R.id.btn_debt_createdebtor){
				DoCreateDeptor();
			}
		}
	}
    
    private void DoInsertDebt(int type){
    	String mAmount = txtAmount.getText().toString();
    	String mNotice = txtNotice.getText().toString();
    	
    	//dbAdapter.open();
    	if(mAmount.equals("")||txtDate.getText().equals("")){
    		Toast.makeText(getApplicationContext(), "Please insert amount, date and hour", Toast.LENGTH_LONG).show();
    	}
    	else {
    		if (Long.valueOf(mAmount) > 0){
	    		long curentmoney = dbAdapter.getAmountOfWallet(id_wallet);
	    		if ((type == 1) || (type == 2 && curentmoney > Long.valueOf(mAmount))){
		    		DiaryDebt diary = new DiaryDebt();
		    		diary.setAmount(Long.valueOf(mAmount));
		    		diary.setId_debtor(id_debtor);
		    		diary.setId_account(id_user);
		    		diary.setId_wallet(id_wallet);
		    		diary.setType(type); // 1: vay, 2: cho vay
		    		diary.setDay(mDay);
		    		diary.setMonth(mMonth);
		    		diary.setYear(mYear);
		    		diary.setNotice(mNotice);
		    		if(dbAdapter.insertDebtDiary(diary)){
		    			long newmoney = 0;
		    			if (type == 1){
		    				newmoney = curentmoney + Long.valueOf(mAmount);
		    			}
		    			else if (type == 2){
		    				newmoney = curentmoney - Long.valueOf(mAmount);
		    			}
		    			dbAdapter.updateWallet(id_wallet, newmoney);
		    			Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
		    		}
		    		else {
		    			Toast.makeText(getApplicationContext(), "Can't write into database", Toast.LENGTH_SHORT).show();
		    		}
		    		ClearTextBox();
	    		}
	    		else if (type == 2 && curentmoney < Long.valueOf(mAmount)) {
	    			Toast.makeText(getApplicationContext(), "You Don't Have Enougn Money", Toast.LENGTH_SHORT).show();
	    		}
	    		else {
	    			Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
	    		}
    		}
    		else {
    			Toast.makeText(getApplicationContext(), "Invalid Amount", Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    private void DoCreateDeptor(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Create Debtor");

    	// Set up the input
    	final EditText input = new EditText(this);
    	// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    	input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
    	input.setHint("Debtor Name");
    	builder.setView(input);

    	// Set up the buttons
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        String name = input.getText().toString();
    	        if (dbAdapter.insertDebtor(name, id_user)){
    	        	Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
    	        	loadSpinnerDataDebtor();
    	        }
    	        else{
    	        	Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
    	        }
    	        
    	    }
    	});
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        dialog.cancel();
    	    }
    	});
    	builder.show();
    }
	
	private void ClearTextBox(){
    	txtAmount.setText("");
    	txtNotice.setText("");
    	getCurrentDate();
    }
    
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner spinner = (Spinner) parent;
		if(spinner.getId() == R.id.spn_debt_wallet)
		{
			walletsCursor.moveToPosition(position);
			id_wallet = walletsCursor.getInt(walletsCursor.getColumnIndexOrThrow("_id"));
			//name_wallet = walletsCursor.getString(walletsCursor.getColumnIndexOrThrow("NAME_WALLET"));
		}
		else if(spinner.getId() == R.id.spn_debt_debtor)
		{
			debtorCursor.moveToPosition(position);
			id_debtor = debtorCursor.getInt(debtorCursor.getColumnIndexOrThrow("ID_DEBTOR"));
			//name_debtor = debtorCursor.getString(debtorCursor.getColumnIndexOrThrow("NAME_DEBTOR"));
		}
	}
	
	@Override
    public void onNothingSelected(AdapterView<?> arg0) {
		
    }
    
	/**
	 * xu ly DatePickerDialog
	 */
	 private DatePickerDialog.OnDateSetListener dateChange = new OnDateSetListener() {
		 @Override
		 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			 mYear=year;
			 mMonth=monthOfYear+1;
			 mDay=dayOfMonth;
			 txtDate.setText(mDay+"-"+(mMonth)+"-"+mYear);
		 }
	 };
 
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id==1){
			return new DatePickerDialog(this, dateChange, mYear, mMonth-1, mDay);
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
