package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Usuario {

    private String id, email, senha;

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void salvar(){
        DatabaseReference usuarioRef = ConfigFirebase.getFirebase()
                .child("usuarios")
                .child(this.getId());
        usuarioRef.setValue(this);
    }
}
