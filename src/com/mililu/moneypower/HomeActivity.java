package com.mililu.moneypower;

import java.util.ArrayList;

import com.mililu.moneypower.classobject.NavItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class HomeActivity extends Activity{

	private ListView mDrawerList;
	private RelativeLayout mDrawerPane;
	private DrawerLayout mDrawerLayout;
	 
	private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
	
	private Button btnMenu, btnWallet, btnIncome, btnExpenditure, btnStatistic;
	private TextView lableFullname, lableUsername;
	private DataBaseAdapter dbAdapter;
	public static int id_user;
	private String username_current_user, fullname_current_user;
	private Cursor accountCursor;
	private Intent intent_income, intent_expend, intent_wallet, intent_stattistic, intent_diary, intent_category, intent_transfer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);
		
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    Intent intent = getIntent();
	    // Get data from intent
	    id_user = intent.getIntExtra("ID_ACCOUNT", -1);
	    
	    // Khoi tao font
	    Typeface font_bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
	    Typeface font_light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    Typeface font_italic = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLDITALIC.TTF");
	    
	    // Get The Reference 
	    btnMenu = (Button)findViewById(R.id.btn_home_menu);
	    btnWallet=(Button)findViewById(R.id.btn_home_wallet);
	    btnIncome=(Button)findViewById(R.id.btn_home_income);
	    btnExpenditure=(Button)findViewById(R.id.btn_home_expenditure);	
	    btnStatistic=(Button)findViewById(R.id.btn_home_statistic);
	    lableFullname = (TextView)findViewById(R.id.tv_home_fullname) ;
	    lableUsername = (TextView)findViewById(R.id.tv_home_username);
	   
	    // Set font
	    btnWallet.setTypeface(font_bold);
	    btnIncome.setTypeface(font_light);
	    btnExpenditure.setTypeface(font_light);
	    btnStatistic.setTypeface(font_italic);
	    
	    LoadInforUser();
	    // Set infor of user
	    lableFullname.setText(fullname_current_user);
	    lableUsername.setText(username_current_user);
	    lableFullname.setTypeface(font_bold);
	    lableUsername.setTypeface(font_light);
	    
	    // Set OnClick Listener
	    btnIncome.setOnClickListener(new MyEvent());
	    btnExpenditure.setOnClickListener(new MyEvent());
	    btnWallet.setOnClickListener(new MyEvent());
	    btnStatistic.setOnClickListener(new MyEvent());
	    btnMenu.setOnClickListener(new MyEvent());
	    
	    // Add list menu
	    mNavItems.add(new NavItem("Wallet", "", R.drawable.btn_wall));
		mNavItems.add(new NavItem("Income", "", R.drawable.btn_income));
		mNavItems.add(new NavItem("Expenture", "", R.drawable.btn_expen));
		mNavItems.add(new NavItem("Transfer", "", R.drawable.btn_transfer));
		mNavItems.add(new NavItem("Category", "Category of Income & Expenditure", R.drawable.btn_cate));
		mNavItems.add(new NavItem("Diary", "Everything you have wrote", R.drawable.btn_diary));
		mNavItems.add(new NavItem("Report", "", R.drawable.btn_report));
	    mNavItems.add(new NavItem("About us", "Information about this app", R.drawable.btn_help));
	    mNavItems.add(new NavItem("Logout", "", R.drawable.btn_logout));
	 
	    // DrawerLayout
	    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
	 
	    // Populate the Navigtion Drawer with options
	    mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
	    mDrawerList = (ListView) findViewById(R.id.lv_home_navList);
	    DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
	    mDrawerList.setAdapter(adapter);
	 
	    // Drawer Item click listeners
	    mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            selectItemFromDrawer(position);
	        }
	    });
	}
		
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Khoi tao intent
		intent_income = new Intent (HomeActivity.this, IncomeActivity.class);
		intent_expend = new Intent (HomeActivity.this, ExpenditureActivity.class);
		intent_wallet = new Intent (HomeActivity.this, WalletActivity.class);
		intent_stattistic = new Intent (HomeActivity.this, StatisticActivity2.class);
		intent_category = new Intent (HomeActivity.this, CategoryActivity.class);
		intent_diary = new Intent (HomeActivity.this, DiaryActivity.class);
		intent_transfer = new Intent (HomeActivity.this, TransferActivity.class);
	}
	
	private void LoadInforUser(){
		accountCursor = dbAdapter.getAccountInfor(id_user);
		if (accountCursor.getCount() <1){
			Toast.makeText(this, "Error! User not found !!!", Toast.LENGTH_SHORT).show();
		}
		else {
			accountCursor.moveToFirst();
			// get infor of user
			fullname_current_user = accountCursor.getString(accountCursor.getColumnIndexOrThrow("FULLNAME"));
		    username_current_user = accountCursor.getString(accountCursor.getColumnIndexOrThrow("USERNAME"));
		}
	}

	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(v.getId()==R.id.btn_home_income)
			{
				startActivity(intent_income);
			}
			else if(v.getId()==R.id.btn_home_expenditure) {
				startActivity(intent_expend);
			}
			else if(v.getId()==R.id.btn_home_wallet) {
				startActivity(intent_wallet);
			}
			else if(v.getId()==R.id.btn_home_statistic) {
				startActivity(intent_stattistic);
			}
			else if (v.getId()==R.id.btn_home_menu){
				mDrawerLayout.openDrawer(mDrawerPane);
			}
		}
	}
	
	/**
	* Called when a particular item from the navigation drawer
	* is selected.
	**/
	private void selectItemFromDrawer(int position) {
	 
		if (position == 0){ /// selected wallet
			startActivity(intent_wallet);
		}
		else if (position == 1){  /// selected income
			startActivity(intent_income);
		}
		else if (position == 2){ ///selected expenditure
			startActivity(intent_expend);
		}
		else if (position == 3){ ///selected transfer
			startActivity(intent_transfer);
		}
		else if (position == 4){ ///selected category
			startActivity(intent_category);
		}
		else if (position == 5){ ///selected Diary
			startActivity(intent_diary);
		}
		else if (position == 6){ ///selected report
			startActivity(intent_stattistic);
		}
		else if (position == 7){ ///selected about us
			Toast.makeText(HomeActivity.this, "You have just select About us", Toast.LENGTH_LONG).show();
		}
		else if (position == 8){ ///selected logout
			Intent intent_login = new Intent(HomeActivity.this, LoginActivity.class);
			HomeActivity.this.finish(); // Close Home Activity
			startActivity(intent_login); // Start login activity
		}		
	    //mDrawerList.setItemChecked(position, true);
	    //setTitle(mNavItems.get(position).mTitle);
	 
	    // Close the drawer
	    mDrawerLayout.closeDrawer(mDrawerPane);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		backButtonEvent();
	}
	private void backButtonEvent(){
		AlertDialog.Builder adb = new AlertDialog.Builder(HomeActivity.this);
		adb.setTitle("Exit?");
		adb.setMessage("Are you sure you want to exit?");
		adb.setNegativeButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		adb.setPositiveButton("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		adb.show();
	}
	
}
