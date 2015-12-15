package com.mililu.moneypower;

import java.text.NumberFormat;
import java.util.List;

import com.mililu.moneypower.classobject.Diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class AdapterDetailWalletArray extends ArrayAdapter<Diary>{
	private Activity context;
	private int layout;
	private List<Diary>list;
	private Typeface tf, tf1 ;
	private DataBaseAdapter dbAdapter;
	
	public AdapterDetailWalletArray(Context context, int textViewResourceId, List<Diary> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=(Activity) context;
		this.layout=textViewResourceId;
		this.list=objects;
		
		tf = Typeface.createFromAsset(context.getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
		tf1 = Typeface.createFromAsset(context.getAssets(),"fonts/HELVETICANEUELIGHTITALIC.TTF");

		// Create a instance of SQLite Database
		dbAdapter = new DataBaseAdapter(context);
		dbAdapter = dbAdapter.open();
	}	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null){
			LayoutInflater flater = context.getLayoutInflater();
			convertView = flater.inflate(layout, parent,false);
		}
			
			TextView NameDiary = (TextView) convertView.findViewById(R.id.tv_layoutdetailwallet_name);
			NameDiary.setTypeface(tf);
			TextView Date=(TextView) convertView.findViewById(R.id.tv_layoutdetailwallet_date);	
			Date.setTypeface(tf1);
			TextView Amount=(TextView) convertView.findViewById(R.id.tv_layoutdetailwallet_money);
			Amount.setTypeface(tf);
			TextView Notice=(TextView) convertView.findViewById(R.id.tv_layoutdetailwallet_notice);
			Notice.setTypeface(tf1);
			Diary data=list.get(position);
			
			if (data.getType() == 1) // neu la thu vao
			{
				NameDiary.setText(data.getName_income()==null?"":data.getName_income().toString());
//				Amount.setTextColor(Color.BLUE);
				Amount.setTextColor(Color.parseColor("#b92902"));
			}
			else if (data.getType() == 2) // neu la chi 
			{
				NameDiary.setText(data.getName_expen()==null?"":data.getName_expen().toString());
//				Amount.setTextColor(Color.RED); // chinh mau chu (#FF0000 = 16711680 = RED)
				Amount.setTextColor(Color.parseColor("#33ccff"));
			}
			if ((String.valueOf(data.getDay())=="")|| (String.valueOf(data.getDay())=="") || (String.valueOf(data.getDay())=="")) {
				Date.setText("");
			}
			else {
				Date.setText(data.getDay() + "/" + data.getMonth() + "/" + data.getYear());
			}
			//Amount.setText(String.valueOf(data.getAmount())==null?"":data.getAmount() + "VND");
			Amount.setText(NumberFormat.getCurrencyInstance().format(data.getAmount()));
			Notice.setText(data.getNotice()==null?"":data.getNotice().toString());
			
			// Retrieve the popup button from the inflated view
            View popupButton = convertView.findViewById(R.id.lv_detail);
            // Set the item as the button's tag so it can be retrieved later
            popupButton.setTag(getItem(position));
 
            // Set the fragment instance as the OnClickListener
            popupButton.setOnClickListener(new myOnClick());
			
			return convertView;
	}
	
	private class myOnClick implements OnClickListener{
		@Override
	    public void onClick(final View view) {
	        // We need to post a Runnable to show the popup to make sure that the PopupMenu is
	        // correctly positioned. The reason being that the view may change position before the
	        // PopupMenu is shown.
	        view.post(new Runnable() {
	            @Override
	            public void run() {
	                showPopupMenu(view);
	            }
	        });
	    }
	}
	private void showPopupMenu(View view) {

        
		// Retrieve the clicked item from view's tag
        final Diary item = (Diary) view.getTag();
 
        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(context, view);
 
        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
 
        // Set a listener so we are notified if a menu item is clicked
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_popup_delete:
                    	String transactionName = "";
                    	if (item.getType() == 1){
                    		transactionName = item.getName_income();
                    	}
                    	else if(item.getType() == 2){
                    		transactionName = item.getName_expen();
                    	}
                    		
                        // Show messenger to confirm delete
                    	AlertDialog.Builder adb = new AlertDialog.Builder(getContext()); // khoi tao thong bao
                        adb.setTitle("Delete !!!"); // title of messenger
                        adb.setMessage("Are you sure you want to delete " + transactionName + " (" + NumberFormat.getCurrencyInstance().format(item.getAmount()) + ") ?" ); // contain of messenger
                        adb.setNegativeButton("NO", null);
                        adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            	
                            	// Remove the item from the adapter and database
                            	if(dbAdapter.deteleDiary(item.getId_diary())){
                            		// calculator new money
                            		long curentmoney = dbAdapter.getAmountOfWallet(DetailWalletActivity.id_current_wallet); // lay so tien hien tai
                            		long newmoney = 0;
                            		if (item.getType() == 1){ // neu thu thi tru ra
                            			newmoney = curentmoney - item.getAmount();
                            		}
                            		else if (item.getType() == 2){ // neu chi thi cong vo
                            			newmoney = curentmoney + item.getAmount();
                            		}
                            		dbAdapter.updateWallet(DetailWalletActivity.id_current_wallet, newmoney);
                            		
                            		DetailWalletActivity.ShowInforWallet(); // load lai thong tin cua wallet
                            		list.remove(getPosition(item)); // xoa trong list
                            		DetailWalletActivity.adtDeatailWalletArr.notifyDataSetChanged(); // reset lai listview
                            		Toast.makeText(context, "Transaction has been deleted !!", Toast.LENGTH_SHORT).show(); // thong bao thanh cong
                            	}
                            	else{
                            		Toast.makeText(context, "Can't delete this this transaction !!", Toast.LENGTH_SHORT).show(); // thong bao that bai
                            	}
                            } // end of onClick
                        }); 
                        adb.show(); // show messenger
                        return true; // end of case delete
                    case R.id.menu_popup_edit:
                    	// Edit the item form the adapter
                    	if (item.getType() == 1){
                    		Intent intent = new Intent (context, EditIncomeActivity.class);
                    		intent.putExtra("id_diary", item.getId_diary());
                    		context.startActivity(intent);
                    	}
                    	else if(item.getType() == 2){
                    		Intent intent = new Intent (context, EditExpenseActivity.class);
                    		intent.putExtra("id_diary", item.getId_diary());
                    		context.startActivity(intent);
                    	}
                    	
                    	return true; // end of case edit
                }
                return false;
            }
        });
 
        // Finally show the PopupMenu
        popup.show();
    }
}
