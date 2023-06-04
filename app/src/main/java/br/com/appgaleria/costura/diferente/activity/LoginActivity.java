package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroUsuarioActivity;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private Button btn_cadastrar;
    private TextView txt_recuperar;
    //private Button btn_entrar;
    private EditText txt_email, txt_senha;
    private ProgressBar progress_bar;
    //private CheckBox checkbox;
    private Usuario usuario;
    private FirebaseAuth autentificacao;
    protected static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //esconde actcionbar
        getSupportActionBar().hide();

        iniciarComponentes();

        btn_cadastrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
            startActivity(intent);
        });
        txt_recuperar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RedefinirSenhaActivity.class);
            startActivity(intent);
        });

        checkbox();
    }

    public void entrarApp(View v) {
        progress_bar.setVisibility(View.VISIBLE);
        String email = txt_email.getText().toString();
        String senha = txt_senha.getText().toString();

        if (!email.isEmpty() && !senha.isEmpty()) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(senha);

            validarLogin(usuario);

        } else {
            Toast.makeText(getApplicationContext(), "Informe seu e-mail e senha.", Toast.LENGTH_SHORT).show();
        }
    }

    private void validarLogin(Usuario usuario) {
        autentificacao = ConfigFirebase.getAutentificacao();
        autentificacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progress_bar.setVisibility(View.GONE);
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name","true");
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    progress_bar.setVisibility(View.GONE);
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não cadastrado.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Senha incorreta.";
                    } catch (Exception e) {
                        excecao = "Erro ao logar o usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkbox() {
         SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
         String check = sharedPreferences.getString("name","");
         if(check.equals("true")){
             startActivity(new Intent(getApplicationContext(), HomeActivity.class));
             finish();
         }
    }

    private void iniciarComponentes() {
        btn_cadastrar = findViewById(R.id.log_btn_cadastrar);
        txt_recuperar = findViewById(R.id.log_txt_recuperar);
        //btn_entrar = findViewById(R.id.log_btn_entrar);
        txt_email = findViewById(R.id.log_edit_email);
        txt_senha = findViewById(R.id.log_edit_senha);
        progress_bar = findViewById(R.id.log_progress_bar);
        //checkbox = findViewById(R.id.log_check_box);
    }
}