package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroMoldeActivity;
import br.com.appgaleria.costura.diferente.adapter.MoldeAdapter;
import br.com.appgaleria.costura.diferente.databinding.ActivityFavoritoBinding;
import br.com.appgaleria.costura.diferente.databinding.DialogMoldeBinding;
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
    private AlertDialog dialog;

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

    private void showDialog(Molde molde) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);

        DialogMoldeBinding dialogBinding = DialogMoldeBinding
                .inflate(LayoutInflater.from(this));

        for (int i = 0; i < molde.getUrlsImagens().size(); i++) {
            if (molde.getUrlsImagens().get(i).getIndex() == 0) {
                Glide.with(getApplicationContext())
                        .load(molde.getUrlsImagens().get(i).getCaminhoImagem())
                        .into(dialogBinding.dialogMoldeImagem);
            }
        }

        dialogBinding.dialogMoldeBtnAcessar.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MoldeDetalheActivity.class);
            intent.putExtra("moldeSelecionado", molde);
            startActivity(intent);
            dialog.dismiss();
        });

        dialogBinding.dialogMoldeBtnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CadastroMoldeActivity.class);
            intent.putExtra("moldeSelecionado", molde);
            startActivity(intent);
            dialog.dismiss();
        });

        dialogBinding.dialogMoldeBtnRemover.setOnClickListener(v -> {
            molde.removerMolde();
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Molde removido com sucesso!", Toast.LENGTH_SHORT).show();

            listEmpty();
        });

        dialogBinding.dialogMoldeTxtNome.setText(molde.getNome());

        dialogBinding.dialogMoldeBtnFechar.setOnClickListener(v -> dialog.dismiss());

        builder.setView(dialogBinding.getRoot());

        dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoritoRef.removeEventListener(eventListener);
        binding = null;
    }

    @Override
    public void onClick(Molde molde) {
        showDialog(molde);
       // Intent intent = new Intent(getApplicationContext(), MoldeDetalheActivity.class);
       // intent.putExtra("moldeSelecionado", molde);
       // startActivity(intent);
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