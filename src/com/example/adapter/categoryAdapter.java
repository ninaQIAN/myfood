package com.example.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.category;
import com.example.myfood.DiquActivity;
import com.example.myfood.R;
import com.example.myfood.StarActivity;

/***
 * 自定义baseadapter
 * 
 * @author Administrator
 * 
 */
public class categoryAdapter extends BaseAdapter {
	// 自定义图片Adapter以内部类形式存在于MainActivity中，方便访问MainActivity中的各个变量，特别是imgs数组
	private DiquActivity context;// 用于接收传递过来的Context对象
	private LayoutInflater layoutinflater;
	private String inflater = Context.LAYOUT_INFLATER_SERVICE; // 这个必须得有
	private List<category> list;
	private List<HashMap<String, Object>> categorylist = new ArrayList<HashMap<String, Object>>();
	private ProgressDialog ProgressDialog1; // 加载对话框
	private ImageLoader mImageLoader;
	private boolean mBusy = false;

	public void setFlagBusy(boolean busy) {
		this.mBusy = busy;
	}

	public categoryAdapter(DiquActivity context, List<category> list) {
		super();
		this.context = context;
		layoutinflater = (LayoutInflater) context.getSystemService(inflater); // 这个必须得写
		this.list = list;
		mImageLoader = new ImageLoader(); // 声明图片文件流
	}

	@Override
	public int getCount() {
		return categorylist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addlist(List<category> addlist) {
		try {
			for (category categorys : addlist) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("_id", categorys.get_id());
				item.put("_channel_id", categorys.get_channel_id());
				item.put("_title", categorys.get_title());
				item.put("_call_index", categorys.get_call_index());
				item.put("_parent_id", categorys.get_parent_id());
				item.put("_class_list", categorys.get_class_list());
				item.put("_class_layer", categorys.get_class_layer());
				item.put("_sort_id", categorys.get_sort_id());
				item.put("_link_url", categorys.get_link_url());
				item.put("_img_url", categorys.get_img_url());
				item.put("_content", categorys.get_content());
				item.put("_seo_title", categorys.get_seo_title());
				item.put("_seo_keywords", categorys.get_seo_keywords());
				item.put("_seo_description", categorys.get_seo_description());
				categorylist.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
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
				viewHolder.textview2 = (Button) convertView
						.findViewById(R.diqu.tv2);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (categoryViewHolder) convertView.getTag();
			}
			if (!mBusy) {
				mImageLoader.categoryloadImage(
						context.localhost()
								+ categorylist.get(position).get("_img_url")
										.toString(), this, viewHolder);
				viewHolder.textview1.setText(categorylist.get(position)
						.get("_title").toString());

				try {
					String[] spStr = categorylist.get(position)
							.get("_seo_keywords").toString().split(",");
					viewHolder.textview2.setText("联系电话：" + spStr[0]);
				} catch (Exception e) {
					viewHolder.textview2.setText("联系电话："
							+ categorylist.get(position).get("_seo_keywords")
									.toString());
					e.printStackTrace();

				}

			} else {
				Bitmap bitmap = mImageLoader
						.getBitmapFromCache(context.localhost()
								+ categorylist.get(position).get("_img_url")
										.toString());
				if (bitmap != null) {
					viewHolder.mImageView.setImageBitmap(bitmap);
				} else {
					viewHolder.mImageView
							.setImageResource(R.drawable.ic_launcher);
				}
			}
			viewHolder.textview2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					context.calltel(categorylist.get(position).get("_seo_keywords")
							.toString());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	static class categoryViewHolder {
		ImageView mImageView;
		TextView textview1;
		Button textview2;
	}

}
