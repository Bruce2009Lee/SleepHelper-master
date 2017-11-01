package com.example.developerhaoz.sleephelper.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developerhaoz.sleephelper.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Administrator on 2017/10/29.
 */

public class ActivityTestSaveToFile extends AppCompatActivity implements View.OnClickListener{

    private EditText mEditText;
    private Button mButton;
    private TextView mTextView;
    String fileString;

    private static final String TAG = "TestSave";
    private static final String MY_JSON = "[\n" +
            "  {\"id\":\"1\",\"version\":\"1.5\",\"name\":\"Apple\"},\n" +
            "  {\"id\":\"2\",\"version\":\"1.6\",\"name\":\"WillFlow\"},\n" +
            "  {\"id\":\"3\",\"version\":\"1.7\",\"name\":\"WGH\"}\n" +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savetofile_main);

        mEditText = (EditText) findViewById(R.id.edit_file);
        mButton = (Button) findViewById(R.id.button_save);
        mButton.setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.tv_dataFromFile);

    }

    public void saveToFile(String sourceString) {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = openFileOutput("fileData", Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(sourceString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String loadFromFile() {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileInputStream = openFileInput("fileData");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String readLine = "";
            while ((readLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    private void parseByGSON(String jsonData) {
        Gson gson = new Gson();
        List<AppBean> appList = gson.fromJson(jsonData, new TypeToken<List<AppBean>>() {}.getType());
        for (AppBean app : appList) {
            Log.i(TAG, "id : " + app.getId());
            Log.i(TAG, "name : " + app.getName());
            Log.i(TAG, "version : " + app.getVersion());
            Log.i(TAG, "————————————————————");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveToFile(mEditText.getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save:
                saveToFile(mEditText.getText().toString());
                Toast.makeText(this,"save success",Toast.LENGTH_SHORT).show();
                fileString = loadFromFile();
                if (!TextUtils.isEmpty(fileString) && mTextView.getText().toString() != "To be show" ) {
                    mTextView.setText(fileString);
                    Toast.makeText(ActivityTestSaveToFile.this, "获得文件文本：" + fileString, Toast.LENGTH_SHORT).show();
                }
                parseByGSON(MY_JSON);
                break;
        }
    }
}
