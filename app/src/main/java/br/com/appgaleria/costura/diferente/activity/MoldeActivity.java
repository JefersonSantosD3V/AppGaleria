package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroMoldeActivity;
import br.com.appgaleria.costura.diferente.databinding.ActivityMoldeBinding;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Molde;

public class MoldeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapterItens;
    private String[] listTop,listBottom,listHibrido,listOutros;
    private View viewControleGenero,viewControleCategoria;
    private ToggleButton tbPP,tbP,tbM,tbG,tbGG,tbEG,tbSM;
    private FloatingActionButton floatingActionButton;
    private ActivityMoldeBinding binding;
    private String tipo,categoria,genero;
    private List<String> listTamanhosSelecionados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoldeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getSupportActionBar().hide();

        iniciaNavigation();
        iniciarComponentes();
        criarListasDropdown();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoldeActivity.this, CadastroMoldeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void iniciaNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_molde);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              //  if (item.getItemId() != R.id.menu_molde) {
                //    finish();

                    switch (item.getItemId()) {
                        case R.id.menu_home:
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.menu_molde:
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
                            startActivity(new Intent(getApplicationContext(), ContatoActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
        });
    }

    private void iniciarComponentes() {
        autoCompleteTextView = findViewById(R.id.molde_autocomplete_dropdown);
        floatingActionButton = findViewById(R.id.molde_btn_add);
        tbPP = findViewById(R.id.molde_tb_pp);
        tbP = findViewById(R.id.molde_tb_p);
        tbM = findViewById(R.id.molde_tb_m);
        tbG = findViewById(R.id.molde_tb_g);
        tbGG = findViewById(R.id.molde_tb_gg);
        tbEG = findViewById(R.id.molde_tb_eg);
        tbSM = findViewById(R.id.molde_tb_sm);
        tipo = "";
        categoria = "";
        genero = "";
    }

    private void criarListasDropdown(){
        listTop = getResources().getStringArray(R.array.categoria_top);
        listBottom = getResources().getStringArray(R.array.categoria_bottom);
        listHibrido = getResources().getStringArray(R.array.categoria_hibrido);
        listOutros = getResources().getStringArray(R.array.categoria_outros);
    }

    private void selecaoDropdown(String[] list){
        autoCompleteTextView.setText("");
        adapterItens = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_item,list);
        autoCompleteTextView.setAdapter(adapterItens);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoria = autoCompleteTextView.getText().toString();
            }
        });
    }

    public void opcaoCategoria(View view) {
        boolean checked =((RadioButton) view).isChecked();
        if (viewControleCategoria != null) {
            ((RadioButton) viewControleCategoria).setTypeface(null, Typeface.NORMAL);
        }
        switch (view.getId()){
            case R.id.molde_rb_top:
                if(checked){
                    ((RadioButton) view).setTypeface(null, Typeface.BOLD);
                    selecaoDropdown(listTop);
                }
                break;
            case R.id.molde_rb_bottom:
                if(checked){
                    ((RadioButton) view).setTypeface(null, Typeface.BOLD);
                    selecaoDropdown(listBottom);
                }
                break;
            case R.id.molde_rb_hibrido:
                if(checked){
                    ((RadioButton) view).setTypeface(null, Typeface.BOLD);
                    selecaoDropdown(listHibrido);
                }
                break;
            case R.id.molde_rb_outros:
                if(checked){
                    ((RadioButton) view).setTypeface(null, Typeface.BOLD);
                    selecaoDropdown(listOutros);
                }
                break;
        }
        viewControleCategoria = view;
        tipo = ((RadioButton) view).getText().toString();
    }

    public void opcaoGenero(View view) {
        boolean checked =((RadioButton) view).isChecked();
        if (viewControleGenero != null) {
            ((RadioButton) viewControleGenero).setTypeface(null, Typeface.NORMAL);
        }
        switch (view.getId()){
            case R.id.molde_rb_masc:
            case R.id.molde_rb_fem:
            case R.id.molde_rb_age:
                if(checked){
                    ((RadioButton) view).setTypeface(null, Typeface.BOLD);
                }
                break;
        }
        viewControleGenero = view;
        genero = ((RadioButton) view).getText().toString();
    }

    public void opcaoTamanho(View view) {
        boolean checked = ((ToggleButton) view).isChecked();

        switch (view.getId()){
        case R.id.molde_tb_pp:
        case R.id.molde_tb_p:
        case R.id.molde_tb_m:
        case R.id.molde_tb_g:
        case R.id.molde_tb_gg:
        case R.id.molde_tb_eg:
        case R.id.molde_tb_sm:
            if (checked) {
                listTamanhosSelecionados.add(((ToggleButton) view).getText().toString());
            }else{
                listTamanhosSelecionados.remove(((ToggleButton) view).getText().toString());
            }
            break;
        }
    }

    public void filtrarMoldes(View view) {

        if(!tipo.isEmpty() && !categoria.isEmpty() && !genero.isEmpty()) {

            Intent intent = new Intent(MoldeActivity.this, ConsultaMoldeActivity.class);

            intent.putExtra("tipoSelecionado", tipo);
            intent.putExtra("categoriaSelecionada", categoria);
            intent.putExtra("generoSelecionado", genero);
            intent.putStringArrayListExtra("tamanhosSelecionads", (ArrayList<String>) listTamanhosSelecionados);

            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        }
    }
}
