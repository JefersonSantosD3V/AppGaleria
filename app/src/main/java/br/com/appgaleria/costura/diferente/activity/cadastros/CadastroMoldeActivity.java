package br.com.appgaleria.costura.diferente.activity.cadastros;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.MoldeActivity;
import br.com.appgaleria.costura.diferente.adapter.TamanhoDialogAdapter;
import br.com.appgaleria.costura.diferente.databinding.ActivityCadastroMoldeBinding;
import br.com.appgaleria.costura.diferente.databinding.BottomSheetDailogImagemBinding;
import br.com.appgaleria.costura.diferente.databinding.DialogFormMoldeTamanhoBinding;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.ImagemUpload;
import br.com.appgaleria.costura.diferente.model.Molde;

public class CadastroMoldeActivity extends AppCompatActivity implements TamanhoDialogAdapter.OnClick{
    private ImageView img_btn_fechar;
    private Spinner spinner_tipo,spinner_categoria,spinner_genero;
    private ArrayAdapter<String> adapterListCategoria,adapterListTipo,adapterListGenero;
    private List<String> listTipo,listTamanho,listCategoriaTop,listGenero,listCategoriaBottom,
            listCategoriaHibrido,listCategoriaOutros;
    private final List<ImagemUpload> imagemUploadList = new ArrayList<>();
    private Molde molde;
    private boolean novoMolde = true;
    private ActivityCadastroMoldeBinding binding;
    private int resultCode = 0;
    private String currentPhotoPath;
    private AlertDialog dialog;
    private DialogFormMoldeTamanhoBinding tamanhoBinding;
    private final List<String> idsTamanhosSelecionadas = new ArrayList<>();
    private final List<String> TamanhoSelecionadaList = new ArrayList<>();
    private String tipoSelecionado;
    private String catelgoriaSelecionada;
    private String generoSelecionado;
    private int idTipo, idCategoria, idGenero;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroMoldeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       // getSupportActionBar().hide();

        criarListasDropdown();
        getExtra();
        configClicks();
        iniciarComponentes();
        configuraTamanhosEdicao();
        selecaoSpinner(0,0,0);

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroMoldeActivity.this, MoldeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(novoMolde == false) {
            selecaoSpinner(idTipo, idCategoria, idGenero);
        }
    }

    public void cadastrarMolde(View v) {
        String nome = binding.cadMolEditNome.getText().toString().trim();
        String descricao = binding.cadMolEditDescricao.getText().toString().trim();

        if (!nome.isEmpty()) {
                if(!idsTamanhosSelecionadas.isEmpty()){
                    if (molde == null) molde = new Molde();

                    molde.setNome(nome);
                    molde.setDescricao(descricao);
                    molde.setTamanhos(idsTamanhosSelecionadas);
                    molde.setTipo(tipoSelecionado);
                    molde.setCategoria(catelgoriaSelecionada);
                    molde.setGenero(generoSelecionado);

                    if (novoMolde) {
                        if(imagemUploadList.size() == 3){
                            for(int i = 0; i < imagemUploadList.size(); i++){
                                salvarImagemFirebase(imagemUploadList.get(i), i);
                            }
                        }else{
                           //ocultaTeclado();
                            Toast.makeText(this, "Carregue 3 imagens.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ocultaTeclado();
                        if(imagemUploadList.size() > 0){
                            for(int i = 0; i < imagemUploadList.size(); i++){
                                salvarImagemFirebase(imagemUploadList.get(i), i);
                            }
                        }else{
                            molde.salvarMolde(false);
                        }
                    }
                }else{
                    ocultaTeclado();
                    Toast.makeText(this, "Seleciona pelo menos um tamanho para o molde.",
                            Toast.LENGTH_SHORT).show();
                }
        }else{
            binding.cadMolEditNome.setError("Informação obrigatória.");
        }
    }

    private void getExtra() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            molde = (Molde) bundle.getSerializable("moldeSelecionado");
            configProduto();
        }
    }

    private void configProduto() {
        novoMolde = false;

        idsTamanhosSelecionadas.addAll(molde.getTamanhos());

        binding.cadMoldeFotoDefault1.setVisibility(View.GONE);
        binding.cadMoldeFotoDefault2.setVisibility(View.GONE);
        binding.cadMoldeFotoDefault3.setVisibility(View.GONE);

        Glide.with(this)
                .load(molde.getUrlsImagens().get(0).getCaminhoImagem())
                .into(binding.cadMoldeFoto1);

        Glide.with(this)
                .load(molde.getUrlsImagens().get(1).getCaminhoImagem())
                .into(binding.cadMoldeFoto2);

        Glide.with(this)
                .load(molde.getUrlsImagens().get(2).getCaminhoImagem())
                .into(binding.cadMoldeFoto3);

        binding.cadMolEditNome.setText(molde.getNome());
        binding.cadMolEditDescricao.setText(molde.getDescricao());

        idTipo = listTipo.indexOf(molde.getTipo());
        idCategoria = listCategoriaHibrido.indexOf(molde.getCategoria());
        idGenero = listGenero.indexOf(molde.getGenero());

    }

    private void configClicks() {
        binding.cadMoldeFoto1.setOnClickListener(v -> dialogImage(0));
        binding.cadMoldeFoto2.setOnClickListener(v -> dialogImage(1));
        binding.cadMoldeFoto3.setOnClickListener(v -> dialogImage(2));
    }

    private void configRv() {
        tamanhoBinding.rvTamanhos.setLayoutManager(new LinearLayoutManager(this));
        tamanhoBinding.rvTamanhos.setHasFixedSize(true);
        TamanhoDialogAdapter tamanhoDialogAdapter = new TamanhoDialogAdapter(idsTamanhosSelecionadas, listTamanho, this, this);
        tamanhoBinding.rvTamanhos.setAdapter(tamanhoDialogAdapter);
    }

    public void showDialogTamanhos(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog2);

        tamanhoBinding = DialogFormMoldeTamanhoBinding
                .inflate(LayoutInflater.from(this));

        tamanhoBinding.btnFechar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        tamanhoBinding.btnSalvar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        configRv();

        builder.setView(tamanhoBinding.getRoot());

        dialog = builder.create();
        dialog.show();
    }

    private void configuraTamanhosEdicao() {
        if(!novoMolde){ // Edição
            for (String tamanho : listTamanho){
                if(molde.getTamanhos().contains(tamanho)){
                    TamanhoSelecionadaList.add(tamanho);
                }
            }
            tamanhosSelecionados();
        }
    }

    private void tamanhosSelecionados() {
        StringBuilder tamanhos = new StringBuilder();
        for (int i = 0; i < TamanhoSelecionadaList.size(); i++) {
            if (i != TamanhoSelecionadaList.size() - 1) {
                tamanhos.append(TamanhoSelecionadaList.get(i)).append(", ");
            } else {
                tamanhos.append(TamanhoSelecionadaList.get(i));
            }
        }

        if (!TamanhoSelecionadaList.isEmpty()) {
            binding.cadMolDropdownTamanho.setText(tamanhos);
        } else {
            binding.cadMolDropdownTamanho.setText("Nenhuma tamanho selecionada");
        }
    }

    private void dialogImage(int code) {
        resultCode = code;

        BottomSheetDailogImagemBinding bottomSheetDailogImagemBinding =
                BottomSheetDailogImagemBinding.inflate(LayoutInflater.from(this));

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                this,R.style.BottomSheetDialog);

        bottomSheetDialog.setContentView(bottomSheetDailogImagemBinding.getRoot());
        bottomSheetDialog.show();

        bottomSheetDailogImagemBinding.dailogBtnCamera.setOnClickListener(v -> {
            verificaPermissaoCamera();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDailogImagemBinding.dailogBtnGaleria.setOnClickListener(v -> {
            verificaPermissaoGaleria();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDailogImagemBinding.dailogBtnCancelar.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
    }

    private void verificaPermissaoCamera() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirCamera();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão Negada.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        showDialogPermissao(
                permissionlistener,
                new String[]{Manifest.permission.CAMERA},
                "Permissão para acessar a câmera necessaria, ativar a permissão agora?"
        );
    }

    private void verificaPermissaoGaleria() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                abrirGaleria();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getBaseContext(), "Permissão Negada.", Toast.LENGTH_SHORT).show();
            }
        };
        showDialogPermissao(
                permissionlistener,
                new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                "Permissão para acessar a galeria necessaria, ativar a permissão agora?"
        );
    }

    private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes, String msg) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão negada")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void abrirCamera() {
        switch (resultCode) {
            case 0:
                resultCode = 3;
                break;
            case 1:
                resultCode = 4;
                break;
            case 2:
                resultCode = 5;
                break;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "br.com.appgaleria.costura.diferente.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            resultLauncher.launch(takePictureIntent);
        }
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private void configUpload(String caminhoImagem) {
        int request = 0;
        switch (resultCode) {
            case 0:
            case 3:
                request = 0;
                break;
            case 1:
            case 4:
                request = 1;
                break;
            case 2:
            case 5:
                request = 2;
                break;
        }

        ImagemUpload imagemUpload = new ImagemUpload(request, caminhoImagem);

        if (!imagemUploadList.isEmpty()) {

            boolean encontrou = false;
            for (int i = 0; i < imagemUploadList.size(); i++) {
                if (imagemUploadList.get(i).getIndex() == request) {
                    encontrou = true;
                }
            }

            if (encontrou) {
                imagemUploadList.set(request, imagemUpload);
            } else {
                imagemUploadList.add(imagemUpload);
            }

        } else {
            imagemUploadList.add(imagemUpload);
        }
    }

    private void salvarImagemFirebase(ImagemUpload imagemUpload, int count) {
        int index = imagemUpload.getIndex();
        String caminhoImagem = imagemUpload.getCaminhoImagem();

        StorageReference storageReference = ConfigFirebase.getStorage()
                .child("imagens")
                .child("moldes")
                .child(molde.getId())
                .child("imagem" + index + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            imagemUpload.setCaminhoImagem(task.getResult().toString());

            if(novoMolde){
                molde.getUrlsImagens().add(imagemUpload);
            }else {
                molde.getUrlsImagens().set(index, imagemUpload);
            }

            if (imagemUploadList.size() == count + 1) {
                molde.salvarMolde(novoMolde);
                imagemUploadList.clear();

                if(novoMolde){
                    finish();
                }

            }

        })).addOnFailureListener(e -> Toast.makeText(
                this, "Ocorreu um erro com o upload, tente novamente.",
                Toast.LENGTH_SHORT).show());
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                    String caminhoImagem;

                    if (resultCode <= 2) { // Galeria

                        Uri imagemSelecionada = result.getData().getData();

                        try {

                            caminhoImagem = imagemSelecionada.toString();

                            switch (resultCode) {
                                case 0:
                                    binding.cadMoldeFotoDefault1.setVisibility(View.GONE);
                                    binding.cadMoldeFoto1.setImageBitmap(getBitmap(imagemSelecionada));
                                    break;
                                case 1:
                                    binding.cadMoldeFotoDefault2.setVisibility(View.GONE);
                                    binding.cadMoldeFoto2.setImageBitmap(getBitmap(imagemSelecionada));
                                    break;
                                case 2:
                                    binding.cadMoldeFotoDefault3.setVisibility(View.GONE);
                                    binding.cadMoldeFoto3.setImageBitmap(getBitmap(imagemSelecionada));
                                    break;
                            }

                            configUpload(caminhoImagem);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else { // Câmera

                        File file = new File(currentPhotoPath);
                        caminhoImagem = String.valueOf(file.toURI());

                        switch (resultCode) {
                            case 3:
                                binding.cadMoldeFotoDefault1.setVisibility(View.GONE);
                                binding.cadMoldeFoto1.setImageURI(Uri.fromFile(file));
                                break;
                            case 4:
                                binding.cadMoldeFotoDefault2.setVisibility(View.GONE);
                                binding.cadMoldeFoto2.setImageURI(Uri.fromFile(file));
                                break;
                            case 5:
                                binding.cadMoldeFotoDefault3.setVisibility(View.GONE);
                                binding.cadMoldeFoto3.setImageURI(Uri.fromFile(file));
                                break;
                        }

                        configUpload(caminhoImagem);

                    }
                }
            }
    );

    private Bitmap getBitmap(Uri caminhoUri) {
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), caminhoUri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), caminhoUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // Oculta o teclado do dispotivo
    private void ocultaTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(binding.cadMolEditNome.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void iniciarComponentes() {
        img_btn_fechar = findViewById(R.id.cadMol_btn_fechar);

        if (novoMolde) {
            binding.cadMolTituloCadastrar.setText("Cadastrar\nMolde");
            binding.cadMolBtnCadastrar.setText("CADASTRAR");
        } else {
            binding.cadMolTituloCadastrar.setText("Editar\nMolde");
            binding.cadMolBtnCadastrar.setText("EDITAR");
        }
    }

    private void selecaoSpinner(int idTipo,int idCategoria, int idGenero){
        adapterListTipo = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, listTipo);
        adapterListTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterListTipo.notifyDataSetChanged();
        spinner_tipo.setAdapter(adapterListTipo);
        spinner_tipo.setSelection(idTipo);

        adapterListGenero = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, listGenero);
        adapterListGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterListGenero.notifyDataSetChanged();
        spinner_genero.setAdapter(adapterListGenero);
        spinner_genero.setSelection(idGenero);

        spinner_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selecao = position;
                if(selecao == 0) {
                    adapterListCategoria = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, listCategoriaTop);
                }
                else if(selecao == 1){
                    adapterListCategoria = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, listCategoriaBottom);
                }
                else if(selecao == 2){
                    adapterListCategoria = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, listCategoriaHibrido);
                }
                else if(selecao == 3){
                    adapterListCategoria = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, listCategoriaOutros);
                }

                adapterListCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterListCategoria.notifyDataSetChanged();
                spinner_categoria.setAdapter(adapterListCategoria);
                spinner_categoria.setSelection(idCategoria);

                tipoSelecionado = adapterListTipo.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                generoSelecionado = adapterListGenero.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catelgoriaSelecionada = adapterListCategoria.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void criarListasDropdown(){
        spinner_tipo = findViewById(R.id.cadMol_spinner_tipo);
        spinner_categoria = findViewById(R.id.cadMol_spinner_categoria);
        spinner_genero = findViewById(R.id.cadMol_spinner_genero);

        listTipo = Arrays.asList(getResources().getStringArray(R.array.spinner_tipo));
        listGenero = Arrays.asList(getResources().getStringArray(R.array.spinner_genero));
        listTamanho = Arrays.asList(getResources().getStringArray(R.array.dropdown_tamanho));
        listCategoriaTop = Arrays.asList(getResources().getStringArray(R.array.categoria_top));
        listCategoriaBottom  = Arrays.asList(getResources().getStringArray(R.array.categoria_bottom));
        listCategoriaHibrido = Arrays.asList(getResources().getStringArray(R.array.categoria_hibrido));
        listCategoriaOutros = Arrays.asList(getResources().getStringArray(R.array.categoria_outros));
    }

    @Override
    public void onClickListener(String tamanho) {
        if (!idsTamanhosSelecionadas.contains(tamanho)) { // Adc
            idsTamanhosSelecionadas.add(tamanho);
            TamanhoSelecionadaList.add(tamanho);
        } else { // Del
            idsTamanhosSelecionadas.remove(tamanho);
            TamanhoSelecionadaList.remove(tamanho);
        }

        tamanhosSelecionados();
    }
}