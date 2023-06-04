package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Aviamento implements Serializable {
    private String nome, descricao;
    private Double quantidade;
    private Byte[] foto;

    public void salvarAviamento(){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        firebaseRef.child("aviamentos")
                .push()
                .setValue(this);

        //DatabaseReference aviamentoRef = firebaseRef.child("aviamentos");
        //aviamentoRef.setValue(this);
    }

    public Aviamento() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
