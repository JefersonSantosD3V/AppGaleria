package br.com.appgaleria.costura.diferente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContatoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        getSupportActionBar().hide();

        iniciaNavigation();

        floatingActionButton = findViewById(R.id.contato_btn_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContatoActivity.this, CadastroContatoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void iniciaNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_contato);

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
                        startActivity(new Intent(getApplicationContext(), FavoritoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_aviamento:
                        startActivity(new Intent(getApplicationContext(), AviamentoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_contato:
                        return true;
                }
                return false;
            }
        });
    }

}