package br.com.appgaleria.costura.diferente.activity;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.activity.cadastros.CadastroMoldeActivity;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.helper.Permissao;
import br.com.appgaleria.costura.diferente.model.Info;

public class HomeActivity extends AppCompatActivity {

    private String[] permissoes = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA
    };

    private Info info;
    BottomNavigationView bottomNavigationView;
    private TextView txt_info;
    private Button logout,facebook, instagram; /*btn_salvarInfo,*/
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    protected static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Permissao.validarPermissoes(permissoes, this, 1);

        getSupportActionBar().hide();

        iniciaNavigation();
        iniciarComponentes();

        recuperarInfo();

        logout.setOnClickListener(v -> {
            deslogar();
        });

        facebook.setOnClickListener(v -> {
            sharingToSocialMedia("com.facebook.katana", "testando");
        });
        instagram.setOnClickListener(v -> {
            sharingToSocialMedia("com.instagram.android", "testando");
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Permissão obrigatoria para acesso!");
        builder.setCancelable(false);
        builder.setPositiveButton("Confrimar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deslogar();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sharingToSocialMedia(String application, String linkopen) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, linkopen);
        boolean installed = checkAppInstall(application);
        if (installed) {
            intent.setPackage(application);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Installed application first", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    public void salvarInfo(View v) {
        String txt = txt_info.getText().toString();
        info = new Info();
        info.setInformacao(txt);
        info.salvarInfoBD();
    }

    public void recuperarInfo(){
        DatabaseReference infoRef = firebaseRef.child("home_info");
        infoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Info info = snapshot.getValue(Info.class);
                txt_info.setText(info.getInformacao());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deslogar(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name","");
        editor.apply();

        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
        }

    private void iniciaNavigation(){
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                //  if (item.getItemId() != R.id.menu_home) {
                //    finish();

                switch(item.getItemId()){
                    case R.id.menu_home:
                        return true;
                    case R.id.menu_molde:
                        startActivity(new Intent(getApplicationContext(),MoldeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_favorito:
                        startActivity(new Intent(getApplicationContext(),FavoritoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_aviamento:
                        startActivity(new Intent(getApplicationContext(),AviamentoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_contato:
                        startActivity(new Intent(getApplicationContext(),ContatoActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
            //return false;
            //}
        });
    }

    private void iniciarComponentes(){
        logout=findViewById(R.id.home_logout);
        facebook=findViewById(R.id.home_facebook);
        instagram=findViewById(R.id.home_instagram);
        txt_info=findViewById(R.id.home_info);
        //btn_salvarInfo = findViewById(R.id.home_btn_salve_info);
        }
}