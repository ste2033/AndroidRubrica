package com.example.rubricaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.rubricaapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private static String _FILENAME = "rubrica.txt";
    private static String _SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private File _FILE = new File(_SDPATH + "/" + File.separator + _FILENAME);

    public ArrayList<Rubrica> _rubricaArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForPermissions();

        //riempi la lista
        readFullFile();

        Button nuovoDato = findViewById(R.id.nuovoDatoButton);
        nuovoDato.setOnClickListener(view -> aggiungiElemento());

    }


    public void aggiungiElemento() {
        Intent intent = new Intent(getBaseContext(), ModifyElement.class);
        intent.putExtra("daModificare",true);

        startActivity(intent);

        readFullFile();
    }


    public void modificaElemento(View view) {
        Intent intent = new Intent(this,  ModifyElement.class);
        intent.putExtra("_DAMODIFICARE", true);

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



    /*
     * Legge il file e riempie l'arrayList
     */
    private void readFullFile() {

        ListView mListView = findViewById(R.id.listView);
        _rubricaArrayList = new ArrayList<>();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                FileInputStream fileInput = new FileInputStream(_FILE);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    if(line != "")
                    {
                        String[] data = line.split("\\|");
                        for(int i = 0; i < data.length; i++){
                            data[i] = data[i].replace('§', '\n');
                        }
                        
                        Rubrica rubrica = new Rubrica(data[0],data[1],data[2],data[3]);
                        _rubricaArrayList.add(rubrica);
                    }
                }

                reader.close();
                fileInput.close();
            }
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.relativeLayout), "Errore nella lettura del file!", Snackbar.LENGTH_LONG).show();
        }


        RubricaListAdapter adapter = new RubricaListAdapter(this, R.layout.adapter_view_layout, this._rubricaArrayList);
        mListView.setAdapter(adapter);
    }

}