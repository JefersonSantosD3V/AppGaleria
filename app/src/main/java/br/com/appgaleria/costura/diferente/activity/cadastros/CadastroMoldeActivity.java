package br.com.appgaleria.costura.diferente.activity.cadastros;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.MoldeActivity;
import br.com.appgaleria.costura.diferente.helper.Permissao;

public class CadastroMoldeActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private ImageView img_btn_fechar;
    private Spinner spinner_tipo,spinner_categoria,spinner_genero,spinner_tamanho;
    private ArrayAdapter<String> adapterItens;
    private String[] listTipo,listGenero,listTamanho,listCategoriaTop,listCategoriaBottom,listCategoriaHibrido,listCategoriaOutros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_molde);

       // getSupportActionBar().hide();

        iniciarComponentes();
        criarListasDropdown();

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroMoldeActivity.this, MoldeActivity.class);
            startActivity(intent);
            finish();
        });

        spinner_tipo.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listTipo));
        spinner_genero.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listGenero));
        spinner_tamanho.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listTamanho));

        spinner_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selecao = position;
                if(selecao == 0) {
                    adapterItens = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listCategoriaTop);
                }
                else if(selecao == 1){
                    adapterItens = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listCategoriaBottom);
                }
                else if(selecao == 2){
                    adapterItens = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listCategoriaHibrido);
                }
                else if(selecao == 3){
                    adapterItens = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listCategoriaOutros);
                }

                adapterItens.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterItens.notifyDataSetChanged();
                spinner_categoria.setAdapter(adapterItens);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_tamanho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CadastroMoldeActivity.this, "camera", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void iniciarComponentes() {
        floatingActionButton = findViewById(R.id.cadMol_btn_camera);
        img_btn_fechar = findViewById(R.id.cadMol_btn_fechar);
        spinner_tipo = findViewById(R.id.cadMol_spinner_tipo);
        spinner_categoria = findViewById(R.id.cadMol_spinner_categoria);
        spinner_genero = findViewById(R.id.cadMol_spinner_genero);
        spinner_tamanho = findViewById(R.id.cadMol_spinner_tamanho);
    }

    private void criarListasDropdown(){
        listTipo = getResources().getStringArray(R.array.spinner_tipo);
        listGenero = getResources().getStringArray(R.array.spinner_genero);
        listTamanho = getResources().getStringArray(R.array.spinner_tamanho);
        listCategoriaTop = getResources().getStringArray(R.array.categoria_top);
        listCategoriaBottom  = getResources().getStringArray(R.array.categoria_bottom);
        listCategoriaHibrido = getResources().getStringArray(R.array.categoria_hibrido);
        listCategoriaOutros = getResources().getStringArray(R.array.categoria_outros);
    }
}