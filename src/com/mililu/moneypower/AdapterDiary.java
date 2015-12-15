package com.mililu.moneypower;

import java.text.NumberFormat;
import java.util.ArrayList;

import com.mililu.moneypower.classobject.Diary;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class AdapterDiary extends BaseExpandableListAdapter {
	private Activity activity;
	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private ArrayList<String> parentItems;
	private ArrayList<Diary> child;
	
	public AdapterDiary(ArrayList<String> parents, ArrayList<Object> childern) {
		this.parentItems = parents;
		this.childtems = childern;
	}
	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}
	
	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		
		child = (ArrayList<Diary>) childtems.get(groupPosition);

		String mName = "";
		String mWallet = child.get(childPosition).getName_wallet();
		String mAmount = NumberFormat.getCurrencyInstance().format((child.get(childPosition).getAmount()));
		String mNotice = child.get(childPosition).getNotice();
		int mType = child.get(childPosition).getType();
		if (mType == 1){
			mName = child.get(childPosition).getName_income(); 
		}
		else if (mType == 2){
			mName = child.get(childPosition).getName_expen();
		}
		else {
			mName = "Unidentify !!!";
		}
		
		TextView tvName = null;
		TextView tvWallet = null;
		TextView tvAmount = null;
		TextView tvNotice = null;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.layout_for_diary_item, null);
		}
		
		tvName = (TextView) convertView.findViewById(R.id.tv_layoutdiary_namecategory);
		tvWallet = (TextView)convertView.findViewById(R.id.tv_layoutdiary_namewallet);
		tvAmount = (TextView)convertView.findViewById(R.id.tv_layoutdiary_amount);
		tvNotice = (TextView)convertView.findViewById(R.id.tv_layoutdiary_notice);
		
		tvName.setText(mName);
		tvWallet.setText(mWallet);
		tvAmount.setText(mAmount);
		tvNotice.setText(mNotice);
		
		return convertView;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.layout_for_diary_group, null);
		}
		
		((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);
		
		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<Diary>) childtems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}