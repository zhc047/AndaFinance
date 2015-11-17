package com.icitymobile.anda.view;

import com.icitymobile.anda.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 自定义Loading Dialog
 * @author robot
 */
public class AndaProcessDialog extends Dialog{
		TextView mTvMessage;
		String message = null;
		
		public AndaProcessDialog(Context context) {
			super(context, R.style.AppDialog);
			//按“返回键”无法取消
			setCancelable(false);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.dialog_loading);
			mTvMessage = (TextView)findViewById(R.id.dialog_message);
			if(message != null) {
				mTvMessage.setText(message);
			}
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setMessage(int messageId) {
			this.message = getContext().getString(messageId);
		}
		
	}