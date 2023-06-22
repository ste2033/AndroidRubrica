package com.example.rubricaapp;

public class Rubrica {
    private String codice = "";
    private String nome = "";
    private String telefono = "";
    private String note = "";


    public Rubrica(String codice, String nome, String telefono, String note) {
        this.codice = codice;
        this.nome = nome;
        this.telefono = telefono;
        this.note = note;
    }


    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
