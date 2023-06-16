package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Favorito {

    private List<String> idsProdutos;

    public static void salvar(List<String> idsProdutos) {
        DatabaseReference favoritoRef = ConfigFirebase.getFirebase()
                .child("favoritos")
                .child(ConfigFirebase.getIdFirebase());
        favoritoRef.setValue(idsProdutos);
    }

    public List<String> getIdsProdutos() {
        return idsProdutos;
    }

    public void setIdsProdutos(List<String> idsProdutos) {
        this.idsProdutos = idsProdutos;
    }
}
