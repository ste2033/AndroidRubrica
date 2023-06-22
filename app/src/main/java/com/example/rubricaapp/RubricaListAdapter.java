package com.example.rubricaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RubricaListAdapter extends ArrayAdapter<Rubrica> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView codice;
        TextView nome;
        TextView telefono;
        TextView note;
    }

    public RubricaListAdapter(Context context, int resource, ArrayList<Rubrica> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String codice = getItem(position).getCodice();
        String nome = getItem(position).getNome();
        String telefono = getItem(position).getTelefono();
        String note = getItem(position).getNote();


        //Create the person object with the information
        Rubrica rubrica = new Rubrica(codice, nome,telefono,note);

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();

            holder.codice = convertView.findViewById(R.id.listCodice);
            holder.nome = convertView.findViewById(R.id.listNome);
            holder.telefono = convertView.findViewById(R.id.listTelefono);
            holder.note = convertView.findViewById(R.id.listNote);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        holder.codice.setText(codice);
        holder.nome.setText(nome);
        holder.telefono.setText(telefono);
        holder.note.setText(note);

        return  convertView;
    }
}
