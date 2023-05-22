package br.com.appgaleria.costura.diferente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btn_cadastrar;
    private TextView txt_recuperar;
    private Button btn_entrar;
    private EditText txt_email, txt_senha;
    private ProgressBar progress_bar;
    private CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //esconde actcionbar
        getSupportActionBar().hide();
        //tela cheia
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        iniciarComponentes();

        btn_cadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
            finish();
        });
        txt_recuperar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RecuperarActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void entrar(View v) {
        //String email = txt_email.getText().toString();
        //String senha = txt_senha.getText().toString();

        String email = "teste";
        String senha = "1234";

        if (email.equals("") || senha.equals("")) {
            Toast.makeText(LoginActivity.this, "Informe seu e-mail e senha.", Toast.LENGTH_SHORT).show();
        } else if (verificaEmail(email) == false) {
            Toast.makeText(LoginActivity.this, "E-mail não cadastrado.", Toast.LENGTH_SHORT).show();
        } else if (verificaLogin(email, senha)) {
            progress_bar.setVisibility(v.getVisibility());
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "Login/Senha incorreto(s).", Toast.LENGTH_SHORT).show();
        }
    }

    //verifica se o email tem cadastro
    private boolean verificaEmail(String email) {
        return true;
    }

    //valida login no banco de dados
    private boolean verificaLogin(String email, String senha) {
        return true;
    }

    private void checkbox() {
        if (checkbox.isChecked()) {

        }
    }

    private void iniciarComponentes() {
        btn_cadastrar = findViewById(R.id.log_btn_cadastrar);
        txt_recuperar = findViewById(R.id.log_txt_recuperar);
        btn_entrar = findViewById(R.id.log_btn_entrar);
        txt_email = findViewById(R.id.log_edit_email);
        txt_senha = findViewById(R.id.log_edit_senha);
        progress_bar = findViewById(R.id.log_progress_bar);
        checkbox = findViewById(R.id.log_check_box);
    }

}