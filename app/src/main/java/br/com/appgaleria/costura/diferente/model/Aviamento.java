package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Aviamento {
    private String nome, descricao;
    private Double quantidade;
    private Byte[] foto;
    private String key;

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
    public Aviamento(String nome, String descricao, Double quantidade, Byte[] foto) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.foto = foto;
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
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
