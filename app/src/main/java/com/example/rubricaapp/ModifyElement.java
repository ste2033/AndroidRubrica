package com.example.rubricaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.view.WindowManager;

import com.example.rubricaapp.databinding.ActivityModifyElementsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class ModifyElement extends AppCompatActivity {
    private ActivityModifyElementsBinding binding;

    private static String _FILENAME = "rubrica.txt";
    private File _FILE = new File("");

    private Boolean _daModificare;
    private String _oldValue;

    @Override
    protected void onCreate(Bundle bundle) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(bundle);
        binding = ActivityModifyElementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadElements();

        binding.saveButton.setOnClickListener(view -> salvaElemento());
        binding.cancelButton.setOnClickListener(view -> exit());
        binding.deleteButton.setOnClickListener(view -> cancellaElemento());
    }

    public void salvaElemento() {
        String codice = binding.codiceInput.getText().toString();
        String nome = binding.nomeInput.getText().toString();
        String telefono = binding.telefonoInput.getText().toString();
        String note = binding.noteInput.getText().toString();

        // codice|nome|telefono|note

        codice = codice.replaceAll("\n", "§");
        nome = nome.replaceAll("\n", "§");
        telefono = telefono.replaceAll("\n", "§");
        note = note.replaceAll("\n", "§");

        String whatToWrite = codice + "|" + nome + "|" + telefono + "|" + note + "|";

        if (this._daModificare == true) {
            // prendi l'id dell'elemento nella lista e modificalo

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileInputStream fileInput = new FileInputStream(_FILE);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));

                    OutputStream outputStream = new FileOutputStream(_FILE, true);
                    Writer writer = new OutputStreamWriter(outputStream, "UTF-8");

                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.replace(_oldValue, whatToWrite);
                        writer.append(line);
                    }

                    reader.close();
                    writer.flush();
                    outputStream.flush();

                    writer.close();
                    outputStream.close();

                    finish();
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.relativeLayout), "Errore nella lettura del file!", Snackbar.LENGTH_LONG)
                        .show();
            }

        } else {
            try {
                OutputStream outputStream = new FileOutputStream(_FILE, true);
                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");

                writer.append("\n");
                writer.append(whatToWrite);

                writer.flush();
                outputStream.flush();

                writer.close();
                outputStream.close();

                finish();
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.relativeLayout), "La scrittura del file è andata male!",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void cancellaElemento() {

        if (this._daModificare == true) {

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FileInputStream fileInput = new FileInputStream(_FILE);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));

                    OutputStream outputStream = new FileOutputStream(_FILE, true);
                    Writer writer = new OutputStreamWriter(outputStream, "UTF-8");

                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = "";
                        writer.write(line);
                    }

                    reader.close();
                    writer.flush();
                    outputStream.flush();

                    writer.close();
                    outputStream.close();

                    exit();
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.relativeLayout), "Errore nella lettura del file!", Snackbar.LENGTH_LONG)
                        .show();
            }

        }

        finish();
    }

    public void exit() {
        finish();
    }

    private void searchElement() {

    }

    private void loadElements() {
        this._daModificare = getIntent().getBooleanExtra("daModificare", false);

        if (this._daModificare == true) {
            searchElement();

            binding.codiceInput.setText(getIntent().getStringExtra("codice"));
            binding.nomeInput.setText(getIntent().getStringExtra("nome"));
            binding.telefonoInput.setText(getIntent().getStringExtra("telefono"));
            binding.noteInput.setText(getIntent().getStringExtra("note"));

            String codice = binding.codiceInput.getText().toString().replaceAll("\n", "§");
            String nome = binding.nomeInput.getText().toString().replaceAll("\n", "§");
            String telefono = binding.telefonoInput.getText().toString().replaceAll("\n", "§");
            String note = binding.noteInput.getText().toString().replaceAll("\n", "§");

            this._oldValue = codice + "|" + nome + "|" + telefono + "|" + note + "|";
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
            List<StorageVolume> storageVolumeList = storageManager.getStorageVolumes();
            StorageVolume storageVolume = storageVolumeList.get(1); // 1 is for external SD Card. 0 is
            _FILE = new File(storageVolume.getDirectory().getAbsolutePath() + "/" + _FILENAME);
        }
    }
}