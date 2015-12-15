package com.mililu.moneypower;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCategoryIncomeActivity extends Activity {
	private DataBaseAdapter dbAdapter;
	private Button btnBack, btnInsert;
	private EditText txtNameIncome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_create_category_income);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Get The Reference Of Views
	    txtNameIncome = (EditText)findViewById(R.id.txt_createcategoryincome_nameincome);
	    btnBack=(Button)findViewById(R.id.btn_createcategoryincome_back);
	    btnInsert=(Button)findViewById(R.id.btn_createcategoryincome_OK);
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnInsert.setOnClickListener(new MyEvent());
	    
	}

	public class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_createcategoryincome_back)
			{
				CreateCategoryIncomeActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_createcategoryincome_OK) {
					InsertCategoryIncome();
			}
		}
	}
	
	private void InsertCategoryIncome(){
		String nameincome = txtNameIncome.getText().toString();

		// check if any of the fields are vaccant
		if(nameincome.equals(""))
		{
				Toast.makeText(this, "Please write name of income", Toast.LENGTH_LONG).show();
				return;
		}
		else{
			// Save the Data in Database
			dbAdapter.insertCategoryIncome(nameincome);
			Toast.makeText(getApplicationContext(), "Create Successful", Toast.LENGTH_LONG).show();
			CreateCategoryIncomeActivity.this.finish();
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