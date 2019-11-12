package com.kuliah.komsi.storageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String FILENAME = "namafile.txt";
    public static final String PREFNAME = "com.kuliah.komsi.storageapp";
    private TextView textBaca;
    private EditText editFile;
    private static final String[] PERMISSIONS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textBaca = findViewById(R.id.text_baca);
        editFile = findViewById(R.id.edit_file);
    }

    //untuk mengecek permission
    private static boolean hasPermission(Context context, String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null){
            for (String permission:permissions){
                if(ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    //External Storage
    public void simpanFile(View view) {
        if (hasPermission(this, PERMISSIONS)){
            String isiFile = editFile.getText().toString();
            File path = Environment.getExternalStorageDirectory();
            File file = new File(path.toString(), FILENAME);
            FileOutputStream outputStream = null;

            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);
                outputStream.write(isiFile.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //popup request permission
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE);
        }

    }

    public void bacaFile(View view) {
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path.toString(), FILENAME);
        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line != null){
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            textBaca.setText(text.toString());
        }else{
            textBaca.setText("");
        }
    }

    public void hapusFile(View view) {
        File path = Environment.getExternalStorageDirectory();
        File file = new File(path.toString(), FILENAME);
        if (file.exists()){
            file.delete();
        }
    }



    //Internal Storage
    public void simpanFileIS(View view) {
        String isiFile = editFile.getText().toString();
        File path = getDir("ININAMAFOLDER", MODE_PRIVATE);
        File file = new File(path.toString(), FILENAME);
        FileOutputStream outputStream = null;

        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, false);
            outputStream.write(isiFile.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bacaFileIS(View view) {
        File path = getDir("ININAMAFOLDER", MODE_PRIVATE); //mengambil file pada direktiri
        File file = new File(path.toString(), FILENAME);
        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line != null){
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            textBaca.setText(text.toString());
        }else{
            textBaca.setText("");
        }
    }

    public void hapusFileIS(View view) {
        File path = getDir("ININAMAFOLDER", MODE_PRIVATE); //mengambil file pada direktiri
        File file = new File(path.toString(), FILENAME);
        if (file.exists()){
            file.delete();
        }
    }

    //Shared preferences
    public void simpanFilePref(View view) {
        String isiFile = editFile.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FILENAME, isiFile);
        editor.commit();
    }

    public void bacaFilePref(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        if (sharedPreferences.contains(FILENAME)){
            String myText = sharedPreferences.getString(FILENAME, "");
            textBaca.setText(myText);
        }else{
            textBaca.setText("");
        }
    }

    public void hapusFilePref(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFNAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
