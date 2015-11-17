package com.icitymobile.anda.ui.member;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.BillItem;

public class BillAdapter extends ArrayAdapter<BillItem>{

	public BillAdapter(Context context) {
		super(context, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		BillItem item = getItem(position);//get one bill item
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(R.layout.bill_item,null); //layout setting
			viewHolder = new ViewHolder();
			viewHolder.remark = (TextView) view.findViewById(R.id.bill_title);
			viewHolder.pay_time = (TextView) view.findViewById(R.id.bill_time);
			viewHolder.amount = (TextView) view.findViewById(R.id.bill_amount);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.remark.setText(item.getRemark());
		viewHolder.pay_time.setText(item.getPayTime());
		viewHolder.amount.setText(item.getAmount());
		return view;
	}
	
	class ViewHolder{
		TextView remark;
		TextView pay_time;
		TextView amount;
	}
}
