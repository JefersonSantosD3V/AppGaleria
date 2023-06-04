package br.com.appgaleria.costura.diferente.activity.cadastros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.LoginActivity;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    //private Button btn_cadastrar;
    private EditText txt_email, txt_senha;
    private FirebaseAuth autentificacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        getSupportActionBar().hide();

        iniciarComponetes();

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void cadastrarUsuario(View v) {
        String email = txt_email.getText().toString();
        String senha = txt_senha.getText().toString();

        if (!email.isEmpty() && !senha.isEmpty()) {
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(senha);

            validarCadastroUsuario(usuario);
        }
        else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void validarCadastroUsuario(Usuario usuario) {
        autentificacao = ConfigFirebase.getAutentificacao();
        autentificacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            } else {
                String excecao = "";
                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e) {
                    excecao = "A senha deve deve ter no mínimo 6 números.";
                }catch (FirebaseAuthInvalidCredentialsException e){
                    excecao = "Digite um e-mail válido.";
                }catch (FirebaseAuthUserCollisionException e){
                    excecao = "Este e-mail já esta cadastrado.";
                }catch (Exception e) {
                    excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), excecao, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.cad_btn_fechar);
        //btn_cadastrar = findViewById(R.id.cad_btn_cadastrar);
        txt_email = findViewById(R.id.cad_edit_email);
        txt_senha = findViewById(R.id.cad_edit_senha);
    }
}