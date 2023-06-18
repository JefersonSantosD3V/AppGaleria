package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.adapter.MoldeAdapter;
import br.com.appgaleria.costura.diferente.databinding.ActivityFavoritoBinding;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Favorito;
import br.com.appgaleria.costura.diferente.model.Molde;

public class FavoritoActivity extends AppCompatActivity implements MoldeAdapter.OnClickLister, MoldeAdapter.OnClickFavorito {

    private BottomNavigationView bottomNavigationView;
    private ActivityFavoritoBinding binding;
    private final List<Molde> moldeList = new ArrayList<>();
    private final List<String> idsFavoritos = new ArrayList<>();
    private MoldeAdapter moldeAdapter;
    private DatabaseReference favoritoRef;
    private ValueEventListener eventListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getSupportActionBar().hide();

        iniciaNavigation();
        confgRv();
        recuperaFavoritos();

    }

    private void confgRv(){
        binding.rvFavoritos.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        binding.rvFavoritos.setHasFixedSize(true);
        moldeAdapter = new MoldeAdapter(moldeList,getApplicationContext(),this,this,true,idsFavoritos);
        binding.rvFavoritos.setAdapter(moldeAdapter);
    }

    private void recuperaFavoritos() {
        if (ConfigFirebase.getAutentifica()) {
           favoritoRef = ConfigFirebase.getFirebase()
                    .child("favoritos")
                    .child(ConfigFirebase.getIdFirebase());
            eventListener = favoritoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    idsFavoritos.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idFavorito = ds.getValue(String.class);
                        idsFavoritos.add(idFavorito);
                    }
                    Collections.reverse(idsFavoritos);
                    listEmpty();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void recuperaMoldes(){
        moldeList.clear();
       for(int i = 0; i < idsFavoritos.size(); i++){
           DatabaseReference moldeRef = ConfigFirebase.getFirebase()
                   .child("moldes")
                   .child(idsFavoritos.get(i));
           moldeRef.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   Molde molde = snapshot.getValue(Molde.class);
                   moldeList.add(molde);
                   if(moldeList.size() == idsFavoritos.size()){
                       binding.favoritoProgressBar.setVisibility(View.GONE);
                       moldeAdapter.notifyDataSetChanged();
                   }

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
       }
    }

    private void listEmpty() {
        if(idsFavoritos.isEmpty()){
            binding.favoritoProgressBar.setVisibility(View.GONE);
            binding.favoritoTextInfo.setText("Nenhum molde marcado como favorito.");
        }else {
            binding.favoritoTextInfo.setText("");

            recuperaMoldes();
        }
    }

    private void iniciaNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_favorito);

        //return false;
// }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                        return true;
                    case R.id.menu_aviamento:
                        startActivity(new Intent(getApplicationContext(), AviamentoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.menu_contato:
                        startActivity(new Intent(getApplicationContext(), ContatoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoritoRef.removeEventListener(eventListener);
        binding = null;
    }

    @Override
    public void onClick(Molde molde) {
        Intent intent = new Intent(getApplicationContext(), MoldeDetalheActivity.class);
        intent.putExtra("moldeSelecionado", molde);
        startActivity(intent);
    }

    @Override
    public void onClickFavorito(Molde molde) {
        if (!idsFavoritos.contains(molde.getId())) {
            idsFavoritos.add(molde.getId());
            moldeList.add(molde);
        } else {
            idsFavoritos.remove(molde.getId());
            moldeList.remove(molde);
        }
        Favorito.salvar(idsFavoritos);
        moldeAdapter.notifyDataSetChanged();
    }
}