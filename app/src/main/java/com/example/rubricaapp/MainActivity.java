package com.example.rubricaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String FILENAME = "rubrica.txt";
    private File _file = new File("");
    public ArrayList<Rubrica> _rubricaArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        richiediPermessi();

        leggiFile();

        Button nuovoDato = findViewById(R.id.nuovoDatoButton);
        nuovoDato.setOnClickListener(view -> aggiungiElemento());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Calling this public method will prevent the android.support.v4.app.SuperNotCalledException
        // when we don't immediately call the super.onDestroy - since it is in an endAction

        finishAndRemoveTask();
    }


    /**
     * Chiamato quando viene cliccato il bottone di aggiunta elemento
     * */
    public void aggiungiElemento() {
        Intent intent = new Intent(this,  ModifyElement.class);

        startActivity(intent);

        leggiFile();
    }

    /**
     * Chiamato quando viene cliccato un elemento già esistente
     */
    public void modificaElemento(View view) {
        Intent intent = new Intent(this,  ModifyElement.class);
        intent = fillIntent(view, intent);

        startActivity(intent);

        leggiFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                createFile();
            }
            leggiFile();
        }
    }

    /** Richiedi i permessi dell'applicazione
     * I permessi riguardano lettura e scrittura
     * su scheda SD
     */
    private void richiediPermessi() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
        {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }

            StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
            List<StorageVolume> storageVolumeList = storageManager.getStorageVolumes();
            StorageVolume storageVolume = storageVolumeList.get(1); // 1 is for external SD Card. 0 is for internal memory

            _file = new File(storageVolume.getDirectory().getAbsolutePath() +"/"+ FILENAME);

            createFile();
        }
    }


    /**
     * Crea il file (se non esiste già)
     */
    public void createFile() {
        if (_file.exists() == false)
        {
            try {
                _file.createNewFile();
            } catch (Exception e) {
                Snackbar.make(this.getCurrentFocus(), "ERRORE CREAZIONE FILE!", Snackbar.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Legge il file e riempie l'arrayList
     */
    private void leggiFile() {

        ListView listViewInput = findViewById(R.id.listView);
        _rubricaArrayList = new ArrayList<>();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                FileInputStream fileInput = new FileInputStream(_file);
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

                        if(data.length == 4) {
                            Rubrica rubrica = new Rubrica(data[0], data[1], data[2], data[3]);
                            _rubricaArrayList.add(rubrica);
                        }
                    }
                }

                reader.close();
                fileInput.close();
            }
        } catch (Exception e) {
            Snackbar.make(this.getCurrentFocus(), "LETTURA DEL FILE FALLITA!", Snackbar.LENGTH_LONG).show();
        }


        RubricaListAdapter adapter = new RubricaListAdapter(this, R.layout.adapter_view_layout, this._rubricaArrayList);
        listViewInput.setAdapter(adapter);
    }

    private Intent fillIntent(View view, Intent intent){

        intent.putExtra("daModificare", true);

        String codice = ((TextView) view.findViewById(R.id.listCodice)).getText().toString();
        String nome = ((TextView) view.findViewById(R.id.listNome)).getText().toString();
        String telefono = ((TextView) view.findViewById(R.id.listTelefono)).getText().toString();
        String note = ((TextView) view.findViewById(R.id.listNote)).getText().toString();

        intent.putExtra("codice", codice);
        intent.putExtra("nome", nome);
        intent.putExtra("telefono", telefono);
        intent.putExtra("note", note);

        return intent;
    }
}