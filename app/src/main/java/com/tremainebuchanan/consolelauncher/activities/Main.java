package com.tremainebuchanan.consolelauncher.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.tremainebuchanan.consolelauncher.R;
import com.tremainebuchanan.consolelauncher.adapters.CommandAdapter;
import com.tremainebuchanan.consolelauncher.models.Command;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {
    private static String TAG = "HomeActivity";
    private List<Command> commandList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommandAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        final EditText command = (EditText) findViewById(R.id.command);
        recyclerView = (RecyclerView) findViewById(R.id.command_output);
        mAdapter = new CommandAdapter(commandList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        command.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.d(TAG, command.getText().toString());
                    addCommand(command.getText().toString());
                    //command.setText("");
                    //listApps();
                    launchApp("org.mozilla.firefox");
                    command.requestFocus();
                    return true;
                }
                return false;
            }
        });
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){  actionBar.hide(); }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
    }

    private void addCommand(String input){
        String prompt = ":~$";
        Command command = new Command(prompt + " " + input);
        commandList.add(command);
        mAdapter.notifyDataSetChanged();
    }

    private void listApps(){
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo packageInfo: packages){
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
    }

    private void launchApp( String name ){
        Intent intent = new Intent();
        intent.setPackage(name);
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

