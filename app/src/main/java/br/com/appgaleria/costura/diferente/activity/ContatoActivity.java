package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
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
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroContatoActivity;
import br.com.appgaleria.costura.diferente.adapter.ContatoAdapter;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Contato;



public class ContatoActivity extends AppCompatActivity implements ContatoAdapter.OnClickListener {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference firebaseRef  = ConfigFirebase.getFirebase();
    private ValueEventListener valueEventListenerContatos;
    private RecyclerView recyclerView;
    private ContatoAdapter contatoAdapter;
    private List<Contato> listaContatos = new ArrayList<>();
    private Contato contato;
    private DatabaseReference contatosRef;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        //getSupportActionBar().hide();

        iniciarComponentes();
        iniciaNavigation();
        swipeContato();

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarListaContatos(newText);
                return true;
            }
        });

        contatoAdapter = new ContatoAdapter(listaContatos,getApplicationContext(),this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter( contatoAdapter );

//        recuperarContatos();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContatoActivity.this, CadastroContatoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void filtrarListaContatos(String text) {
        List<Contato> filtro = new ArrayList<>();
        for(Contato contato : listaContatos){
            if(contato.getNome().toLowerCase().contains(text.toLowerCase())){
                filtro.add(contato);
            }
        }
        if(filtro.isEmpty()){
            if (!text.isEmpty()) {
                //Toast.makeText(getApplicationContext(), "Não encontrado!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            contatoAdapter.setFiltroList(filtro);
        }
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
        searchView = findViewById(R.id.searchView_contatos);
    }

    public void recuperarContatos(){
        contatosRef = firebaseRef.child("contatos");

        valueEventListenerContatos = contatosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaContatos.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    Contato contato = dados.getValue( Contato.class );
                    listaContatos.add( contato );

                }

                Collections.sort(listaContatos, new Comparator<Contato>() {
                    @Override
                    public int compare(Contato contact1, Contato contact2) {
                        return contact1.getNome().compareToIgnoreCase(contact2.getNome());
                    }
                });

                contatoAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void swipeContato(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                excluirContato( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView( recyclerView );

    }

    public void excluirContato(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir");
        alertDialog.setMessage("Excluir contato?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", (dialog, which) -> {
            int position = viewHolder.getAbsoluteAdapterPosition();
            contato = listaContatos.get( position );

            contato.deletaContao();

            contatoAdapter.notifyItemRemoved( position );
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ContatoActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                contatoAdapter.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        contatosRef.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public void OnClick(Contato contato) {
        Intent intent = new Intent(getApplicationContext(), CadastroContatoActivity.class);
        intent.putExtra("contatoSelecionado",contato);
        startActivity(intent);
    }
}