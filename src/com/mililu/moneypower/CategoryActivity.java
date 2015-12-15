package com.mililu.moneypower;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class CategoryActivity extends TabActivity{
	private TabHost tabHost;
	private Button btnBack,btnCreate;
	private TabSpec incomespec, expendspec;
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.activity_category);
	    
	    tabHost = getTabHost();
	     
	    // Tab for Income
	    incomespec = tabHost.newTabSpec("income");
	    incomespec.setIndicator("Income", getResources().getDrawable(R.drawable.icon_info));
	    Intent incomesIntent = new Intent(this, CategoryIncomeActivity.class);
	    incomespec.setContent(incomesIntent);
	     
	    // Tab for Expenditure
	    expendspec = tabHost.newTabSpec("expend");
	    expendspec.setIndicator ("Expenditure", getResources().getDrawable(R.drawable.ic_launcher)); // setting Title and Icon for the Tab
	    Intent expendIntent = new Intent(this, CategoryExpenditureActivity.class);
	    expendspec.setContent(expendIntent);
	     
	    // Adding all TabSpec to TabHost
	    tabHost.addTab(incomespec); // Adding income tab (0)
	    tabHost.addTab(expendspec); // Adding expenditure tab (1)
	    
		// Get The Reference Of View
	    btnBack=(Button)findViewById(R.id.btn_category_back);
	    btnCreate=(Button)findViewById(R.id.btn_category_create);
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnCreate.setOnClickListener(new MyEvent());
	}
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_category_back)
			{
				CategoryActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_category_create)
			{
				if (tabHost.getCurrentTab() == 0){
					// Open activity create Income Category
					Intent intent = new Intent (getApplicationContext(), CreateCategoryIncomeActivity.class);
					startActivity(intent);
				}
				else if (tabHost.getCurrentTab() == 1){
					Toast.makeText(getApplicationContext(), "under developding", Toast.LENGTH_SHORT).show();
				}					
			}
		}
	}
}

