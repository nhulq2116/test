package com.mililu.moneypower;

import com.mililu.moneypower.classobject.Income;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class EditCategoryIncomeActivity extends Activity {
	private DataBaseAdapter dbAdapter;
	private Button btnBack, btnInsert;
	private EditText txtNameIncome;
	private int id_income;
	private Cursor income;
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
	    
	    Intent intent = getIntent();
	    id_income = intent.getIntExtra("id_income", 0); 
	    
	    income = dbAdapter.getInforCategoryIncome(id_income);
	    income.moveToFirst();
	    txtNameIncome.setText(income.getString(income.getColumnIndexOrThrow("NAME_INCOME")));
	}

	public class MyEvent implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_createcategoryincome_back)
			{
				EditCategoryIncomeActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_createcategoryincome_OK) {
					UpdateCategoryIncome();
			}
		}
	}
	
	private void UpdateCategoryIncome(){
		Income income = new Income();
		income.setId(id_income);
		income.setName(txtNameIncome.getText().toString());

		// check if any of the fields are vaccant
		if(income.getName().equals(""))
		{
				Toast.makeText(this, "Please write name of income", Toast.LENGTH_LONG).show();
				return;
		}
		else{
			// Save the Data in Database
			dbAdapter.updateCategoryIncome(income);
			Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
			EditCategoryIncomeActivity.this.finish();
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
