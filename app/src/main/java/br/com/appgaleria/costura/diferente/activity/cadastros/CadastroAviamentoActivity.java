package br.com.appgaleria.costura.diferente.activity.cadastros;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.AviamentoActivity;
import br.com.appgaleria.costura.diferente.databinding.ActivityCadastroAviamentoBinding;
import br.com.appgaleria.costura.diferente.databinding.BottomSheetDailogImagemBinding;

import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.helper.Permissao;
import br.com.appgaleria.costura.diferente.model.Aviamento;
import br.com.appgaleria.costura.diferente.model.Info;

public class CadastroAviamentoActivity extends AppCompatActivity {

    private ActivityCadastroAviamentoBinding binding;
    private ImageView img_btn_fechar;
    private Aviamento aviamento;
    private String currentPhotoPath;
    private int resultCode = 0;
    private String caminhoImagem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroAviamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       // getSupportActionBar().hide();

        img_btn_fechar = findViewById(R.id.cadAvi_btn_fechar);

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    CadastroAviamentoActivity.this, AviamentoActivity.class);
            startActivity(intent);
            finish();
        });

        binding.cadAviFoto.setOnClickListener(v -> dialogImage());
    }

    private void dialogImage(){
        resultCode = 0;
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
    private void verificaPermissaoCamera(){
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
    private void verificaPermissaoGaleria(){
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
    private void showDialogPermissao(PermissionListener permissionListener, String[] permissoes, String msg){
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedTitle("Permissão Negada")
                .setDeniedMessage(msg)
                .setDeniedCloseButtonText("Não")
                .setGotoSettingButtonText("Sim")
                .setPermissions(permissoes)
                .check();
    }
    public void cadastrarAviamento(View v) {
        String nome = binding.cadAviEditNome.getText().toString().trim();
        String descricao = binding.cadAviEditDescricao.getText().toString().trim();
        String quantidade = binding.cadAviEditQuantidade.getText().toString().trim();
        String caminho = this.caminhoImagem;

        if (!nome.isEmpty() && !descricao.isEmpty() && !quantidade.isEmpty() && !caminho.isEmpty()) {
            aviamento = new Aviamento();
            aviamento.setNome(nome);
            aviamento.setDescricao(descricao);
            aviamento.setQuantidade(Double.valueOf(quantidade));
            salvarImagemFirebase();
        }
        else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos e carregue uma foto.", Toast.LENGTH_SHORT).show();
        }
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
    private void abrirCamera(){
        resultCode = 1;
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
    private void abrirGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private void salvarImagemFirebase(){

        StorageReference storageReference = ConfigFirebase.getStorage()
                .child("imagens")
                .child("aviamentos")
                .child(aviamento.getId())
                .child("imagem.jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                .addOnCompleteListener(task -> {

            aviamento.setUrlImagem(task.getResult().toString());

            aviamento.salvarAviamento();
            Intent intent = new Intent(CadastroAviamentoActivity.this, AviamentoActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso!",
                    Toast.LENGTH_SHORT).show();

        })).addOnFailureListener(e -> Toast.makeText(
                this, "Ocorreu um erro com o upload, tente novamente.",
                Toast.LENGTH_SHORT).show());
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                    if(resultCode == 0){ //galeria

                        Uri imagemSelecionada = result.getData().getData();

                        try {
                            caminhoImagem = imagemSelecionada.toString();

                            binding.cadAviFotoDefault.setVisibility(View.GONE);
                            binding.cadAviFoto.setImageBitmap(getBitmap(imagemSelecionada));

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else{ //camera

                        File file = new File(currentPhotoPath);
                        caminhoImagem = String.valueOf(file.toURI());

                        binding.cadAviFotoDefault.setVisibility(View.GONE);
                        binding.cadAviFoto.setImageURI(Uri.fromFile(file));

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
}