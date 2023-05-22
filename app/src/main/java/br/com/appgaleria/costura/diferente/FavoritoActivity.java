package br.com.appgaleria.costura.diferente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavoritoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito);

        getSupportActionBar().hide();

        iniciaNavigation();
    }

    private void iniciaNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_favorito);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_molde:
                        startActivity(new Intent(getApplicationContext(), MoldeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_favorito:
                        return true;
                    case R.id.menu_aviamento:
                        startActivity(new Intent(getApplicationContext(), AviamentoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_contato:
                        startActivity(new Intent(getApplicationContext(), ContatoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

}