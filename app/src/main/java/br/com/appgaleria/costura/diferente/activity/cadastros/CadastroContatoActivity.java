package br.com.appgaleria.costura.diferente.activity.cadastros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.AviamentoActivity;
import br.com.appgaleria.costura.diferente.activity.ContatoActivity;
import br.com.appgaleria.costura.diferente.model.Aviamento;
import br.com.appgaleria.costura.diferente.model.Contato;
import br.com.appgaleria.costura.diferente.model.Usuario;

public class CadastroContatoActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    //private Button btn_cadastrar;
    private EditText txt_nome, txt_email, txt_telefone/*,txt_instagram,txt_facebook*/;
    private Contato contato;

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

    public void cadastrarContato(View v) {
        String nome = txt_nome.getText().toString();
        String email = txt_email.getText().toString();
        String telefone = txt_telefone.getText().toString();
        //String instagram = txt_instagram.getText().toString();
        //String facebook = txt_facebook.getText().toString();

        if (!nome.isEmpty() && !telefone.isEmpty()) {
            contato = new Contato();
            contato.setNome(nome);
            contato.setTelefone(telefone);
            contato.setEmail(email);
            //contato.setInstagram(instagram);
            //contato.setFacebook(facebook);

            contato.salvarContato();

            Intent intent = new Intent(CadastroContatoActivity.this, ContatoActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Preencha os campos obrigatórios (*).", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.cadCon_btn_fechar);
        //btn_cadastrar = findViewById(R.id.cadCon_btn_cadastrar);
        txt_nome = findViewById(R.id.cadCon_edit_nome);
        txt_email = findViewById(R.id.cadCon_edit_email);
        txt_telefone = findViewById(R.id.cadCon_edit_telefone);
        //txt_facebook = findViewById(R.id.cadCon_edit_facebook);
        //txt_instagram = findViewById(R.id.cadCon_edit_instagram);
    }
}