package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroContatoActivity;
import br.com.appgaleria.costura.diferente.adapter.ContatoAdapter;
import br.com.appgaleria.costura.diferente.model.Contato;

public class ContatoActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private ContatoAdapter contatoAdapter;
    private List<Contato> listaContatos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        getSupportActionBar().hide();

        iniciarComponentes();
        iniciaNavigation();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContatoActivity.this, CadastroContatoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void iniciaNavigation() {

        bottomNavigationView.setSelectedItemId(R.id.menu_contato);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               // if (item.getItemId() != R.id.menu_contato) {
                 //   finish();
                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_molde:
                            startActivity(new Intent(getApplicationContext(), MoldeActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_favorito:
                            startActivity(new Intent(getApplicationContext(), FavoritoActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_aviamento:
                            startActivity(new Intent(getApplicationContext(), AviamentoActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_contato:
                            return true;
                    }
                    return false;
                }
             //   return false;
           // }
        });
    }

    private void iniciarComponentes() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.contato_btn_add);
        recyclerView = findViewById(R.id.contato_recyclerView);
    }

    public void carregarListaContatos(){
        //Listar tarefas (static, sem BD)
        Contato con1 = new Contato();
        con1.setNome("con1");
        con1.setTelefone("(51)11111.1111");
        listaContatos.add(con1);
        Contato con2 = new Contato();
        con2.setNome("con2");
        con2.setTelefone("(51)22222.1111");
        listaContatos.add(con2);
        Contato con3 = new Contato();
        con3.setNome("con3");
        con3.setTelefone("(51)33333.1111");
        listaContatos.add(con3);

       /* Exibe a lista de tarefas*/

        //adapter
        contatoAdapter = new ContatoAdapter(listaContatos);

        //recryclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(contatoAdapter);
    }

    @Override
    protected void onStart() {
        carregarListaContatos();
        super.onStart();
    }
}