package br.com.appgaleria.costura.diferente.activity.cadastros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.ContatoActivity;
import br.com.appgaleria.costura.diferente.databinding.ActivityCadastroAviamentoBinding;
import br.com.appgaleria.costura.diferente.databinding.ActivityCadastroContatoBinding;
import br.com.appgaleria.costura.diferente.model.Contato;
import br.com.appgaleria.costura.diferente.model.Molde;

public class CadastroContatoActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    private EditText txt_nome, txt_email, txt_telefone/*,txt_instagram,txt_facebook*/;
    private Contato contato;
    private ActivityCadastroContatoBinding binding;
    private boolean novoContato = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroContatoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getSupportActionBar().hide();

        getExtra();
        iniciarComponetes();

        txt_telefone.addTextChangedListener(new PhoneTextWatcher());

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroContatoActivity.this, ContatoActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            contato = (Contato) bundle.getSerializable("contatoSelecionado");
            configContato();
        }
    }

    private void configContato(){
        novoContato = false;

        binding.cadConEditNome.setText(contato.getNome());
        binding.cadConEditEmail.setText(contato.getEmail());
        binding.cadConEditTelefone.setText(contato.getTelefone());
    }

    private class PhoneTextWatcher implements TextWatcher {

        private static final int MAX_PHONE_LENGTH = 10;

        private boolean isFormatting;
        private boolean deletingHyphen;
        private int hyphenStart;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (isFormatting) return;

            // Verifica se o usuário está apagando o hífen
            deletingHyphen = count == 1 && after == 0 && s.charAt(start) == '-';
            hyphenStart = start;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isFormatting) return;

            // Remove os hífens existentes para facilitar a formatação
            String digits = s.toString().replaceAll("[^0-9]", "");

            StringBuilder formatted = new StringBuilder();

            // Formatação personalizada para um telefone com 10 dígitos
            int digitCount = 0;
            int formattedCount = 0;
            while (formattedCount < digits.length()) {
                if (digitCount == 0) {
                    formatted.append('(');
                } else if (digitCount == 2) {
                    formatted.append(")");
                } else if (digitCount == 7) {
                    formatted.append('-');
                }

                // Adiciona o dígito atual
                formatted.append(digits.charAt(formattedCount));

                // Atualiza os contadores
                digitCount++;
                formattedCount++;
            }

            isFormatting = true;
            txt_telefone.setText(formatted.toString());
            txt_telefone.setSelection(formatted.length());

            if (deletingHyphen && hyphenStart > 0) {
                txt_telefone.setSelection(hyphenStart - 1);
            }

            isFormatting = false;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public void cadastrarContato(View v) {
        String nome = txt_nome.getText().toString().trim();
        String email = txt_email.getText().toString().trim();
        String telefone = txt_telefone.getText().toString().trim();

        if (!nome.isEmpty() && !telefone.isEmpty()) {
            if(contato == null) contato = new Contato();
            contato.setNome(nome);
            contato.setTelefone(telefone);
            contato.setEmail(email);

            if(novoContato){
                contato.salvarContato();
                finish();
                Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
            }else{
                contato.salvarContato();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Preencha os campos obrigatórios (*).", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.cadCon_btn_fechar);
        txt_nome = findViewById(R.id.cadCon_edit_nome);
        txt_email = findViewById(R.id.cadCon_edit_email);
        txt_telefone = findViewById(R.id.cadCon_edit_telefone);

        if (novoContato) {
            binding.cadConTituloCadastrar.setText("Cadastrar\nContato");
            binding.cadConBtnCadastrar.setText("CADASTRAR");
        } else {
            binding.cadConTituloCadastrar.setText("Editar\nContato");
            binding.cadConBtnCadastrar.setText("EDITAR");
        }

    }
}