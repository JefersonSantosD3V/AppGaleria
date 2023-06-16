package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroAviamentoActivity;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroMoldeActivity;
import br.com.appgaleria.costura.diferente.adapter.MoldeAdapter;
import br.com.appgaleria.costura.diferente.databinding.ActivityConsultaMoldeBinding;
import br.com.appgaleria.costura.diferente.databinding.DialogMoldeBinding;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Favorito;
import br.com.appgaleria.costura.diferente.model.Molde;

public class ConsultaMoldeActivity extends AppCompatActivity implements MoldeAdapter.OnClickLister, MoldeAdapter.OnClickFavorito {

    private ActivityConsultaMoldeBinding binding;
    private ImageView img_btn_fechar;
    private List<Molde> moldeList = new ArrayList<>();
    private List<String> idsFavoritos = new ArrayList<>();
    private MoldeAdapter moldeAdapter;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultaMoldeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        confgRv();
        recuperaFavoritos();

        img_btn_fechar = findViewById(R.id.consultaMolde_btn_fechar);

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    ConsultaMoldeActivity.this, MoldeActivity.class);
            startActivity(intent);
            finish();
        });
    }
    private void recuperaFavoritos() {
        if (ConfigFirebase.getAutentifica()) {
            DatabaseReference favoritoRef = ConfigFirebase.getFirebase()
                    .child("favoritos")
                    .child(ConfigFirebase.getIdFirebase());
            favoritoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    idsFavoritos.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String idFavorito = ds.getValue(String.class);
                        idsFavoritos.add(idFavorito);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void confgRv(){
        binding.rvMoldes.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        binding.rvMoldes.setHasFixedSize(true);
        moldeAdapter = new MoldeAdapter(moldeList,getApplicationContext(),this,this,false,new ArrayList<>());
        binding.rvMoldes.setAdapter(moldeAdapter);
    }

    private void recuperaMoldes(){
        DatabaseReference moldeRef = ConfigFirebase.getFirebase()
                .child("moldes");
        moldeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moldeList.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    Molde molde = data.getValue(Molde.class);
                    moldeList.add(molde);
                }

                listEmpty();

                binding.progressBar.setVisibility(View.GONE);
                Collections.reverse(moldeList);
                moldeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            Intent intent = new Intent(getApplicationContext(), DetalheMoldeActivity.class);
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

    private void listEmpty() {
        if(moldeList.isEmpty()){
            binding.textInfo.setText("Nenhum molde cadastrado.");
        }else {
            binding.textInfo.setText("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaMoldes();
    }

    @Override
    public void onClick(Molde molde) {
        showDialog(molde);
    }

    @Override
    public void onClickFavorito(Molde molde) {
        if (!idsFavoritos.contains(molde.getId())) {
            idsFavoritos.add(molde.getId());
        } else {
            idsFavoritos.remove(molde.getId());
        }
        Favorito.salvar(idsFavoritos);
    }
}