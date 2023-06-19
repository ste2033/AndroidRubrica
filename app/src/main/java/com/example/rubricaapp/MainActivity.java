package com.example.rubricaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.WindowManager;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        askForPermissions();

        //riempi la lista
        readFullFile();

        binding.nuovoDatoButton.setOnClickListener(view -> aggiungiElemento());

        binding.listCodice.setOnClickListener(view -> modificaElemento());

        binding.listNome.setOnClickListener(view -> modificaElemento());

        binding.listTelefono.setOnClickListener(view -> modificaElemento());

        binding.listNote.setOnClickListener(view -> modificaElemento());
    }

    public void aggiungiElemento() {
        Intent intent = new Intent(this, ModifyElement.class);
        startActivity(intent);

        readFullFile();
    }

    public void modificaElemento() {
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



    private ArrayList<String> readFullFile() {
        ArrayList<String> data = new ArrayList<>();

        try {
            FileInputStream fileInput = new FileInputStream(_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));

            String line;
            while ((line = reader.readLine()) != null) {
                if(line != ""){
                    data.add(line);
                }
            }

            reader.close();
            fileInput.close();
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.relativeLayout), "Errore nella lettura del file!", Snackbar.LENGTH_LONG).show();
        }

        return data;
    }

}