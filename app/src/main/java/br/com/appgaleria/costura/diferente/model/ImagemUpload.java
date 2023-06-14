package br.com.appgaleria.costura.diferente.model;


import java.io.Serializable;

public class ImagemUpload implements Serializable {

    private String caminhoImagem;

    public ImagemUpload(){
    }

    public ImagemUpload(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }
}
