package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Info;

public class HomeActivity extends AppCompatActivity {

    private Info info;
    private BottomNavigationView bottomNavigationView;
    private TextView txt_info;
    private Button logout,facebook, instagram,share;
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    protected static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //getSupportActionBar().hide();

        iniciaNavigation();
        iniciarComponentes();

        recuperarInfo();

        logout.setOnClickListener(v -> {
            deslogar();
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String perfilInstagram = "galeria.costura.diferente";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + perfilInstagram));
                intent.setPackage("com.instagram.android");
                try{
                    startActivity(intent);
                }catch (android.content.ActivityNotFoundException ex){
                    // Instagram não está instalado, abra o perfil no navegador
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + perfilInstagram)));
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookProfile = "galeria.costura.diferente/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + facebookProfile));
                intent.setPackage("com.facebook.katana");
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Facebook não está instalado, abra o perfil no navegador
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + facebookProfile)));
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String linktree = "https://linktr.ee/galeria.costura.diferente";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, linktree);
                startActivity(Intent.createChooser(intent, "Compartilhar via"));
            }
        });
    }



    public void salvarInfo(View v) {
        String txt = txt_info.getText().toString();
        info = new Info();
        info.setInformacao(txt);
        info.salvarInfoBD();
        Toast.makeText(getApplicationContext(), "Informações salvas!", Toast.LENGTH_SHORT).show();
    }

    public void recuperarInfo(){
        DatabaseReference infoRef = firebaseRef.child("home_info");
        infoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Info info = snapshot.getValue(Info.class);
                    txt_info.setText(info.getInformacao());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deslogar(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name","");
        editor.apply();

        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
        }

    private void iniciaNavigation(){
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){

                switch(item.getItemId()){
                    case R.id.menu_home:
                        return true;
                    case R.id.menu_molde:
                        startActivity(new Intent(getApplicationContext(),MoldeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_favorito:
                        startActivity(new Intent(getApplicationContext(),FavoritoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_aviamento:
                        startActivity(new Intent(getApplicationContext(),AviamentoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_contato:
                        startActivity(new Intent(getApplicationContext(),ContatoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
            //return false;
            //}
        });
    }

    private void iniciarComponentes(){
        logout = findViewById(R.id.home_logout);
        facebook = findViewById(R.id.home_facebook);
        instagram = findViewById(R.id.home_instagram);
        share = findViewById(R.id.home_share);
        txt_info = findViewById(R.id.home_info);

    }
}