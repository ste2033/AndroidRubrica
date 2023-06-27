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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ModifyElement extends AppCompatActivity {
    private ActivityModifyElementsBinding binding;

    private final String _CHARSET = "UTF-8";
    private final String _FILENAME = "rubrica.txt";
    private final String _TEMPFILE = "tempFile";
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

                    File temp = File.createTempFile(_TEMPFILE, ".txt", _FILE.getParentFile());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_FILE), _CHARSET));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), _CHARSET));

                    for (String line; (line = reader.readLine()) != null;) {
                        line = line.replace(_oldValue, whatToWrite);
                        writer.println(line);
                    }

                    reader.close();
                    writer.close();

                    _FILE.delete();

                    temp.renameTo(_FILE);

                    finish();
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.relativeLayout), "LETTURA DEL FILE FALLITA!", Snackbar.LENGTH_LONG)
                        .show();
            }
        } else {
            try {
                OutputStream outputStream = new FileOutputStream(_FILE, true);
                Writer writer = new OutputStreamWriter(outputStream, _CHARSET);

                writer.append(whatToWrite);
                writer.append("\n");

                writer.flush();
                outputStream.flush();

                writer.close();
                outputStream.close();

                finish();
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.relativeLayout), "ERRORE IN FASE DI SCRITTURA!",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public void cancellaElemento() {

        if (this._daModificare == true)
        {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    File temp = File.createTempFile(_TEMPFILE, ".txt", _FILE.getParentFile());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_FILE), _CHARSET));
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), _CHARSET));

                    for (String line; (line = reader.readLine()) != null;) {
                        line = line.replace(_oldValue, "");
                        writer.println(line);
                    }

                    reader.close();
                    writer.close();

                    _FILE.delete();

                    temp.renameTo(_FILE);

                    finish();
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.relativeLayout), "LETTURA DEL FILE FALLITA!", Snackbar.LENGTH_LONG)
                        .show();
            }
        }

        finish();
    }

    /*todo: Need to check if value is empty (otherwhise do nothing)
    *  Refactoring*/


    public void exit() {
        finish();
    }

    private void loadElements() {
        this._daModificare = getIntent().getBooleanExtra("daModificare", false);

        if (this._daModificare == true) {

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