package br.com.appgaleria.costura.diferente.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {

    private static FirebaseAuth autentificacao;
    private static DatabaseReference referenciaFirebase;
    private static StorageReference referenciaStorage;

    public static FirebaseAuth getAutentificacao(){
        if (autentificacao == null){
            autentificacao = FirebaseAuth.getInstance();
        }
        return autentificacao;
    }

    public static DatabaseReference getFirebase(){
        if( referenciaFirebase == null ){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static StorageReference getStorage(){
        if( referenciaStorage == null ){
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }

    public static String getIdFirebase(){
        return getAutentificacao().getUid();
    }

    public static boolean getAutentifica(){
        return getAutentificacao().getCurrentUser() != null;
    }
}
