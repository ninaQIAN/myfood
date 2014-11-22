package com.example.myfood;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import com.example.adapter.categoryAdapter;
import com.example.control.MyListView;
import com.example.control.MyListView.OnRefreshListener;
import com.example.jsonservices.jsoncategory;
import com.example.method.mymethod;
import com.example.model.category;
import com.example.utils.DBhelper;
import com.example.utils.myapplication;

public class DiquActivity extends Activity {

	private ProgressDialog ProgressDialog1; // 加载对话框
	private mymethod mymethods; // 公共方法调用
	private categoryAdapter adapter; // 店铺adapter
	private SimpleAdapter dqadapter; // 地区adapter
	private myapplication myapplication1;
	private Thread thread = null;
	private int page = 1;
	private MyListView listview1;
	private View view;
	private List<category> list1;
	private List<HashMap<String, Object>> categorylist = new ArrayList<HashMap<String, Object>>();
	private List<HashMap<String, Object>> dqlist = null;
	private final int pageType = 1;
	private Thread Thread1;
	private boolean havedata = true; // 来判断是否还有数据
	private TabHost tabhost;
	private List<category> dqcategory;
	private List<category> Categorys;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diqu);
		try {
			myapplication1 = (myapplication) getApplication();
			myapplication1.getInstance().addActivity(this);
			tabhost = (TabHost) findViewById(android.R.id.tabhost);
			tabhost.setup();
			TabWidget tabwidget = tabhost.getTabWidget();
			tabhost.addTab(tabhost.newTabSpec("tab1").setIndicator("地区")
					.setContent(R.id.tab1));
			tabhost.addTab(tabhost.newTabSpec("tab2").setIndicator("地图模式")
					.setContent(R.id.tab2));
			listview1 = (MyListView) findViewById(R.id.listview1);
			
			/* create json */
			create_json();
			
			threadstart();
			binddq();
			tabhost.getTabWidget().getChildAt(0)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							tabhost.setCurrentTab(0);

							try {
								Builder mydialog = new AlertDialog.Builder(
										DiquActivity.this);
								mydialog.setTitle("地区选择");
								mydialog.setAdapter(dqadapter,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub

												try {
													String test = "URL area_id=" + dqlist.get(which).get("id");
													Log.v("JSON", test);
													myapplication1
															.setcategoryurl("servlet/JsonAction?action_flag=Category&area_id="
																	+ dqlist.get(which).get("id"));
													Intent Intent1 = new Intent();
													Intent1.setClass(
															DiquActivity.this,
															DiquActivity.class);
													startActivity(Intent1);

													finish();
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										});
								mydialog.show();

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

			// 单击事件跳转到店铺详细页
			listview1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub

					Intent intent1 = new Intent();
					intent1.setClass(DiquActivity.this, CantingActivity.class);
					Bundle bundle1 = new Bundle();
					bundle1.putInt(
							"id",
							Integer.parseInt(categorylist.get(arg2 - 1)
									.get("_id").toString()));
					bundle1.putInt(
							"channel_id",
							Integer.parseInt(categorylist.get(arg2 - 1)
									.get("_channel_id").toString()));
					bundle1.putString("title",
							categorylist.get(arg2 - 1).get("_title").toString());
					bundle1.putString("call_index", categorylist.get(arg2 - 1)
							.get("_call_index").toString());
					bundle1.putInt(
							"parent_id",
							Integer.parseInt(categorylist.get(arg2 - 1)
									.get("_parent_id").toString()));
					bundle1.putString("class_list", categorylist.get(arg2 - 1)
							.get("_class_list").toString());
					bundle1.putInt(
							"class_layer",
							Integer.parseInt(categorylist.get(arg2 - 1)
									.get("_class_layer").toString()));
					bundle1.putInt(
							"sort_id",
							Integer.parseInt(categorylist.get(arg2 - 1)
									.get("_sort_id").toString()));
					bundle1.putString("link_url", categorylist.get(arg2 - 1)
							.get("_link_url").toString());
					bundle1.putString("img_url", categorylist.get(arg2 - 1)
							.get("_img_url").toString());
					bundle1.putString("content", categorylist.get(arg2 - 1)
							.get("_content").toString());
					bundle1.putString("seo_title", categorylist.get(arg2 - 1)
							.get("_seo_title").toString());
					bundle1.putString("seo_keywords", categorylist
							.get(arg2 - 1).get("_seo_keywords").toString());
					bundle1.putString("seo_description",
							categorylist.get(arg2 - 1).get("_seo_description")
									.toString());

					intent1.putExtras(bundle1);
					startActivity(intent1);
					overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
					myapplication1.setdiqu(categorylist.get(arg2 - 1)
							.get("_title").toString()); // 传递全局地区选择变量
				}
			});

			listview1.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub
					// 当不滚动时
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
						// 判断是否滚动到底部
						if (view.getLastVisiblePosition() == view.getCount() - 1) {
							if (havedata) {
								threadmore();
							}
						}
					}
					switch (scrollState) {
					case OnScrollListener.SCROLL_STATE_FLING:
						adapter.setFlagBusy(true);
						break;
					case OnScrollListener.SCROLL_STATE_IDLE:
						adapter.setFlagBusy(false);
						break;
					case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
						adapter.setFlagBusy(false);
						break;
					default:
						break;
					}
					adapter.notifyDataSetChanged();

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

				}
			});

			listview1.setonRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					if (havedata) {
						threadmore();
					}
					new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(Void... params) {
							// 因为这个列表加载不是在android中设置，而是通过服务器端获取，所以当page页超过时，服务端是没有数据显示的，为了避免程序的出错而对page进行了try判断一旦数据出问题了就不再刷新
							// 由于我现在水平有限对这个判断问题还没有得到妥善解决。

							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							try {
								listview1.onRefreshComplete();
							} catch (Exception e) {
								e.printStackTrace();
								ProgressDialog1.dismiss();
							}
						}

					}.execute();
				}

			});

		} catch (Exception e) {
			e.printStackTrace();
			mymethods.showMsg(DiquActivity.this, "无网络访问");
		}
	}

	public String localhost() {
		return myapplication1.getlocalhost();
	}

	/**
	 * 启动地图
	 */
	private void qdmap() {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < categorylist.size(); i++) {
			if (!"".equals(categorylist.get(i).get("_link_url").toString())) {
				arrayList.add(categorylist.get(i).get("_link_url").toString()
						+ "," + categorylist.get(i).get("_title").toString()
						+ ","
						+ categorylist.get(i).get("_seo_title").toString());
			}
		}
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		intent.setClass(DiquActivity.this, MapActivity.class);

		bundle.putStringArrayList("map", arrayList);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	/***
	 * 返回地区列表
	 */
	private void binddq() {
		try {
			dqlist = new ArrayList<HashMap<String, Object>>();
			
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("id", "1");
			item.put("title", "College Ave");
			dqlist.add(item);
			item = new HashMap<String, Object>();
			item.put("id", "2");
			item.put("title", "Busch");
			dqlist.add(item);
			item = new HashMap<String, Object>();
			item.put("id", "3");
			item.put("title", "Livingston");
			dqlist.add(item);
			item = new HashMap<String, Object>();
			item.put("id", "4");
			item.put("title", "Cook/Douglass");
			dqlist.add(item);
			dqadapter = new SimpleAdapter(DiquActivity.this, dqlist,
					R.layout.alertdialog_dq, new String[] { "title", "id" },
					new int[] { R.id.alertdialog_dq_textView1,
							R.id.alertdialog_dq_textView2 });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 配套使用加载读取
	 */
	public void threadstart() {
		ProgressDialog1 = new ProgressDialog(this);
		ProgressDialog1.setMessage("数据加载中，请稍后...");
		ProgressDialog1.show();
		Thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread1.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Handler1.sendEmptyMessage(0);
			}
		});
		Thread1.start();
	}

	private Handler Handler1 = new Handler() {
		public void handleMessage(Message msg) {
			try {
				list1 = loaddata(page);
				bindlist(list1);
				adapter = new categoryAdapter(DiquActivity.this, list1);
				adapter.addlist(list1);
				listview1.setAdapter(adapter);

				tabhost.getTabWidget().getChildAt(1)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								// tabhost.setCurrentTab(1);
								qdmap();

							}
						});

				ProgressDialog1.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
				ProgressDialog1.dismiss();
				Toast.makeText(DiquActivity.this, "网络不给力，无法获得活动信息!", 1).show();
			}
		}
	};

	/***
	 * 加载更多
	 */
	public void threadmore() {
		ProgressDialog1 = new ProgressDialog(this);
		ProgressDialog1.setMessage("数据加载中，请稍后...");
		ProgressDialog1.show();
		Thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread1.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Handler2.sendEmptyMessage(0);
			}
		});
		Thread1.start();
	}

	private Handler Handler2 = new Handler() {
		public void handleMessage(Message msg) {
			try {
				list1 = loaddata(page += 1);
				bindlist(list1);
				adapter.addlist(list1);
				adapter.notifyDataSetChanged();
				ProgressDialog1.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
				ProgressDialog1.dismiss();
				adapter.notifyDataSetChanged();
				Toast.makeText(DiquActivity.this, "已无数据记录", 1).show();
				havedata = false;
			}
		}
	};

	public void bindlist(List<category> addlist) {
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

	/***
	 * 加载json数据
	 */
	@SuppressLint("NewApi")
	
	public List<category> loaddata(int page) {

//		String temp_url = "http://172.31.104.28:8080/JsonProject/servlet/JsonAction?action_flag=Category";
		String temp_url = "http://172.31.104.28:8080/JsonProject/" + myapplication1.getcategoryurl();
		
//		String temp_url = "http://172.31.104.28:8080/JsonProject/" + "servlet/JsonAction?action_flag=Category&area_id=2";
		try {
//			Categorys = jsoncategory.getjsonlastcategory(temp_url);
			Categorys = jsoncategory.getJsonCategory(temp_url);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Categorys;
	}
	
//	public List<category> loaddata(int page) throws IOException {
//		List<category> list = new ArrayList<category>();
//
//		FileInputStream fis = openFileInput("tours");
//		BufferedInputStream bis = new BufferedInputStream(fis);
//		StringBuffer b = new StringBuffer();
//		while (bis.available() != 0) {
//			char c = (char) bis.read();
//			b.append(c);
//		}
//		bis.close();
//		fis.close();
//		try {
//			JSONArray jsonarray = new JSONArray(b.toString());
//			for (int i = 0; i < jsonarray.length(); i++) {
//				JSONObject jsonobject = jsonarray.getJSONObject(i);
//				int id = jsonobject.getInt("id");
//				int channel_id = jsonobject.getInt("channel_id");
//				String title = jsonobject.getString("title");
//				String call_index = jsonobject.getString("call_index");
//				int parent_id = jsonobject.getInt("parent_id");
//				String class_list = jsonobject.getString("class_list");
//				int class_layer = jsonobject.getInt("class_layer");
//				int sort_id = jsonobject.getInt("sort_id");
//				String link_url = jsonobject.getString("link_url");
//				String img_url = jsonobject.getString("img_url");
//				String content = jsonobject.getString("content");
//				String seo_title = jsonobject.getString("seo_title");
//				String seo_keywords = jsonobject.getString("seo_keywords");
//				String seo_description = jsonobject
//						.getString("seo_description");
//				list.add(new category(id, channel_id, title, call_index,
//						parent_id, class_list, class_layer, sort_id, link_url,
//						img_url, content, seo_title, seo_keywords,
//						seo_description));
//			}
//			return list;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return list;
//		}
//	}

	/**
	 * 打电话
	 * 
	 * @param tel
	 */
	public void calltel(String tel) {
		try {
			final String[] spStr = tel.split(",");
			new AlertDialog.Builder(this).setItems(spStr,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(
									"android.intent.action.CALL", Uri
											.parse("tel:" + spStr[which]));
							startActivity(intent);
						}
					}).show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& KeyEvent.KEYCODE_BACK == keyCode) {
			Intent intent = new Intent();
			intent.setClass(DiquActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
	
	/* create a experimental json file */
	public void create_json() throws JSONException, IOException {
		JSONArray data = new JSONArray();
		JSONObject tour;
		
//		tour = new JSONObject();
//		tour.put("_id", 100);
//		tour.put("_title", "la");
//		tour.put(name, value)
//		data.put(tour);
//		
//		tour = new JSONObject();
//		tour.put("_id", 101);
//		tour.put("_title", "wa");
//		data.put(tour);
		
		tour = new JSONObject();
		tour.put("id", 1);
		tour.put("channel_id", 1);
		tour.put("title", "1");
		tour.put("call_index", "1");
		tour.put("parent_id", 1);
		tour.put("class_list", "1");
		tour.put("class_layer", 1);
		tour.put("sort_id", 1);
		tour.put("link_url", "1");
		tour.put("img_url", "1");
		tour.put("content", "1");
		tour.put("seo_title", "1");
		tour.put("seo_keywords", "1");
		tour.put("seo_description", "1");
		data.put(tour);
		
		
		tour = new JSONObject();
		tour.put("id", 2);
		tour.put("channel_id", 2);
		tour.put("title", "2");
		tour.put("call_index", "2");
		tour.put("parent_id", 2);
		tour.put("class_list", "2");
		tour.put("class_layer", 2);
		tour.put("sort_id", 2);
		tour.put("link_url", "2");
		tour.put("img_url", "2");
		tour.put("content", "2");
		tour.put("seo_title", "2");
		tour.put("seo_keywords", "2");
		tour.put("seo_description", "2");
		data.put(tour);
		
		String text = data.toString();
		
		FileOutputStream fos = openFileOutput("tours", MODE_PRIVATE);
		fos.write(text.getBytes());
		fos.close();	
	}

}