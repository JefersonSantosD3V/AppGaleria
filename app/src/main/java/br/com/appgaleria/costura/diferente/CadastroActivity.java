package br.com.appgaleria.costura.diferente;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    private Button btn_cadastrar;
    private EditText txt_nome, txt_email, txt_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        getSupportActionBar().hide();

        iniciarComponetes();

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void cadastrar(View v) {
        String nome = txt_nome.getText().toString();
        String email = txt_email.getText().toString();
        String senha = txt_senha.getText().toString();

        if (nome.equals("") || email.equals("") || senha.equals("")) {
            Toast.makeText(CadastroActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        } else if (verificaEmail(email)) {
            Toast.makeText(CadastroActivity.this, "E-mail já cadastrado.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(CadastroActivity.this, "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    //verifica se o email já tem cadastro
    private boolean verificaEmail(String email) {
        return false;
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.cad_btn_fechar);
        btn_cadastrar = findViewById(R.id.cad_btn_cadastrar);
        txt_nome = findViewById(R.id.cad_edit_nome);
        txt_email = findViewById(R.id.cad_edit_email);
        txt_senha = findViewById(R.id.cad_edit_senha);
    }
}