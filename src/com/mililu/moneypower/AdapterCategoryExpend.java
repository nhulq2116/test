package com.mililu.moneypower;

import java.util.ArrayList;

import com.mililu.moneypower.classobject.ExpenditureDetail;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class AdapterCategoryExpend extends BaseExpandableListAdapter{
	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private ArrayList<String> parentItems;
	private ArrayList<ExpenditureDetail> child;
	private Activity activity;
	
	public AdapterCategoryExpend(ArrayList<String> parents, ArrayList<Object> childern) {
		this.parentItems = parents;
		this.childtems = childern;
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}

	
	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		
		child = (ArrayList<ExpenditureDetail>) childtems.get(groupPosition);

		//int mId = child.get(childPosition).getId_exp_det();
		String mName = child.get(childPosition).getName_expend();

		TextView tvName = null;
	
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.layout_for_category_item, null);
		}
		
		tvName = (TextView) convertView.findViewById(R.id.tv_layoutcategoryitem_name);
		
		tvName.setText(mName);
		
		
		/*convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Toast.makeText(activity, String.valueOf(child.get(childPosition).getId_diary()),
						Toast.LENGTH_SHORT).show();
			}
		});*/
		
		return convertView;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.layout_for_category_group, null);
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
		return ((ArrayList<ExpenditureDetail>) childtems.get(groupPosition)).size();
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
