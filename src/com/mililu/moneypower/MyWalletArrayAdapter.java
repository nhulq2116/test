package com.mililu.moneypower;

import java.text.NumberFormat;
import java.util.List;

import com.mililu.moneypower.classobject.Wallet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyWalletArrayAdapter extends ArrayAdapter<Wallet>{
	
	private Activity context;
	private int layout;
	private List<Wallet>list;
	private Typeface tf;
	
	public MyWalletArrayAdapter(Context context, int textViewResourceId, List<Wallet> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=(Activity) context;
		this.layout=textViewResourceId;
		this.list=objects;
		
		tf = Typeface.createFromAsset(context.getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	}	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null){
			LayoutInflater flater = context.getLayoutInflater();
			convertView = flater.inflate(layout, parent,false);
		}
		TextView walletname =(TextView) convertView.findViewById(R.id.tv_layoutlistwallet_name);
		walletname.setTypeface(tf);
		TextView amount = (TextView) convertView.findViewById(R.id.tv_layoutlistwallet_amount);	
		amount.setTypeface(tf);
		
		Wallet data=list.get(position);
		walletname.setText(data.getName()==null?"":data.getName().toString());
		amount.setText(NumberFormat.getInstance().format(data.getMoney()));
		return convertView;
	}

	
}