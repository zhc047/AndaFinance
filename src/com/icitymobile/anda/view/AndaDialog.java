package com.icitymobile.anda.view;

import com.icitymobile.anda.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 对话框
 * @author Sherry
 */
public class AndaDialog {
	
	AlertDialog mAlertDialog;
	Context mContext;
	
	//内容
	String message;
	
	OnClickListener onCancelListener;
	OnClickListener onSubmitListener;
	
	
	public AndaDialog(Context mContext) {
		this.mContext = mContext;
	}

	public AndaDialog setMessage(String message) {
		this.message = message;
		return this;
	}

	public AndaDialog setMessage(int resId) {
		this.message = mContext.getResources().getString(resId);
		return this;
	}
	
	
	/**
	 * 显示对话框
	 */
	public void show() {
		mAlertDialog = new AlertDialog.Builder(mContext).create();
		mAlertDialog.show();
		mAlertDialog.getWindow().setContentView(R.layout.dialog_common);
		ViewGroup mContainer = (ViewGroup)mAlertDialog.getWindow().findViewById(R.id.dialog_container);
		TextView mMsgTextView = (TextView)mAlertDialog.getWindow().findViewById(R.id.dialog_message);
		mMsgTextView.setText(message);
		mAlertDialog.getWindow().findViewById(R.id.dialog_btn_cancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onCancelListener != null) {
					onCancelListener.onClick(v);
				}
				dismiss();
			}
		});
		mAlertDialog.getWindow().findViewById(R.id.dialog_btn_submit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onSubmitListener != null) {
					onSubmitListener.onClick(v);
				}
				dismiss();
			}
		});
	}
	
	/**
	 * 取消对话框
	 */
	public void dismiss() {
		if(mAlertDialog != null && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
	}

	public AndaDialog setOnCancelListener(OnClickListener onCancelListener) {
		this.onCancelListener = onCancelListener;
		return this;
	}

	public AndaDialog setOnSubmitListener(OnClickListener onSubmitListener) {
		this.onSubmitListener = onSubmitListener;
		return this;
	}
}