package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Molde {

    private String nome;
    private String descrição;
    private String tipo;
    private String categoria;
    private String genero;
    private String tamanho;
    private Byte[] foto;

    private String medida;

    public void salvarMolde(){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference moldeRef = firebaseRef.child("moldes");

        moldeRef.setValue(this);
    }

    public Molde() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrição() {
        return descrição;
    }

    public void setDescrição(String descrição) {
        this.descrição = descrição;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Byte[] getFoto() {
        return foto;
    }

    public void setFoto(Byte[] foto) {
        this.foto = foto;
    }
/*
    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }
*/
}
