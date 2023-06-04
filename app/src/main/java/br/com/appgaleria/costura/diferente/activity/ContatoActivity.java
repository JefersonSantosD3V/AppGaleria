package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroContatoActivity;
import br.com.appgaleria.costura.diferente.adapter.ContatoAdapter;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Contato;

public class ContatoActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private ContatoAdapter contatoAdapter;
    private Contato contato;
    private FirebaseRecyclerOptions<Contato> options;
    //private ArrayList<Contato> listaContatos;
    //private DatabaseReference contatosRef;
    //private ValueEventListener valueEventListenerContatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        getSupportActionBar().hide();

        iniciarComponentes();
        iniciaNavigation();

        swipeDeletarContato();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<Contato>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("contatos"),Contato.class)
                        .build();

        contatoAdapter = new ContatoAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(contatoAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContatoActivity.this, CadastroContatoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void swipeDeletarContato(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deletarContato(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);
    }

    public void deletarContato(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir");
        alertDialog .setMessage("Excluir contato?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", (dialog, which) -> {
            int position = viewHolder.getAbsoluteAdapterPosition();
            contato = options.getSnapshots().get(position);
        });
        alertDialog.setNegativeButton("Cancelar", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            contatoAdapter.notifyDataSetChanged();
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
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
        recyclerView = (RecyclerView) findViewById (R.id.contato_recyclerView);

        //contatosRef = ConfigFirebase.getFirebase();
        //listaContatos = new ArrayList<>();
    }


    @Override
    protected void onStart() {
        super.onStart();
        contatoAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        contatoAdapter.stopListening();
    }
}