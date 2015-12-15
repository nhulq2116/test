package com.mililu.moneypower;

import java.util.List;

import com.mililu.moneypower.classobject.Income;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class ArrayAdapterCategoryIncome extends ArrayAdapter<Income>{
	private Activity context;
	private int layout;
	private List<Income>list;
	private Typeface tf;
	private DataBaseAdapter dbAdapter;
	
	public ArrayAdapterCategoryIncome(Context context, int textViewResourceId, List<Income> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=(Activity) context;
		this.layout=textViewResourceId;
		this.list=objects;
		
		// khai bao font
		tf = Typeface.createFromAsset(context.getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
		
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
			
			// Set reference
			TextView incomename =(TextView) convertView.findViewById(R.id.tv_layoutcategoryitem_name);
			
			// Set font
			incomename.setTypeface(tf);
			
			Income data=list.get(position);
			incomename.setText(data.getName()==null?"":data.getName().toString());
			
			// Retrieve the popup button from the inflated view
            View popupButton = convertView.findViewById(R.id.btn_layoutcategoryitem_seting);
 
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
        final Income item = (Income) view.getTag();
 
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
                        // Show messenger to confirm delete
                    	AlertDialog.Builder adb = new AlertDialog.Builder(getContext()); // khoi tao thong bao
                        adb.setTitle("Delete !!!"); // title of messenger
                        adb.setMessage("Are you sure you want to delete " + item.getName() + " ?" ); // contain of messenger
                        adb.setNegativeButton("NO", null);
                        adb.setPositiveButton("YES", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            	
                            	// Remove the item from the adapter and database
                            	if(dbAdapter.deteleCategoryIncome(item.getId())){
                            		list.remove(getPosition(item)); // xoa trong list
                            		CategoryIncomeActivity.aaCategoryIncome.notifyDataSetChanged(); // reset lai listview
                            		Toast.makeText(context, "Income has been deleted !!", Toast.LENGTH_SHORT).show(); // thong bao thanh cong
                            	}
                            	else{
                            		Toast.makeText(context, item.getId() + " - " + item.getName(), Toast.LENGTH_SHORT).show(); // thong bao that bai
                            	}
                            } // end of onClick
                        }); 
                        adb.show(); // show messenger
                        return true; // end of case delete
                    case R.id.menu_popup_edit:
                    	// Edit the item form the adapter
                    		Intent intent = new Intent (context, EditCategoryIncomeActivity.class);
                    		intent.putExtra("id_income", item.getId());
                    		context.startActivity(intent);
                    	
                    	return true; // end of case edit
                }
                return false;
            }
        });
 
        // Finally show the PopupMenu
        popup.show();
    }
}
