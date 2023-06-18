package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Contato implements Serializable{

    private String id;
    private String nome ;
    private String email;
    private String telefone;
    private String chave;

    public void salvarContato(){
        DatabaseReference reference = ConfigFirebase.getFirebase()
                .child("contatos")
                .child(this.getId());
        reference.setValue(this);
    }

    public void deletaContao(){
        DatabaseReference reference = ConfigFirebase.getFirebase()
                .child("contatos")
                .child(this.getId());
        reference.removeValue();
    }

    public Contato() {
        DatabaseReference reference = ConfigFirebase.getFirebase();
        this.setId(reference.push().getKey());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
