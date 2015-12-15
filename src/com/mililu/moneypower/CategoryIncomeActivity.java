package com.mililu.moneypower;

import java.util.ArrayList;
import java.util.List;

import com.mililu.moneypower.classobject.Income;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

public class CategoryIncomeActivity extends Activity{
	private DataBaseAdapter dbAdapter;
	private List<Income>list_income ;
	static ArrayAdapterCategoryIncome aaCategoryIncome;
	private ListView lvCategoryIncome;
	private Cursor cursorCategoryIncome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_category_income);
		
		// Get The Reference Of View
	    lvCategoryIncome = (ListView)findViewById(R.id.lv_categoryincome_listincome);
		
	    // khoi tao gia tri
	    list_income = new ArrayList<Income>();
	    
	    // Set font
        //Typeface font_light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        //Typeface font_bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
        //txtTittle.setTypeface(font_light);
        
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Set OnItemClick Listener on listview
	    //lvDiary.setOnItemClickListener(new MyEventItemOnClick());
	    
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		ShowListIncome();
	}
	
	private void ShowListIncome(){
		cursorCategoryIncome = dbAdapter.getCategoryIncomeCursor();
		if (cursorCategoryIncome.getCount()<1){
			Toast.makeText(CategoryIncomeActivity.this, "You don't have any category !!", Toast.LENGTH_LONG).show();
		}
		else{
			list_income.clear();
			cursorCategoryIncome.moveToFirst();
			while(!cursorCategoryIncome.isAfterLast()){
				Income data = new Income();
				data.setId(cursorCategoryIncome.getInt(cursorCategoryIncome.getColumnIndexOrThrow("ID_INC")));
				data.setName(cursorCategoryIncome.getString(cursorCategoryIncome.getColumnIndexOrThrow("NAME_INCOME")));
				list_income.add(data);
				cursorCategoryIncome.moveToNext();
		 	}
			cursorCategoryIncome.close();
			aaCategoryIncome = new ArrayAdapterCategoryIncome(CategoryIncomeActivity.this, R.layout.layout_for_category_item, list_income);
			lvCategoryIncome.setAdapter(aaCategoryIncome);
		}
	}
}
