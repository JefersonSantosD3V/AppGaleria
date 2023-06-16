package br.com.appgaleria.costura.diferente.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.com.appgaleria.costura.diferente.databinding.ActivityConsultaMoldeBinding;
import br.com.appgaleria.costura.diferente.databinding.ActivityDetalheMoldeBinding;

public class DetalheMoldeActivity extends AppCompatActivity {

    ActivityDetalheMoldeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalheMoldeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}