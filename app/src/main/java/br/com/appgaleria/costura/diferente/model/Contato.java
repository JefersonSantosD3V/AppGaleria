package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Contato implements Serializable {

    private String nome ;
    private String email;
    private String telefone;
    private String instagram;
    private String facebook;

    public void salvarContato(){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference contatoRef = firebaseRef.child("contatos");
        contatoRef.setValue(this);
    }

    public Contato() {
    }

    public String getNome() { return nome; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
