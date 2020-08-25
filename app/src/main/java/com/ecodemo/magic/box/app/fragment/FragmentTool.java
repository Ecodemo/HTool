package com.ecodemo.magic.box.app.fragment;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.PopupMenu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ecodemo.magic.box.BrowserActivity;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.ScriptItem;
import com.ecodemo.magic.box.app.adapter.GridAdapter;
import com.ecodemo.magic.box.app.adapter.ScriptAdapter;
import com.ecodemo.magic.box.app.adapter.Tool;
import com.ecodemo.magic.box.app.adapter.ToolType;
import com.ecodemo.magic.box.app.tools.AESEncode;
import com.ecodemo.magic.box.app.tools.Base64;
import com.ecodemo.magic.box.app.tools.Color.ColorSelector;
import com.ecodemo.magic.box.app.tools.CompassActivity;
import com.ecodemo.magic.box.app.tools.CurrentActivity;
import com.ecodemo.magic.box.app.tools.DESEncode;
import com.ecodemo.magic.box.app.tools.Decomment;
import com.ecodemo.magic.box.app.tools.GuaranteeDate;
import com.ecodemo.magic.box.app.tools.HtmlFormat;
import com.ecodemo.magic.box.app.tools.ImageToBase64;
import com.ecodemo.magic.box.app.tools.JsRun;
import com.ecodemo.magic.box.app.tools.Md5;
import com.ecodemo.magic.box.app.tools.MorseCoder;
import com.ecodemo.magic.box.app.tools.Ping;
import com.ecodemo.magic.box.app.tools.RC4;
import com.ecodemo.magic.box.app.tools.RandomActivity;
import com.ecodemo.magic.box.app.tools.SHA1;
import com.ecodemo.magic.box.app.tools.SoundActivity;
import com.ecodemo.magic.box.app.tools.Translate;
import com.ecodemo.magic.box.app.tools.Unicode;
import com.ecodemo.magic.box.app.tools.Whois;
import com.ecodemo.magic.box.app.utils.IToast;
import com.ecodemo.magic.box.app.utils.RunScriptApp;
import com.ecodemo.magic.box.app.widget.CustomeGridView;
import com.ecodemo.magic.qrcode.android.CaptureActivity;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentTool extends Fragment implements OnItemClickListener {
	public static String TAG = "TOOL_FRAGMENT";
	public FragmentTool() {}
    public static FragmentTool newInstance() {
        FragmentTool fragment=new FragmentTool();
        return fragment;
    }
	private CustomeGridView life;
	private CustomeGridView system;
	private CustomeGridView geek;
	private CustomeGridView music;
	private CustomeGridView program;
	private EditText search;
	private static List<Tool> tools = new ArrayList<>();
	private List<Tool> t = new ArrayList<>();
	private List<Tool> s = new ArrayList<>();
	private List<Tool> g = new ArrayList<>();
	private List<Tool> o = new ArrayList<>();
	private GridAdapter lga;
	private GridAdapter sga;
	private GridAdapter gga;
	private GridAdapter mga;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					for (Tool tool: tools) {
						if (tool.getType() == ToolType.TOOL) {
							t.add(tool);
						} else if (tool.getType() == ToolType.SYSTEM) {
							s.add(tool);
						} else if (tool.getType() == ToolType.GEEK) {
							g.add(tool);
						} else if (tool.getType() == ToolType.ONLINE) {
							o.add(tool);
						}
					}
					lga = new GridAdapter(getActivity(), t);
					sga = new GridAdapter(getActivity(), s);
					gga = new GridAdapter(getActivity(), g);
					mga = new GridAdapter(getActivity(), o);
					life.setAdapter(lga);
					system.setAdapter(sga);
					geek.setAdapter(gga);
					music.setAdapter(mga);
					break;
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tool, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		life = getActivity().findViewById(R.id.life);
		system = getActivity().findViewById(R.id.system);
		geek = getActivity().findViewById(R.id.geek);
		music = getActivity().findViewById(R.id.music);
		program = getActivity().findViewById(R.id.program);
		search = getActivity().findViewById(R.id.search);
		life.setOnItemClickListener(this);
		system.setOnItemClickListener(this);
		geek.setOnItemClickListener(this);
		music.setOnItemClickListener(this);
		search.addTextChangedListener(new TextWatcher(){
				private PopupMenu popup;
				@Override
				public void beforeTextChanged(CharSequence cs, int p2, int p3, int p4) {
				}
				@Override
				public void onTextChanged(CharSequence cs, int p2, int p3, int p4) {
				}
				@Override
				public void afterTextChanged(final Editable e) {
					if (TextUtils.isEmpty(e))return;
					popup = new PopupMenu(getActivity(), search);
					getActivity().runOnUiThread(new Runnable(){
							@Override
							public void run() {
								Pattern pattern = Pattern.compile(e.toString());
								for (int i = 0;i < tools.size();i++) {
									Matcher matcher = pattern.matcher(tools.get(i).getName());
									if (matcher.find()) {
										popup.getMenu().add(tools.get(i).getName());
									}
								}
								popup.show();
							}
						});
				}
			});
		handler.sendEmptyMessage(0);
	}
	

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long p4) {
		Class<?> activity = null;
	    String url = null;
		switch ((String)view.getTag()) {
			case "开发者浏览器":
				activity = BrowserActivity.class;
				break;
			case "噪声测试":
				activity = SoundActivity.class;
				break;
			case "调色板":
				activity = ColorSelector.class;
				break;
			case "Base64编码转换":
				activity = Base64.class;
				break;
			case "MD5加密":
				activity = Md5.class;
				break;
			case "RC4加密":
				activity = RC4.class;
				break;
			case "AES加密":
				activity = AESEncode.class;
				break;
			case "DES加密":
				activity = DESEncode.class;
				break;
			case "SHA1加密":
				activity = SHA1.class;
				break;
			case "Unicode编码转换":
				activity = Unicode.class;
				break;
			case "指南针":
				activity = CompassActivity.class;
				break;
			case "图片转编码":
				activity = ImageToBase64.class;
				break;
			case "当前界面":
				activity = CurrentActivity.class;
				break;
			case "保质期计算":
				activity = GuaranteeDate.class;
				break;
			case "随机数生成":
				activity = RandomActivity.class;
				break;
			case "代码去注释":
				activity = Decomment.class;
				break;
			case "HTML格式化":
				activity = HtmlFormat.class;
				break;
			case "扫一扫":
				activity = CaptureActivity.class;
				break;
			case "语言翻译":
				activity = Translate.class;
				break;
			case "Ping":
				activity = Ping.class;
				break;
			case "Whois查询":
				activity = Whois.class;
				break;
			case "摩斯电码":
				activity = MorseCoder.class;
				break;
			case "智能计算器":
				url = "https://www.zybang.com/static/question/m-calculator/m-calculator.html?ZybStaticTitle=%E6%99%BA%E8%83%BD%E8%AE%A1%E7%AE%97%E5%99%A8";
			    break;
			case "以图搜图(百度)":
			    url = "https://graph.baidu.com/view/home";
				break;
			case "以图搜图(搜狗)":
			    url = "https://pic.sogou.com/";
				break;
			case "以图搜图(360)":
			    url = "https://image.so.com/";
				break;
			case "程序员的工具箱":
			    url = "https://tool.lu/";
				break;
			case "运行JS":
				activity = JsRun.class;
				break;
			default:
				IToast.makeText(view.getContext(), "此功能程序员正在辛勤的开发中(๑˃̵ᴗ˂̵)و ", IToast.LENGTH_SHORT).show();
				return;
		}
		if (activity != null) {
			Intent intent = new Intent(view.getContext(), activity);
			intent.putExtra("title", (String) view.getTag());
			view.getContext().startActivity(intent);
		} else if (url != null) {
			Intent intent = new Intent(view.getContext(), BrowserActivity.class);
			intent.setData(Uri.parse(url));
			view.getContext().startActivity(intent);
		}
	}

	static{
		tools.add(new Tool("指南针", ToolType.TOOL));
		tools.add(new Tool("噪声测试", ToolType.TOOL));
		tools.add(new Tool("语言翻译", ToolType.TOOL));
		tools.add(new Tool("图片加水印", ToolType.TOOL));
		tools.add(new Tool("保质期计算", ToolType.TOOL));
		tools.add(new Tool("身份证号码查询", ToolType.TOOL));
		tools.add(new Tool("号码归属地查询", ToolType.TOOL));
		tools.add(new Tool("随机数生成", ToolType.TOOL));
		tools.add(new Tool("储存单位转换", ToolType.TOOL));
		tools.add(new Tool("汇率换算", ToolType.TOOL));
		tools.add(new Tool("快递查询", ToolType.TOOL));
		tools.add(new Tool("垃圾分类", ToolType.TOOL));
		tools.add(new Tool("扫一扫", ToolType.TOOL));
		tools.add(new Tool("APK管理器", ToolType.SYSTEM));
		tools.add(new Tool("开发者浏览器", ToolType.GEEK));
		tools.add(new Tool("调色板", ToolType.GEEK));
		tools.add(new Tool("Base64编码转换", ToolType.GEEK));
		tools.add(new Tool("Unicode编码转换", ToolType.GEEK));
		tools.add(new Tool("RC4加密", ToolType.GEEK));
		tools.add(new Tool("MD5加密", ToolType.GEEK));
		tools.add(new Tool("AES加密", ToolType.GEEK));
		tools.add(new Tool("DES加密", ToolType.GEEK));
		tools.add(new Tool("SHA1加密", ToolType.GEEK));
		tools.add(new Tool("POST/GET测试", ToolType.GEEK));
		tools.add(new Tool("图片转编码", ToolType.GEEK));
		tools.add(new Tool("当前界面", ToolType.GEEK));
		tools.add(new Tool("摩斯电码", ToolType.GEEK));
		tools.add(new Tool("代码去注释", ToolType.GEEK));
		tools.add(new Tool("IP查询", ToolType.GEEK));
		tools.add(new Tool("Whois查询", ToolType.GEEK));
		tools.add(new Tool("端口扫描", ToolType.GEEK));
		tools.add(new Tool("Ping", ToolType.GEEK));
		tools.add(new Tool("DNS反查", ToolType.GEEK));
		tools.add(new Tool("HTML格式化", ToolType.GEEK));
		tools.add(new Tool("运行JS", ToolType.GEEK));
		tools.add(new Tool("智能计算器", ToolType.ONLINE));
		tools.add(new Tool("以图搜图(百度)", ToolType.ONLINE));
		tools.add(new Tool("以图搜图(搜狗)", ToolType.ONLINE));
		tools.add(new Tool("以图搜图(360)", ToolType.ONLINE));
		tools.add(new Tool("程序员的工具箱", ToolType.ONLINE));
	}
}
