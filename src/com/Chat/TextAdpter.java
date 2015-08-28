package com.Chat;

import java.util.List;

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

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if (list.get(position).getFlag() == DataC.RECIVER) {
			layout = (RelativeLayout) inflater.inflate(R.layout.talk_to_other, null);
		}
		if (list.get(position).getFlag() == DataC.SEND) {
			layout = (RelativeLayout) inflater
					.inflate(R.layout.talk_to_me, null);
		}
		TextView tv = (TextView) layout.findViewById(R.id.leftc);
		tv.setText(list.get(position).getContent());
		return layout;
	}

}
