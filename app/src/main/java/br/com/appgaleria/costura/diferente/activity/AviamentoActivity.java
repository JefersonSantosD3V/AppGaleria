package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.RecyclerItemClickListener;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroAviamentoActivity;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroMoldeActivity;
import br.com.appgaleria.costura.diferente.adapter.AviamentoAdapter;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.helper.Permissao;
import br.com.appgaleria.costura.diferente.model.Aviamento;
import br.com.appgaleria.costura.diferente.model.Contato;

public class AviamentoActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference firebaseRef  = ConfigFirebase.getFirebase();
    private ValueEventListener valueEventListenerAviamentos;
    private RecyclerView recyclerView;
    private AviamentoAdapter aviamentoAdapter;
    private List<Aviamento> listaAviamentos = new ArrayList<>();
    private Aviamento aviamento;
    private DatabaseReference aviamentosRef;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviamento);

       // getSupportActionBar().hide();

        iniciarComponentes();
        iniciaNavigation();
        swipeAviamento();

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarListaAviamentos(newText);
                return true;
            }
        });

        aviamentoAdapter = new AviamentoAdapter(listaAviamentos,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter( aviamentoAdapter );

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AviamentoActivity.this, CadastroAviamentoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void filtrarListaAviamentos(String text) {
        List<Aviamento> filtro = new ArrayList<>();
        for(Aviamento aviamento : listaAviamentos){
            if(aviamento.getNome().toLowerCase().contains(text.toLowerCase())){
                filtro.add(aviamento);
            }
        }
        if(filtro.isEmpty()){
            //Toast.makeText(getApplicationContext(), "Não encontrado!", Toast.LENGTH_SHORT).show();
        }
        else{
            aviamentoAdapter.setFiltroList(filtro);
        }
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
        searchView = findViewById(R.id.searchView_aviamentos);
    }

    public void recuperarAviamentos(){
        aviamentosRef = firebaseRef.child("aviamentos");

        valueEventListenerAviamentos = aviamentosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaAviamentos.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Aviamento aviamento = dados.getValue( Aviamento.class );
                    //aviamento.setKey( dados.getKey() );
                    listaAviamentos.add( aviamento );
                }

                Collections.sort(listaAviamentos, new Comparator<Aviamento>() {
                    @Override
                    public int compare(Aviamento aviamento1, Aviamento aviamento2) {
                        return aviamento1.getNome().compareToIgnoreCase(aviamento2.getNome());
                    }
                });

                aviamentoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void swipeAviamento(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                excluirAviamento( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView( recyclerView );

    }

    public void excluirAviamento(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir");
        alertDialog.setMessage("Excluir aviamento?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", (dialog, which) -> {
            int position = viewHolder.getAbsoluteAdapterPosition();
            aviamento = listaAviamentos.get( position );

            aviamento.deletaAviamento();

            aviamentoAdapter.notifyItemRemoved( position );
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AviamentoActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                aviamentoAdapter.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperarAviamentos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        aviamentosRef.removeEventListener(valueEventListenerAviamentos);
    }

}