package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.adapter.SliderAdapter;
import br.com.appgaleria.costura.diferente.databinding.ActivityMoldeDetalheBinding;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Favorito;
import br.com.appgaleria.costura.diferente.model.Molde;

public class MoldeDetalheActivity extends AppCompatActivity {

    private ActivityMoldeDetalheBinding binding;
    private Molde molde;
    private ImageView img_btn_fechar,imgSlider;
    private final List<String> idsFavoritos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoldeDetalheBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configClicks();
        getExtra();
        iniciarComponetes();
        recuperaFavoritos();

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MoldeDetalheActivity.this, ConsultaMoldeActivity.class);
            startActivity(intent);
            finish();
        });

        //binding.moldeDetalheSliderView.setSliderTransformAnimation();
    }

    private void getExtra(){
        molde = (Molde) getIntent().getSerializableExtra("moldeSelecionado");
        configDados();
    }
    public void configClicks() {
        binding.moldeDetalheLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (ConfigFirebase.getAutentifica()) {
                    idsFavoritos.add(molde.getId());
                    Favorito.salvar(idsFavoritos);
                } else {
                    Toast.makeText(getBaseContext(), "Você não está autenticado no app.", Toast.LENGTH_SHORT).show();
                    binding.moldeDetalheLikeButton.setLiked(false);
                }
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                idsFavoritos.remove(molde.getId());
                Favorito.salvar(idsFavoritos);
            }
        });

    }

    private void configDados(){

        binding.moldeDetalheSliderView.setSliderAdapter(new SliderAdapter(molde.getUrlsImagens()));
        binding.moldeDetalheSliderView.stopAutoCycle();
        //binding.moldeDetalheSliderView.startAutoCycle();
        //binding.moldeDetalheSliderView.setScrollTimeInSec(4);
       // binding.moldeDetalheSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
       // binding.moldeDetalheSliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        binding.moldeDetalheTextNome.setText(molde.getNome());
        binding.moldeDetalheTextDescricao.setText(molde.getDescricao());
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
                    binding.moldeDetalheLikeButton.setLiked(idsFavoritos.contains(molde.getId()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.moldeDetalhe_btn_fechar);
    }
}