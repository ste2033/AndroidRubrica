package com.example.rubricaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
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

    private final String CHARSET = "UTF-8";
    private final String FILENAME = "rubrica.txt";
    private final String TEMPFILE = "tempFile";
    private File FILE = new File("");

    private Boolean daModificare;
    private String oldValue;

    @Override
    protected void onCreate(Bundle bundle) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(bundle);
        binding = ActivityModifyElementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        caricaElemento();

        binding.saveButton.setOnClickListener(view -> salvaElemento());
        binding.cancelButton.setOnClickListener(view -> exit());
        binding.deleteButton.setOnClickListener(view -> cancellaElemento());
    }

    public void salvaElemento() {

        String codice = binding.codiceInput.getText().toString();
        String nome = binding.nomeInput.getText().toString();
        String telefono = binding.telefonoInput.getText().toString();
        String note = binding.noteInput.getText().toString();

        codice = replaceText(codice);
        nome = replaceText(nome);
        telefono = replaceText(telefono);
        note = replaceText(note);

        // codice|nome|telefono|note
        String whatToWrite = codice + "|" + nome + "|" + telefono + "|" + note + "|";

        if(codice.equals("") == true || nome.equals("") == true || telefono.equals("") == true || note.equals("") == true){
            Snackbar.make(this.getCurrentFocus(), "RIEMPIRE TUTTI I CAMPI!",Snackbar.LENGTH_LONG).show();
        }
        else{
            if (this.daModificare == true) {
                createTempFile(whatToWrite);
            }
            else
            {
                try {
                    OutputStream outputStream = new FileOutputStream(FILE, true);
                    Writer writer = new OutputStreamWriter(outputStream, CHARSET);

                    writer.append(whatToWrite);
                    writer.append("\n");

                    writer.flush();
                    outputStream.flush();

                    writer.close();
                    outputStream.close();

                    finish();
                } catch (Exception e) {
                    Snackbar.make(this.getCurrentFocus(), "ERRORE IN FASE DI SCRITTURA!", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    public void cancellaElemento() {

        if (this.daModificare == true) {
            createTempFile("");
        }

        finish();
    }
    

    public void exit() {
        finish();
    }

    /*
    * Carica tutti gli elementi e li scrive (se il dato è da modificare)
    * */
    private void caricaElemento() {
        this.daModificare = getIntent().getBooleanExtra("daModificare", false);

        if (this.daModificare == true) {

            //i valori vengono passati come "extra"
            //vengono presi, resi leggibili e settati negli appositi input
            String codice = replaceText(getIntent().getStringExtra("codice"));
            String nome = replaceText(getIntent().getStringExtra("nome"));
            String telefono = replaceText(getIntent().getStringExtra("telefono"));
            String note = replaceText(getIntent().getStringExtra("note"));

            binding.codiceInput.setText(codice);
            binding.nomeInput.setText(nome);
            binding.telefonoInput.setText(telefono);
            binding.noteInput.setText(note);

            this.oldValue = codice + "|" + nome + "|" + telefono + "|" + note + "|";
        }

        //serve per indicare dove deve andare il file
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
            List<StorageVolume> storageVolumeList = storageManager.getStorageVolumes();
            StorageVolume storageVolume = storageVolumeList.get(1); // 1 is for external SD Card. 0 is internal storage

            FILE = new File(storageVolume.getDirectory().getAbsolutePath() + "/" + FILENAME);
        }
    }


    /**
     * Cambia tutti i
     * \n
     * con un
     * §
     **/
    private String replaceText(String stringaDaPulire){
        String stringaPulita = stringaDaPulire.replaceAll("\n", "§");

        return stringaPulita;
    }


    /**
     * Crea un file temporaneo dove salvare i dati
     * */
    private void createTempFile(String stringToReplace ){

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                File temp = File.createTempFile(TEMPFILE, ".txt", FILE.getParentFile());

                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE), CHARSET));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), CHARSET));

                for (String line; (line = reader.readLine()) != null;) {
                    line = line.replace(oldValue, stringToReplace);
                    writer.println(line);
                }

                reader.close();
                writer.close();

                FILE.delete();

                temp.renameTo(FILE);

                finish();
            }
        } catch (Exception e) {
            Snackbar.make(this.getCurrentFocus(), "LETTURA DEL FILE FALLITA!", Snackbar.LENGTH_LONG).show();
        }

    }

}