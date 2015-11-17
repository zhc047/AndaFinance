package com.icitymobile.anda.ui.member;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.PCItem;

public class PCAdapter extends ArrayAdapter<PCItem>{
	public PCAdapter(Context context){
		super(context, 0);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		PCItem item = getItem(position);//get one news item
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(R.layout.pcb_list_item,null); //layout setting
			viewHolder = new ViewHolder();
			viewHolder.itemName = (TextView) view.findViewById(R.id.pbc_name);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.itemName.setText(item.getCity());

		return view;
	}
	
	class ViewHolder{
		TextView itemName;
	}
}
