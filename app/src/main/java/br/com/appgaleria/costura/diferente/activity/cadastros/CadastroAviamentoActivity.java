package br.com.appgaleria.costura.diferente.activity.cadastros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.AviamentoActivity;
import br.com.appgaleria.costura.diferente.helper.Permissao;
import br.com.appgaleria.costura.diferente.model.Aviamento;
import br.com.appgaleria.costura.diferente.model.Info;

public class CadastroAviamentoActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    //private Button btn_cadastrar;
    private EditText txt_nome, txt_descricao, txt_quantidade;
    private FloatingActionButton camera;
    private Aviamento aviamento;

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

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CadastroAviamentoActivity.this, "camera", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cadastrarAviamento(View v) {
        String nome = txt_nome.getText().toString();
        String descricao = txt_descricao.getText().toString();
        String quantidade = txt_quantidade.getText().toString();

        if (!nome.isEmpty() && !descricao.isEmpty() && !quantidade.isEmpty()) {
            aviamento = new Aviamento();
            aviamento.setNome(nome);
            aviamento.setDescricao(descricao);
            aviamento.setQuantidade(Double.valueOf(quantidade));

            aviamento.salvarAviamento();

            Intent intent = new Intent(CadastroAviamentoActivity.this, AviamentoActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.cadAvi_btn_fechar);
        //btn_cadastrar = findViewById(R.id.cadAvi_btn_cadastrar);
        camera = findViewById(R.id.cadAvi_btn_camera);
        txt_nome = findViewById(R.id.cadAvi_edit_nome);
        txt_descricao = findViewById(R.id.cadAvi_edit_descricao);
        txt_quantidade = findViewById(R.id.cadAvi_edit_quantidade);
    }
}