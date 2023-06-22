package com.example.rubricaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.example.rubricaapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private static String _FILENAME = "rubrica.txt";
    private static String _SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    private File _FILE = new File(_SDPATH + "/" + File.separator + _FILENAME);

    public ArrayList<Rubrica> rubricaArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mListView = (ListView) findViewById(R.id.listView);

        ArrayList<Rubrica> rubricaList = new ArrayList<>();

        for(int i = 0; i<10;i++){
            String intString = Integer.toString(i);

            Rubrica nuovo = new Rubrica(intString,intString,intString,intString);
            rubricaList.add(nuovo);
        }

        RubricaListAdapter adapter = new RubricaListAdapter(this, R.layout.adapter_view_layout, rubricaList);
        mListView.setAdapter(adapter);


        askForPermissions();

        //riempi la lista
        readFullFile();

        Button nuovoDato = findViewById(R.id.nuovoDatoButton);

        nuovoDato.setOnClickListener(view -> aggiungiElemento());
    }


    public void aggiungiElemento() {
        Intent intent = new Intent(this, ModifyElement.class);
        startActivity(intent);

        readFullFile();
    }

    public void modificaElemento(View view) {
        Intent intent = new Intent(this, ModifyElement.class);
        startActivity(intent);

        readFullFile();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                createFile();
            }
            readFullFile();
        }
    }

    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
            createFile();
        }
    }

    public void createFile() {
        if (!_FILE.exists()) {
            try {
                _FILE.createNewFile();
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.relativeLayout), "Il file non è stato creato!", Snackbar.LENGTH_LONG).show();
            }
        }
    }



    private void readFullFile() {

        rubricaArrayList = new ArrayList<>();
/*
        try {
            FileInputStream fileInput = new FileInputStream(_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));

            String line;
            while ((line = reader.readLine()) != null) {
                if(line != ""){
                    String[] data = line.split("|");
                    Rubrica rubrica = new Rubrica(data[0],data[1],data[2],data[3]);
                    rubricaArrayList.add(rubrica);
                }
            }

            reader.close();
            fileInput.close();
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.relativeLayout), "Errore nella lettura del file!", Snackbar.LENGTH_LONG).show();
        }*/
    }

}