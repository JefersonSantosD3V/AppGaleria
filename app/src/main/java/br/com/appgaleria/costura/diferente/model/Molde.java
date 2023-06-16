package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Molde implements Serializable {

    private String id;
    private String nome;
    private String descricao;
    private String tipo;
    private String categoria;
    private String genero;
    private List<String> tamanhos = new ArrayList<>();
    private List<ImagemUpload> urlsImagens = new ArrayList<>();


    public void salvarMolde(boolean novoProduto){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        firebaseRef.child("moldes")
                .child(this.getId())
                .setValue(this);
    }

    public void removerMolde() {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase()
                .child("moldes")
                .child(this.getId());
        firebaseRef.removeValue();

        for (int i = 0; i < getUrlsImagens().size(); i++) {
            StorageReference storageReference = ConfigFirebase.getStorage()
                    .child("imagens")
                    .child("moldes")
                    .child(this.getId())
                    .child("imagem" + i + ".jpeg");
            storageReference.delete();
        }
    }

    public Molde() {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        this.setId(firebaseRef.push().getKey());
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

    public List<String> getTamanhos() {
        return tamanhos;
    }

    public void setTamanhos(List<String> tamanhos) {
        this.tamanhos = tamanhos;
    }

    public List<ImagemUpload> getUrlsImagens() {
        return urlsImagens;
    }

    public void setUrlsImagens(List<ImagemUpload> urlsImagens) {
        this.urlsImagens = urlsImagens;
    }
}
