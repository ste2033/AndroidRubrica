package com.example.rubricaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;

import com.example.rubricaapp.databinding.ActivityModifyElementsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ModifyElement extends AppCompatActivity {
    private ActivityModifyElementsBinding binding;

    private static String _FILENAME = "rubrica.txt";
    private static String _SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private File _FILE = new File(_SDPATH + "/" + File.separator + _FILENAME);

    private Boolean _daModificare;
    private int _index;


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

        //codice|nome|telefono|note

        if (codice.isEmpty() == true || nome.isEmpty() == true || telefono.isEmpty() == true || note.isEmpty() == true) {
            Snackbar.make(findViewById(R.id.relativeLayout), "RIEMPIRE TUTTI I CAMPI!", Snackbar.LENGTH_LONG).show();
        }
        else
        {
            codice = codice.replaceAll("\n", "§");
            nome = nome.replaceAll("\n", "§");
            telefono = telefono.replaceAll("\n", "§");
            note = note.replaceAll("\n", "§");


            String whatToWrite = codice + "|" + nome + "|" + telefono + "|" + note + "|";

            if (false) {
                //prendi l'id dell'elemento nella lista e modificalo
            }
            else
            {
                try {
                    OutputStream outputStream = new FileOutputStream(_FILE, true);
                    Writer writer = new OutputStreamWriter(outputStream, "UTF-8");

                    writer.append(whatToWrite);

                    writer.flush();
                    outputStream.flush();

                    writer.close();
                    outputStream.close();

                    exit();
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.relativeLayout), "La scrittura del file è andata male!", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    public void cancellaElemento() {

        exit();
    }


    public void exit() {
        finish();
    }

    private void searchElement(){

    }

    private void loadElements() {
        this._daModificare = getIntent().getBooleanExtra("daModificare",false);

        if(this._daModificare == true) {
            searchElement();

            binding.codiceInput.setText("");
            binding.nomeInput.setText("");
            binding.telefonoInput.setText("");
            binding.noteInput.setText("");
        }
    }
}