package com.icitymobile.anda.ui.member;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.icitymobile.anda.R;
import com.icitymobile.anda.bean.BankItem;
import com.icitymobile.anda.bean.PCItem;
import com.icitymobile.anda.ui.member.PCAdapter.ViewHolder;

public class BankAdapter extends ArrayAdapter<BankItem>{
	private int resourceId;
	public BankAdapter(Context context, int textViewResourceId,
			List<BankItem> objects){
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		BankItem item = getItem(position);//get one news item
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId,null); //layout setting
			viewHolder = new ViewHolder();
			viewHolder.itemName = (TextView) view.findViewById(R.id.pbc_name);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.itemName.setText(item.getBank());

		return view;
	}
	
	class ViewHolder{
		TextView itemName;
	}
}