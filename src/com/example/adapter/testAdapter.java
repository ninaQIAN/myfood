package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adapter.categoryAdapter.categoryViewHolder;
import com.example.model.test;
import com.example.myfood.DiquActivity;
import com.example.myfood.R;

public class testAdapter extends BaseAdapter{
	
	private DiquActivity context;// 用于接收传递过来的Context对象
	private LayoutInflater layoutinflater;
	private String inflater = Context.LAYOUT_INFLATER_SERVICE; // 这个必须得有
	private List<test> list;
	private List<HashMap<String, Object>> testlist = new ArrayList<HashMap<String, Object>>();
	private ProgressDialog ProgressDialog1; // 加载对话框
	private boolean mBusy = false;
	
	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}
	
	public testAdapter(DiquActivity context, List<test> list) {
		super();
		this.context = context;
		layoutinflater = (LayoutInflater) context.getSystemService(inflater); // 这个必须得写
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return testlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void addlist(List<test> addlist) {
		try {
			for (test test : addlist) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("tour", test.get_area());
				item.put("price", test.get_price());
				testlist.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		categoryViewHolder viewHolder = null;
		try {
			if (convertView == null) {
				convertView = layoutinflater.from(context).inflate(
						R.layout.item_new, null);
				viewHolder = new categoryViewHolder();
				viewHolder.mImageView = (ImageView) convertView
						.findViewById(R.diqu.imageView1);
				viewHolder.textview1 = (TextView) convertView
						.findViewById(R.diqu.tv1);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (categoryViewHolder) convertView.getTag();
			}
			if (!mBusy) {
				
				viewHolder.textview1.setText(testlist.get(position)
						.get("tour").toString());

				try {
					String[] spStr = testlist.get(position)
							.get("price").toString().split(",");
				} catch (Exception e) {
					e.printStackTrace();

				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

}
