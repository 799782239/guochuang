package com.Chat;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.DB.User;
import com.example.notification.R;

public class TextAdpter extends BaseAdapter {
	private List<DataC> list;
	private Context context;

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
		TextCellL textCellL = null;
		TextCellR textCellR = null;
		if (convertView == null) {
			// if (list.get(position).getFlag() == DataC.RECIVER) {
			// convertView = LayoutInflater.from(context).inflate(
			// R.layout.talk_to_other, null);
			// textCell.setTextViewL((TextView) convertView
			// .findViewById(R.id.leftc));
			// convertView.setTag(textCell);
			// } else if (list.get(position).getFlag() == DataC.SEND) {
			// convertView = LayoutInflater.from(context).inflate(
			// R.layout.talk_to_me, null);
			// textCell.setTextViewR((TextView) convertView
			// .findViewById(R.id.leftc));
			// convertView.setTag(textCell);
			// }
			if (list.get(position).getFlag() == DataC.RECIVER) {
				textCellL = new TextCellL();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.talk_to_other, null);
				textCellL.setTextViewL((TextView) convertView
						.findViewById(R.id.leftc));
				convertView.setTag(textCellL);
			} else if (list.get(position).getFlag() == DataC.SEND) {
				textCellR = new TextCellR();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.talk_to_me, null);
				textCellR.setTextViewR((TextView) convertView
						.findViewById(R.id.leftc));
				convertView.setTag(textCellR);
			}
		} else {
			if (list.get(position).getFlag() == DataC.SEND) {
				if (convertView.getTag().getClass().equals(textCellR)) {
					textCellR = (TextCellR) convertView.getTag();
				} else {
					textCellR = new TextCellR();
					convertView = LayoutInflater.from(context).inflate(
							R.layout.talk_to_me, null);
					textCellR.setTextViewR((TextView) convertView
							.findViewById(R.id.leftc));
					convertView.setTag(textCellR);
				}

			} else if (list.get(position).getFlag() == DataC.RECIVER) {
				if (convertView.getTag().getClass().equals(textCellL)) {
					textCellL = (TextCellL) convertView.getTag();
				} else {
					textCellL = new TextCellL();
					convertView = LayoutInflater.from(context).inflate(
							R.layout.talk_to_other, null);
					textCellL.setTextViewL((TextView) convertView
							.findViewById(R.id.leftc));
					convertView.setTag(textCellL);
				}

			}
		}
		if (list.get(position).getFlag() == DataC.SEND) {
			textCellR.getTextViewR().setText(list.get(position).getContent());
		} else if (list.get(position).getFlag() == DataC.RECIVER) {
			textCellL.getTextViewL().setText(list.get(position).getContent());
		}
		return convertView;
	}

	public static class TextCellL {
		private TextView textViewL;

		public TextView getTextViewL() {
			return textViewL;
		}

		public void setTextViewL(TextView textViewL) {
			this.textViewL = textViewL;
		}
	}

	public static class TextCellR {
		private TextView textViewR;

		public TextView getTextViewR() {
			return textViewR;
		}

		public void setTextViewR(TextView textViewR) {
			this.textViewR = textViewR;
		}

	}

}
