package com.mililu.moneypower;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class TransferActivity extends Activity implements OnItemSelectedListener{
	private Spinner spinnerWalletFrom, spinnerWalletTo;
	private Cursor walletsCursor;
	private DataBaseAdapter dbAdapter;
	private Button btnSubmit, btnBack;
	private EditText txtAmount;
	private int id_wallet_from, id_wallet_to, id_user;
	private TextView tvTittle, tvAmount, tvWalletFrom, tvWalletTo;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transfer);
	    
	    // Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    // Initializing font
	    Typeface light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    // Get id user
	    id_user = HomeActivity.id_user;
        // Get The Reference 
        spinnerWalletFrom = (Spinner) findViewById(R.id.spn_transfer_from);
        spinnerWalletTo = (Spinner) findViewById(R.id.spn_transfer_to);
        btnSubmit = (Button)findViewById(R.id.btn_transfer_submit);
        btnBack = (Button)findViewById(R.id.btn_transfer_back);
        txtAmount = (EditText)findViewById(R.id.txt_transfer_amount);
        tvTittle = (TextView)findViewById(R.id.tv_transfer_title);
        tvAmount = (TextView)findViewById(R.id.tv_transfer_amount);
        tvWalletFrom = (TextView)findViewById(R.id.tv_transfer_from);
        tvWalletTo = (TextView)findViewById(R.id.tv_transfer_to);
        // Spinner click listener
        spinnerWalletFrom.setOnItemSelectedListener(this);
        spinnerWalletTo.setOnItemSelectedListener(this);
        // Button click listener
        btnSubmit.setOnClickListener(new MyEvent()); 
        btnBack.setOnClickListener(new MyEvent());
        // Set Font
        txtAmount.setTypeface(light);
        tvTittle.setTypeface(light);
        tvAmount.setTypeface(light);
        tvWalletFrom.setTypeface(light);
        tvWalletTo.setTypeface(light);
    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadSpinnerDataWallet();
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
        spinnerWalletFrom.setAdapter(dataAdapter);
        spinnerWalletTo.setAdapter(dataAdapter);
        
    }
    private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_transfer_submit)
			{
				String amount = txtAmount.getText().toString();
				if (amount.equals("")){
					Notify("Please fill in amount");
				}
				else if (Long.valueOf(amount)<=0){
					Notify("Invalid amount");
				}
				else {
					DoInsertTransfer(id_wallet_from, id_wallet_to, Long.valueOf(amount));
				}
			}
			else if(v.getId() == R.id.btn_transfer_back){
				TransferActivity.this.finish();
			}
		}
	}
    
    private void Notify (String mess){
    	Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_SHORT).show();
    }
    
    private void DoInsertTransfer(int id_wallet_from, int id_wallet_to, long amount){
    	if (id_wallet_from == id_wallet_to){
    		Notify("You don't have to transfer to the same wallet");
    	}
    	else {
    		long amount_wallet_from = dbAdapter.getAmountOfWallet(id_wallet_from);
    		if (amount > amount_wallet_from){
    			Notify("You don't have enough money");
    		}
    		else {
    			long amount_wallet_to = dbAdapter.getAmountOfWallet(id_wallet_to);
    			
    			if(dbAdapter.updateWallet(id_wallet_from, (amount_wallet_from - amount))){
    				if(dbAdapter.updateWallet(id_wallet_to, (amount_wallet_to + amount))){
    					Notify("Success");
    					ClearTextBox();
    				}
    				else {
    					Notify("Can't update wallet ");
    				}
    			}
    			else {
    				Notify("Can't update wallet");
    			}
    		}
    	}
    }
    private void ClearTextBox(){
    	txtAmount.setText("");
    }
    
    @Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner spinner = (Spinner) parent;
		if(spinner.getId() == R.id.spn_transfer_from)
		{
			walletsCursor.moveToPosition(position);
			id_wallet_from = walletsCursor.getInt(walletsCursor.getColumnIndexOrThrow("_id"));
		}
		else if(spinner.getId() == R.id.spn_transfer_to)
		{
			walletsCursor.moveToPosition(position);
			id_wallet_to = walletsCursor.getInt(walletsCursor.getColumnIndexOrThrow("_id"));
		}
	}
	
	@Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
	
	

}
