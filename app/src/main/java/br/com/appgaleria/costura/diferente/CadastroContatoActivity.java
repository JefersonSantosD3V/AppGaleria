package br.com.appgaleria.costura.diferente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CadastroContatoActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    private Button btn_cadastrar;
    private EditText txt_nome, txt_email, txt_telefone,txt_instagram,txt_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contato);

        getSupportActionBar().hide();

        iniciarComponetes();

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroContatoActivity.this, ContatoActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void cadastrar(View v) {
        String nome = txt_nome.getText().toString();
        String email = txt_email.getText().toString();
        String telefone = txt_telefone.getText().toString();

        if (nome.equals("") && (email.equals("") || telefone.equals(""))) {
            Toast.makeText(CadastroContatoActivity.this, "Preencha nome e e-mail ou telefone.", Toast.LENGTH_SHORT).show();
        } else if (verificaEmail(email)) {
            Toast.makeText(CadastroContatoActivity.this, "E-mail já cadastrado.", Toast.LENGTH_SHORT).show();
            //criar condição se deseja continuar
        } else if (verificaTelefone(telefone)) {
            Toast.makeText(CadastroContatoActivity.this, "Telefone já cadastrado.", Toast.LENGTH_SHORT).show();
            //criar condição se deseja continuar
        } else {
            Intent intent = new Intent(CadastroContatoActivity.this, CadastroActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(CadastroContatoActivity.this, "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verificaEmail(String email) {
        return false;
    }
    private boolean verificaTelefone(String telefone) {
        return false;
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.cadCon_btn_fechar);
        btn_cadastrar = findViewById(R.id.cadCon_btn_cadastrar);
        txt_nome = findViewById(R.id.cadCon_edit_nome);
        txt_email = findViewById(R.id.cadCon_edit_email);
        txt_telefone = findViewById(R.id.cadCon_edit_telefone);
        txt_facebook = findViewById(R.id.cadCon_edit_facebook);
        txt_instagram = findViewById(R.id.cadCon_edit_instagram);
    }
}