package br.com.appgaleria.costura.diferente.model;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Contato {

    private String nome ;
    private String email;
    private String telefone;
    private String chave;
    //private String instagram;
   // private String facebook;

    public void salvarContato(){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        firebaseRef.child("contatos")
                .push()
                .setValue(this);
    }

    public Contato() {
    }

    public Contato(String nome, String email, String telefone,String chave) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.chave = chave;
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

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }
/*
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

 */
}
