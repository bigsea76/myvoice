package com.zwh.myTTS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class editwords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editwords);

        SharedPreferences sharedata= getSharedPreferences("data",0);
        String strsentences = sharedata.getString("sentences","我叫院洪\n我饿了\n我要上厕所\n我困了");
        String strwords = sharedata.getString("words","我\n你\n他\n她\n饿\n厕所\n困了\n电视机");
        String token = sharedata.getString("token",null);
        int per = sharedata.getInt("per",1);
        int spd = sharedata.getInt("spd",5);
        int pit = sharedata.getInt("pit",5);
        int vol = sharedata.getInt("vol",9);

        EditText tv = (EditText) findViewById(R.id.editText2);
        tv.setText(strsentences);

        EditText tv1 = (EditText) findViewById(R.id.editTextwords);
        tv1.setText(strwords);

        Spinner sp = (Spinner)findViewById(R.id.spinnerman);
        int index = indexofper(per);
        sp.setSelection(index);

        sp = (Spinner)findViewById(R.id.spinnerspeed);
        sp.setSelection(spd);

        sp = (Spinner)findViewById(R.id.spinnertone);
        sp.setSelection(pit);

        sp = (Spinner)findViewById(R.id.spinnervolumn);
        sp.setSelection(vol);

    }
    public void savetext(View vw)
    {
        EditText tv = (EditText) findViewById(R.id.editText2);
        String strsentences = tv.getText().toString();

        EditText tv1 = (EditText) findViewById(R.id.editTextwords);
        String strwords = tv1.getText().toString();

        SharedPreferences.Editor  sharedata=getSharedPreferences("data",0).edit();
        sharedata.putString("sentences",strsentences);
        sharedata.putString("words",strwords);

        int[] manint= getResources().getIntArray(R.array.manint);
        Spinner sp = (Spinner)findViewById(R.id.spinnerman);
        int index = sp.getSelectedItemPosition();
        int per = manint[index];
        sharedata.putInt("per",per);

        sp = (Spinner)findViewById(R.id.spinnerspeed);
        index = sp.getSelectedItemPosition();
        sharedata.putInt("spd",index);

        sp = (Spinner)findViewById(R.id.spinnertone);
        index = sp.getSelectedItemPosition();
        sharedata.putInt("pit",index);

        sp = (Spinner)findViewById(R.id.spinnervolumn);
        index = sp.getSelectedItemPosition();
        sharedata.putInt("vol",index);

        sharedata.commit();
        finish();

    }
    public void cancel(View vw)
    {
        finish();
    }

    private int indexofper(int per)
    {
        int[] manint= getResources().getIntArray(R.array.manint);

        int index = 0;
        for(int i=0;i < manint.length;i++)
        {
            if(per==manint[i])
            {
                index =i;
                break;
            }
        }
        return index;
    }
}
