package com.mililu.moneypower;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.mililu.moneypower.classobject.Wallet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class WalletActivity extends Activity{
	private MyWalletArrayAdapter Walletaa = null;
	private Button btnBack, btnCreateWallet;
	private DataBaseAdapter dbAdapter;
	private List<Wallet>list_wallet=new ArrayList<Wallet>();
	private TextView txtTittle, txtBalance, txtTotalAmount;
	private Cursor cursorWallet;
	private ListView lvWallet;
	private int id_user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_wallet);
		
		// Get The Reference Of View
	    txtTittle=(TextView)findViewById(R.id.tv_wallet_title);
	    txtBalance=(TextView)findViewById(R.id.tv_wallet_totalbalance);
	    txtTotalAmount=(TextView)findViewById(R.id.tv_wallet_amount);
	    btnBack=(Button)findViewById(R.id.btn_wallet_back);
	    btnCreateWallet=(Button)findViewById(R.id.btn_wallet_createwallet);
	    lvWallet = (ListView)findViewById(R.id.lv_wallet_listwallet);
		
	    // khoi tao font
        Typeface font_light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
        Typeface font_bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
        // set font
        txtTittle.setTypeface(font_light);
        txtBalance.setTypeface(font_light);
        txtTotalAmount.setTypeface(font_bold);
        
		// Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    
	    // Set OnClick Listener on button 
	    btnBack.setOnClickListener(new MyEvent());
	    btnCreateWallet.setOnClickListener(new MyEvent());
	    
	    id_user = HomeActivity.id_user;
	    // Set OnItemClick Listener on listview
	    lvWallet.setOnItemClickListener(new MyEventItemOnClick());
	    lvWallet.setOnItemLongClickListener(new MyEventItemOnLongClick());
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		ShowWallet();
		getTotalAmount(id_user);
	}
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.btn_wallet_back)
			{
				WalletActivity.this.finish();
			}
			else if(v.getId()==R.id.btn_wallet_createwallet)
			{
				Intent intent = new Intent (WalletActivity.this, CreateWalletActivity.class);
				startActivity(intent);
			}
		}
	}
	private void getTotalAmount(int id_user){
		long mBalance = dbAdapter.getTotalAmount(id_user);
		txtTotalAmount.setText(NumberFormat.getCurrencyInstance().format(mBalance)); // NumberFormat.getCurrencyInstance().format(mBalance) dinh dang so tien 
	}
	
	private void ShowWallet(){
		list_wallet.clear();
		cursorWallet=dbAdapter.getListWalletOfUser(id_user);
		if (cursorWallet.getCount()<1){
			Toast.makeText(WalletActivity.this, "You don't have any wallet !!", Toast.LENGTH_LONG).show();
		}
		else{
			cursorWallet.moveToFirst();
			while(!cursorWallet.isAfterLast()){
				Wallet data=new Wallet();
				data.setId_wallet(cursorWallet.getInt(cursorWallet.getColumnIndexOrThrow("ID_WALLET")));
				data.setName(cursorWallet.getString(cursorWallet.getColumnIndexOrThrow("NAME_WALLET")));
				long money = cursorWallet.getLong(cursorWallet.getColumnIndexOrThrow("MONEY"));
				data.setMoney(money);
				list_wallet.add(data);
				cursorWallet.moveToNext();
		 	}
			cursorWallet.close();
			Walletaa = new MyWalletArrayAdapter(WalletActivity.this, R.layout.layout_for_list_wallet, list_wallet);
			Walletaa.notifyDataSetChanged();
			lvWallet.setAdapter(Walletaa);
		}
	}
	
	private class MyEventItemOnClick implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			Bundle bundle=new Bundle();
			bundle.putInt("ID_WALLET", list_wallet.get(position).getId_wallet());
			bundle.putString("NAME_WALLET", list_wallet.get(position).getName());
			Intent intent = new Intent (WalletActivity.this, DetailWalletActivity.class);
			intent.putExtra("DATA", bundle);
			startActivity(intent);
		}
	}
	
	private class MyEventItemOnLongClick implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0,final View view,final int position, long id) {
			showPopupMenu(view, position);
			return true;
		}
	}
	private void showPopupMenu(View view,final int position) {
		// Retrieve the clicked item from view's tag
        final Wallet item = list_wallet.get(position);
        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(WalletActivity.this, view);
        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        // Set a listener so we are notified if a menu item is clicked
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_popup_delete:
                        // Show messenger to confirm delete
                    	AlertDialog.Builder adb = new AlertDialog.Builder(WalletActivity.this); // khoi tao thong bao
                        adb.setTitle("Delete !!!"); // title of messenger
                        adb.setMessage("Are you sure you want to delete " + item.getName() + " ?" ); // contain of messenger
                        adb.setNegativeButton("NO", null);
                        adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            	// Remove the item from the adapter and database
                            	if(dbAdapter.deleteWallet(item.getId_wallet())){
                            		list_wallet.remove(position); // xoa trong list
                            		Walletaa.notifyDataSetChanged(); // reset lai listview
                            		getTotalAmount(id_user); // load lai du lieu
                            		Toast.makeText(WalletActivity.this, "Wallet has been deleted !!", Toast.LENGTH_SHORT).show(); // thong bao thanh cong
                            	}
                            	else{
                            		Toast.makeText(WalletActivity.this, item.getId_wallet() + " - " + item.getName(), Toast.LENGTH_SHORT).show(); // thong bao that bai
                            	}
                            } // end of onClick
                        }); 
                        adb.show(); // show messenger
                        return true; // end of case delete
                    case R.id.menu_popup_edit:
                    	// Edit the item form the adapter
                    		Intent intent = new Intent (WalletActivity.this, EditWalletActivity.class);
                    		intent.putExtra("id_wallet", item.getId_wallet());
                    		startActivity(intent);
                    	return true; // end of case edit
                }
                return false;
            }
        });
        // Finally show the PopupMenu
        popup.show();
	}
}
