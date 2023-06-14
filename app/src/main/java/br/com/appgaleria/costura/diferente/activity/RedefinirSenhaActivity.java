package br.com.appgaleria.costura.diferente.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;

public class RedefinirSenhaActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    //private Button btn_redefinir_senha;
    private EditText txt_email;
    private FirebaseAuth autentificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha);

        //getSupportActionBar().hide();

        iniciarComponetes();

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(RedefinirSenhaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void redefinirSenha(View v) {
        String email = txt_email.getText().toString();

        if (!email.isEmpty()) {
            validarEmail(email);
        }
        else {
            Toast.makeText(getApplicationContext(), "Informe um e-mail válido.", Toast.LENGTH_SHORT).show();
        }
    }

    private void validarEmail(String email) {
        autentificacao = ConfigFirebase.getAutentificacao();
        autentificacao.sendPasswordResetEmail(email).addOnCompleteListener(this,task -> {
            if(task.isSuccessful()){
                Toast.makeText(getApplicationContext(), "E-mail para redefinição enviado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(getApplicationContext(), "Falha na redefinição: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.rec_btn_fechar);
        //btn_redefinir_senha = findViewById(R.id.rec_btn_redefinir);
        txt_email = findViewById(R.id.rec_edit_email);
    }
}