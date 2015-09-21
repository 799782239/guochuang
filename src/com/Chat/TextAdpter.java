package com.Chat;

import java.util.List;

import com.DB.User;
import com.example.notification.R;

import android.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextAdpter extends BaseAdapter {
	private List<DataC> list;
	private Context context;
	private RelativeLayout layout;

	public TextAdpter(List<DataC> list, Context convertView) {
		this.list = list;
		this.context = convertView;
	}

	public void addAll(List<User> data) {
		for (int i = 0; i < data.size(); i++) {
			DataC c = new DataC(data.get(i).getMsgData(), data.get(i)
					.getStatus());
			list.add(c);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			if (list.get(position).getFlag() == DataC.RECIVER) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.talk_to_other, null);
				convertView.setTag(DataC.RECIVER, new TextCellL(
						(TextView) convertView.findViewById(R.id.leftc)));
			} else if (list.get(position).getFlag() == DataC.SEND) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.talk_to_me, null);
				convertView.setTag(DataC.SEND, new TextCellR(
						(TextView) convertView.findViewById(R.id.leftc)));
			}

		}
		if (list.get(position).getFlag() == DataC.SEND) {
			TextCellR textCellR = (TextCellR) convertView.getTag(DataC.SEND);
			textCellR.getTextView().setText(list.get(position).getContent());
		} else if (list.get(position).getFlag() == DataC.RECIVER) {
			TextCellL textCellL = (TextCellL) convertView.getTag(DataC.RECIVER);
			textCellL.getTextView().setText(list.get(position).getContent());
		}
		return convertView;
	}

	public static class TextCellL {
		private TextView textView;

		public TextCellL(TextView textView) {
			this.textView = textView;
		}

		public TextView getTextView() {
			return textView;
		}
	}

	public static class TextCellR {
		private TextView textView;

		public TextCellR(TextView textView) {
			this.textView = textView;
		}

		public TextView getTextView() {
			return textView;
		}
	}
}
