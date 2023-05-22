package br.com.appgaleria.costura.diferente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CadastroAviamentoActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    private Button btn_cadastrar;
    private EditText txt_codigo, txt_nome, txt_descricao, txt_quantidade;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aviamento);

        getSupportActionBar().hide();

        iniciarComponetes();

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroAviamentoActivity.this, AviamentoActivity.class);
            startActivity(intent);
            finish();
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CadastroAviamentoActivity.this, "camera", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cadastrar(View v) {
        String codigo = txt_codigo.getText().toString();
        String nome = txt_nome.getText().toString();
        String descricao = txt_descricao.getText().toString();
        String quantidade = txt_quantidade.getText().toString();

        if (nome.equals("") || nome.equals("")) {
            Toast.makeText(CadastroAviamentoActivity.this, "Preencha os campos.", Toast.LENGTH_SHORT).show();
        } else if (verificaCodigo(codigo)) {
            Toast.makeText(CadastroAviamentoActivity.this, "Código já cadastrado.", Toast.LENGTH_SHORT).show();
            //criar condição se deseja continuar
        } else if (verificaNome(nome)) {
            Toast.makeText(CadastroAviamentoActivity.this, "Telefone já cadastrado.", Toast.LENGTH_SHORT).show();
            //criar condição se deseja continuar
        } else {
            Intent intent = new Intent(CadastroAviamentoActivity.this, AviamentoActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(CadastroAviamentoActivity.this, "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verificaCodigo(String email) {
        return false;
    }
    private boolean verificaNome(String telefone) {
        return false;
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.cadAvi_btn_fechar);
        btn_cadastrar = findViewById(R.id.cadAvi_btn_cadastrar);
        floatingActionButton = findViewById(R.id.cadAvi_btn_camera);
        txt_codigo = findViewById(R.id.cadAvi_edit_codigo);
        txt_nome = findViewById(R.id.cadAvi_edit_nome);
        txt_descricao = findViewById(R.id.cadAvi_edit_descricao);
        txt_quantidade = findViewById(R.id.cadAvi_edit_quantidade);
    }
}