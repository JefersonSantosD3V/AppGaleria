package br.com.appgaleria.costura.diferente.model;

import com.google.firebase.database.DatabaseReference;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class Info {

    private String informacao;

    public Info() {
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public void salvarInfoBD(){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        firebaseRef.child("home_info")
                //.push()
                .setValue(this);

        //DatabaseReference infoRef = firebaseRef.child("home_info").child("informacao");
        //infoRef.setValue(this);
    }

    public void recuperarInfo(){
        DatabaseReference databaseReference = ConfigFirebase.getFirebase();
        DatabaseReference descricao = databaseReference.child("home_info").child("descricao");
        //setInformacao(descricao.getValue());
    }
}
