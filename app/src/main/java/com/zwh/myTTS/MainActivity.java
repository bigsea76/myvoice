package com.zwh.myTTS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.speech.restapi.common.ConnUtil;
import com.baidu.speech.restapi.common.TokenHolder;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * 当前的时间戳，毫秒
     */
    private long expiresAt = 0;
    /**
     * 保存访问接口获取的token
     */
    private String token;
    // 发音人选择, 基础音库：0为度小美，1为度小宇，3为度逍遥，4为度丫丫，
    // 精品音库：5为度小娇，103为度米朵，106为度博文，110为度小童，111为度小萌，默认为度小美
    int per = 1;
    // 语速，取值0-15，默认为5中语速
    int spd = 5;
    // 音调，取值0-15，默认为5中语调
    int pit = 5;
    // 音量，取值0-9，默认为5中音量
    int vol = 9;

    // 下载的文件格式, 3：mp3(default) 4： pcm-16k 5： pcm-8k 6. wav
    int aue = 6;

    String url = "http://tsn.baidu.com/text2audio"; // 可以使用https

    //cuid
    String cuid = "1234567JAVA";

    String strTextSound = "";

    String[] sentances;
    String[] words;
    int intWhichWindows = 0;
    TextWatcher textWatcher =new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() != 0) {
                String str = "";
                try {
                    str =PinyinHelper.convertToPinyinString(String.valueOf(s), ",", PinyinFormat.WITH_TONE_MARK); // nǐ,hǎo,shì,jiè
                } catch (PinyinException e) {
                    e.printStackTrace();
                }
                TextView tv = (TextView) findViewById(R.id.textView5);
                tv.setText(str);
            }else {
                TextView tv = (TextView) findViewById(R.id.textView5);
                tv.setText("");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        intWhichWindows = 0;
        GridView gvsentence = (GridView) findViewById(R.id.gvsentence);
        gvsentence.setVisibility(View.VISIBLE);
        GridView gv = (GridView) findViewById(R.id.gv);
        gv.setVisibility(View.INVISIBLE);

        EditText et = (EditText) findViewById(R.id.editText);

        et.addTextChangedListener(textWatcher);
    }
    public void firevoice(View vw)
    {
        EditText et = (EditText) findViewById(R.id.editText);
        strTextSound = et.getText().toString();
        fireSound();
    }

    private String getFormat(int aue) {
        String[] formats = {"mp3", "pcm", "pcm", "wav"};
        return formats[aue - 3];
    }
    public void test(View vw)
    {
        Intent intent = new Intent(this, editwords.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        startActivity(intent);

    }

    public void cleartext(View vw)
    {
        EditText et = (EditText) findViewById(R.id.editText);
        et.setText("");
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TextView txv = (TextView)view;
        if(intWhichWindows == 0) {
            strTextSound = sentances[i];
        }
        else
        {
            strTextSound = words[i];
        }
        EditText et = (EditText) findViewById(R.id.editText);
        et.append(strTextSound);
        //Log.i("i",String.valueOf(i));
        //Log.i("l",String.valueOf(l));
        //Log.i("观察",strTextSound);
        fireSound();
    }
    private void fireSound()
    {
        new Thread( ) {
            @Override
            public void run() {
                try {
                    if(isExpire()) {
                        String appKey = "SITjbTdHeLDrQGzH9PYDoiBr";//  填写网页上申请的appkey 如 $apiKey="g8eBUMSokVB1BHGmgxxxxxx"
                        String secretKey = "u2ARh3ZSmddnyao6dOf9DKGxGOFTkECd";// 填写网页上申请的APP SECRET 如 $secretKey="94dc99566550d87f8fa8ece112xxxxx"
                        TokenHolder holder = new TokenHolder(appKey, secretKey, TokenHolder.ASR_SCOPE);
                        holder.refresh();
                        token =holder.getToken();
                        expiresAt = holder.getExpiresAt();
                        SharedPreferences.Editor  sharedata1=getSharedPreferences("data",0).edit();
                        sharedata1.putString("token",holder.getToken());
                        sharedata1.putLong("expiresAt",holder.getExpiresAt());
                        sharedata1.commit();
                    }

        // 此处2次urlencode， 确保特殊字符被正确编码

        String params = "tex=" + ConnUtil.urlEncode(ConnUtil.urlEncode(strTextSound));
        params += "&per=" + per;
        params += "&spd=" + spd;
        params += "&pit=" + pit;
        params += "&vol=" + vol;
        params += "&cuid=" + cuid;
        params += "&tok=" + token;
        params += "&aue=" + aue;
        params += "&lan=zh&ctp=1";
        //System.out.println(strTextSound);
        System.out.println(url + "?" + params); // 反馈请带上此url，浏览器上可以测试
        String urltmp = url + "?" + params;
        MediaPlayer mp = new MediaPlayer();
        //Uri ur;

          //  ur = Uri.parse(url);
            mp.setDataSource(urltmp);
            mp.prepare();
            mp.start();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private boolean isExpire()
    {
        long nowtime = System.currentTimeMillis();
        if(nowtime > (expiresAt - 5000))
            return true;
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedata= getSharedPreferences("data",0);
        String strsentences = sharedata.getString("sentences","我叫院洪\n我饿了\n我要上厕所\n我困了");
        String strwords = sharedata.getString("words","我\n你\n他\n她\n饿\n厕所\n困了\n电视机");
        token = sharedata.getString("token",null);
        expiresAt = sharedata.getLong("expiresAt",0);
        per = sharedata.getInt("per",1);
        spd = sharedata.getInt("spd",5);
        pit = sharedata.getInt("pit",5);
        vol = sharedata.getInt("vol",9);

        sentances = strsentences.split("\n");
        words = strwords.split("\n");

        GridView gvsentence = (GridView) findViewById(R.id.gvsentence);
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        for(int i = 0; i< sentances.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", sentances[i]);
            data_list.add(map);
        }
        String [] from ={"text"};
        int [] to = {R.id.text};
        SimpleAdapter sim_adapter = new SimpleAdapter(MainActivity.this, data_list, R.layout.item, from, to);
        gvsentence.setAdapter(sim_adapter);
        gvsentence.setOnItemClickListener(this);

        GridView gv = (GridView) findViewById(R.id.gv);
        List<Map<String, Object>> data_listwords = new ArrayList<Map<String, Object>>();
        for(int i = 0; i< words.length; i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", words[i]);
            data_listwords.add(map);
       }
        int [] towords = {R.id.textwords};
        sim_adapter = new SimpleAdapter(MainActivity.this, data_listwords, R.layout.itemwords, from, towords);
        gv.setAdapter(sim_adapter);
        gv.setOnItemClickListener(this);

    }

    public void sentance(View vw)
    {
        intWhichWindows = 0;
        GridView gvsentence = (GridView) findViewById(R.id.gvsentence);
        gvsentence.setVisibility(View.VISIBLE);
        GridView gv = (GridView) findViewById(R.id.gv);
        gv.setVisibility(View.INVISIBLE);
    }
    public void words(View vw)
    {
        intWhichWindows = 1;
        GridView gvsentence = (GridView) findViewById(R.id.gvsentence);
        gvsentence.setVisibility(View.INVISIBLE);
        GridView gv = (GridView) findViewById(R.id.gv);
        gv.setVisibility(View.VISIBLE);
    }

}
