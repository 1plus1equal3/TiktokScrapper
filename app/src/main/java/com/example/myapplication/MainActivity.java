package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "https://www.tiktok.com/@livelifehacksxjp04/video/7200590258050698497";
        new MyTask().execute(url);

    }
}

class MyTask extends AsyncTask<String, String, String>{

    @Override
    protected String doInBackground(String... urls) {
        Log.e("Task", "Started");
        BufferedReader reader;
        FileWriter writer = null;
        StringBuilder str = null;
        File outputFile = new File("/sdcard/Documents/output.txt");
        try {
            if(outputFile.createNewFile()) Log.e("File", "created");
            else{
                Log.e("File", "already created");
                boolean delete = outputFile.delete();
                Log.e("File", "already created " + delete);
                if(delete) outputFile.createNewFile();
            }
            writer = new FileWriter("/sdcard/Documents/output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(urls[0]);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            str = new StringBuilder();
            String output;
            while ((output = reader.readLine()) != null) {
                str.append(output);
                if (writer != null) writer.write(output);
/*
                reader.close();
*/
                Log.e("Output", output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(str!=null)
        return str.toString();
        else{
            Log.e("Null", "Null");
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.e("Request success", s);
        StringBuilder line = new StringBuilder();
        try {
            File file = new File("/sdcard/Documents/output.txt");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line1;
            while((line1 = bufferedReader.readLine()) != null) line.append(line1);
//            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("Url###", findString(line.toString()));
        super.onPostExecute(s);
    }

    String findString(String line){
        String a = "downloadAddr";
        StringBuilder url = new StringBuilder();
        int b;
        if(!line.contains(a)) return null;
        b = line.indexOf(a) + 15;
        do{
            url.append(line.charAt(b));
            b++;
        } while (line.charAt(b) != '"');
        return url.toString();
    }
}