package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Aviamento{

    private String id;
    private String nome, descricao;
    private Double quantidade;
    private String urlImagem;;


    public Aviamento() {
        DatabaseReference reference = ConfigFirebase.getFirebase();
        this.setId(reference.push().getKey());
    }
    public void salvarAviamento() {
        DatabaseReference reference = ConfigFirebase.getFirebase()
                .child("aviamentos")
                .child(this.getId());
        reference.setValue(this);
    }

    public void deletaAviamento(){
        DatabaseReference reference = ConfigFirebase.getFirebase()
                .child("aviamentos")
                .child(this.getId());
        reference.removeValue();

        StorageReference storageReference = ConfigFirebase.getStorage()
                .child("imagens")
                .child("aviamentos")
                .child(this.getId())
                .child("imagem.jpeg");
        storageReference.delete();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

}
