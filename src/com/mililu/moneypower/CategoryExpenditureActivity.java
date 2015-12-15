package com.mililu.moneypower;

import java.util.ArrayList;

import com.mililu.moneypower.classobject.ExpenditureDetail;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class CategoryExpenditureActivity extends ExpandableListActivity{
	
	private ArrayList<String> parentItems = new ArrayList<String>();
	private ArrayList<Object> childItems = new ArrayList<Object>();
	
	int id_user;
	ArrayList<ExpenditureDetail> list_child; 
	
	ExpandableListView expandableList;
	
	DataBaseAdapter dbAdapter;
	AdapterCategoryExpend aCategoryExpend;
	Cursor cursorCtgExpendParent, cursorCtgExpendChild;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// this is not really  necessary as ExpandableListActivity contains an ExpandableList
		//setContentView(R.layout.main);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
		expandableList = getExpandableListView(); // you can use (ExpandableListView) findViewById(R.id.list)
		
		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		setDATA();
		
		aCategoryExpend = new AdapterCategoryExpend(parentItems, childItems);
		
		aCategoryExpend.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableList.setAdapter(aCategoryExpend);
		expandableList.setOnChildClickListener(this);
		
	    id_user = HomeActivity.id_user;
		
		
		
		
		/// ham mo toan bo group
		for (int i = 0; i < aCategoryExpend.getGroupCount(); i++){
			expandableList.expandGroup(i);
		}
	}
	
	public void setDATA(){
		cursorCtgExpendParent = dbAdapter.getCategoryExpenCursor();
		if (cursorCtgExpendParent.getCount()<1){
			Toast.makeText(getApplicationContext(), "You don't have any category Expenditure !!", Toast.LENGTH_SHORT).show();
		}
		else {
			cursorCtgExpendParent.moveToFirst();
			while(!cursorCtgExpendParent.isAfterLast()){
				int id = cursorCtgExpendParent.getInt(cursorCtgExpendParent.getColumnIndexOrThrow("ID_EXP"));
				String name = cursorCtgExpendParent.getString(cursorCtgExpendParent.getColumnIndexOrThrow("NAME_EXP"));
				
				parentItems.add(name);
				
				cursorCtgExpendChild = dbAdapter.getCategoryExpenDetail(id);
				cursorCtgExpendChild.moveToFirst();
				
				list_child = new ArrayList<ExpenditureDetail>();
				while(!cursorCtgExpendChild.isAfterLast()){
					ExpenditureDetail itemData = new ExpenditureDetail();
					itemData.setId_exp_det(cursorCtgExpendChild.getInt(cursorCtgExpendChild.getColumnIndexOrThrow("ID_EXP_DET")));
					itemData.setId_exp(cursorCtgExpendChild.getInt(cursorCtgExpendChild.getColumnIndexOrThrow("ID_EXP")));
					itemData.setName_expend(cursorCtgExpendChild.getString(cursorCtgExpendChild.getColumnIndexOrThrow("NAME_EXP_DET")));
					list_child.add(itemData);
					cursorCtgExpendChild.moveToNext();
				}
				childItems.add(list_child);
				cursorCtgExpendParent.moveToNext();
			}
			//Toast.makeText(getApplicationContext(), "Fuck that bitch !!", Toast.LENGTH_SHORT).show();
		}
	}
}
