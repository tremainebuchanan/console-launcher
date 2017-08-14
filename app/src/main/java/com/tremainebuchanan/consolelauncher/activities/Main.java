package com.tremainebuchanan.consolelauncher.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.tremainebuchanan.consolelauncher.R;
import com.tremainebuchanan.consolelauncher.adapters.CommandAdapter;
import com.tremainebuchanan.consolelauncher.models.Command;
import com.tremainebuchanan.consolelauncher.utilities.StringFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {
    private static String TAG = "HomeActivity";
    private static String PREF = "console";
    private static String COMMANDS = "commandList";
    private List<Command> commandList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommandAdapter mAdapter;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        final EditText command = (EditText) findViewById(R.id.command);
        recyclerView = (RecyclerView) findViewById(R.id.command_output);
        mAdapter = new CommandAdapter(commandList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        command.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    addCommand(command.getText().toString());
                    searchAndLaunchApp(command.getText().toString());
                    command.setText("");
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

    private void addCommand(String input){;
        Command command = new Command(input);
        commandList.add(command);
        int last_pos = mAdapter.getItemCount() - 1;
        recyclerView.scrollToPosition(last_pos);
    }

    private void searchAndLaunchApp( String user_input){
        boolean found = false;
        Intent intent = new Intent();
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo packageInfo: packages){
            if(StringFilter.contains(packageInfo.packageName, user_input.toLowerCase())){
                intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                found = true;
                break;
            }
        }
        if(found && intent != null){
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            addCommand("App not found");
        }
    }

    public void onResume(){
        super.onResume();
        sharedPreferences = getSharedPreferences(PREF, MODE_PRIVATE);
        String commands = sharedPreferences.getString(COMMANDS, null);
        if(commands != null && commandList.size() == 0 ){
            try{
                JSONArray commandsArray = new JSONArray(commands);
                for(int i = 0; i < commandsArray.length(); i++){
                    JSONObject command = commandsArray.getJSONObject(i);
                    addCommand(command.getString("user_input"));
                }
            }catch(JSONException e){
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void onPause(){
        super.onPause();
        Gson gson = new Gson();
        String jsonList = gson.toJson(commandList);
        sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(COMMANDS, jsonList);
        editor.apply();
    }
}

