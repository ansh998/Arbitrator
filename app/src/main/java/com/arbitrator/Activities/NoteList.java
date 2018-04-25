package com.arbitrator.Activities;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler2;
import com.arbitrator.R;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class NoteList extends AppCompatActivity {

    ListView notelist;

    String u, user, ud[][];
    SharedPreferences spu;
    SharedPreferences.Editor spue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notelist = (ListView) findViewById(R.id.notelist);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        getnotes();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editno = new Intent(getApplicationContext(), NotesEdit.class);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                NotesEdit.nId = s.format(c.getTime());
                Calendar cc = Calendar.getInstance();
                SimpleDateFormat ss = new SimpleDateFormat("dd-MMM--yyyy");
                NotesEdit.nDate = ss.format(cc.getTime());
                editno.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(editno);
                finish();
            }
        });

        notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (notelist.getItemAtPosition(0).toString().equalsIgnoreCase("No Notes yet!")) {
                        Intent editno = new Intent(getApplicationContext(), NotesEdit.class);
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        NotesEdit.nId = s.format(c.getTime());
                        Calendar cc = Calendar.getInstance();
                        SimpleDateFormat ss = new SimpleDateFormat("dd-MMM-yyyy");
                        NotesEdit.nDate = ss.format(cc.getTime());
                        editno.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(editno);
                        finish();
                    } else {
                        Intent editno = new Intent(getApplicationContext(), NotesEdit.class);
                        NotesEdit.nDate = ud[position][2];
                        NotesEdit.nId = ud[position][1];
                        NotesEdit.nData = ud[position][3];
                        NotesEdit.flag = 1;
                        editno.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(editno);
                        finish();
                    }
                } else {
                    Intent editno = new Intent(getApplicationContext(), NotesEdit.class);
                    NotesEdit.nDate = ud[position][2];
                    NotesEdit.nId = ud[position][1];
                    NotesEdit.nData = ud[position][3];
                    NotesEdit.flag = 1;
                    editno.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(editno);
                    finish();
                }
            }
        });
    }

    private void getnotes() {
        ArrayList li = new ArrayList();
        try {
            u = getResources().getString(R.string.url);
            user = getResources().getString(R.string.user);
            spu = getSharedPreferences(user, Context.MODE_PRIVATE);
            spue = spu.edit();
            String arr[][] = null;
            Helper pa = new Helper(u + "note/" + spu.getString("id", "-1"), 1, arr, getApplicationContext());
            JsonHandler2 jh = new JsonHandler2();
            JSONArray jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            ud = new String[jo.length()][4];
            for (int i = 0; i < jo.length(); i++) {
                ud[i][0] = (jo.getJSONObject(i).getString("id"));
                ud[i][1] = (jo.getJSONObject(i).getString("note_id"));
                ud[i][2] = (jo.getJSONObject(i).getString("note_date"));
                ud[i][3] = (jo.getJSONObject(i).getString("note_data"));
            }

            if (ud.length > 0) {
                for (int i = 0; i < ud.length; i++) {
                    if (ud[i][3].length() > 30)
                        li.add(ud[i][3].substring(0, 30) + "...");
                    else
                        li.add(ud[i][3] + "...");
                }
            } else
                li.add("No Notes yet!");

            final ArrayAdapter ada = new ArrayAdapter(this, android.R.layout.simple_list_item_1, li);

            notelist.setAdapter(ada);

        } catch (Exception e) {

        }

    }

}
