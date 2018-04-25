package com.arbitrator.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.R;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class NotesEdit extends AppCompatActivity {

    public static String nId = "", nData = "", nDate = "";
    public static int flag = 0;

    Button can, ok;
    String u, user;
    TextView name;
    EditText note;
    SharedPreferences spu;
    SharedPreferences.Editor spue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_edit);

        can = (Button) findViewById(R.id.btn_cannot);
        ok = (Button) findViewById(R.id.btn_oknot);
        name = (TextView) findViewById(R.id.tv1);
        note = (EditText) findViewById(R.id.et12);
        u = getResources().getString(R.string.url);
        user = getResources().getString(R.string.user);
        spu = getSharedPreferences(user, Context.MODE_PRIVATE);
        spue = spu.edit();

        name.setText(nDate);
        note.setText(nData);

        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 1)
                    sendnote();
                else
                    newnote();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //sending notes to Middleware server
    private void sendnote() {
        try {
            String arr[][] = {
                    {"note_id", nId},
                    {"id", spu.getString("id", "-1")},
                    {"note_date", nDate},
                    {"note_data", note.getText().toString()}
            };
            Helper pa = new Helper(u + "note/1", 4, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            JSONObject jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            if (jo.isNull("error")) {
                Toast.makeText(getApplicationContext(), "Note Saved Successfully!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Unable to process your request right now!", Toast.LENGTH_LONG).show();
                Log.e("sendnote", jo.getString("error"));
            }
        } catch (Exception e) {
            Log.e("sendnote", "down");
            e.printStackTrace();
        }
    }

    //sending new notes
    private void newnote() {
        try {
            String arr[][] = {
                    {"note_id", nId},
                    {"id", spu.getString("id", "-1")},
                    {"note_date", nDate},
                    {"note_data", note.getText().toString()}
            };
            Helper pa = new Helper(u + "note", 2, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            JSONObject jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            if (jo.isNull("error")) {
                Toast.makeText(getApplicationContext(), "Note Inserted Successfully!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Unable to process your request right now!", Toast.LENGTH_LONG).show();
                Log.e("newnote", jo.getString("error"));
            }
        } catch (Exception e) {
            Log.e("newnote", "down");
            e.printStackTrace();
        }
    }
}
