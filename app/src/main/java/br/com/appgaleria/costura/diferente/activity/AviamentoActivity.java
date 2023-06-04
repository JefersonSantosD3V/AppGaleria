package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.RecyclerItemClickListener;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroAviamentoActivity;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroMoldeActivity;
import br.com.appgaleria.costura.diferente.adapter.AviamentoAdapter;
import br.com.appgaleria.costura.diferente.helper.Permissao;
import br.com.appgaleria.costura.diferente.model.Aviamento;

public class AviamentoActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private AviamentoAdapter aviamentoAdapter;
    private List<Aviamento> listaAviamentos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviamento);

        getSupportActionBar().hide();

        iniciarComponentes();
        iniciaNavigation();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AviamentoActivity.this, CadastroAviamentoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void iniciaNavigation() {

        bottomNavigationView.setSelectedItemId(R.id.menu_aviamento);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

          //  if(item.getItemId() != R.id.menu_aviamento) {
            //    finish();
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
                        return true;
                    case R.id.menu_contato:
                        startActivity(new Intent(getApplicationContext(), ContatoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
           // }
           // return false;
        });
    }
    private void iniciarComponentes() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.aviamento_btn_add);
        recyclerView = findViewById(R.id.aviamento_recyclerView);
    }

    public void listaAviamentos(){
        //Listar tarefas (static, sem BD)
        Aviamento avi1 = new Aviamento();

        avi1.setNome("avi1");
        listaAviamentos.add(avi1);
        Aviamento avi2 = new Aviamento();

        avi2.setNome("avi2");
        listaAviamentos.add(avi2);
        Aviamento avi3 = new Aviamento();

        avi3.setNome("avi3");
        listaAviamentos.add(avi3);

        /*Exibe a lista de tarefas*/
        //adapter
        aviamentoAdapter = new AviamentoAdapter(listaAviamentos);
        //recryclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(aviamentoAdapter);

        //evento de click
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Aviamento aviamento = listaAviamentos.get(position);
                                Toast.makeText(getApplicationContext(), "Item pressionado"+aviamento.getNome(), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onLongItemClick(View view, int position) {
                                Toast.makeText(getApplicationContext(), "click longo", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    @Override
    protected void onStart() {
        listaAviamentos();
        super.onStart();
    }
}