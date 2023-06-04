package br.com.appgaleria.costura.diferente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.model.Aviamento;
import br.com.appgaleria.costura.diferente.model.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

//public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MyViewHolder> {
public class ContatoAdapter extends FirebaseRecyclerAdapter<Contato,ContatoAdapter.myViewHolder>{
  // private ArrayList<Contato> listaContatos;
   // private Context contexto;

    public ContatoAdapter(@NonNull FirebaseRecyclerOptions<Contato> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position,@NonNull Contato contato) {
        //Contato contato = listaContatos.get(position);

        holder.nome.setText(contato.getNome());
        holder.email.setText(contato.getEmail());
        holder.telefone.setText(contato.getTelefone());
        //holder.facebook.setText(contato.getFacebook());
        //holder.instagram.setText(contato.getInstagram());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_contato_adapter, parent, false);
        return new myViewHolder(itemLista);
    }




    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView nome,telefone,email;
        CircleImageView img;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = (TextView)itemView.findViewById(R.id.textAdapterNome);
            email = (TextView)itemView.findViewById(R.id.textAdapterEmail);
            telefone = (TextView)itemView.findViewById(R.id.textAdapterTelefone);
            img = (CircleImageView)itemView.findViewById(R.id.img_list_contato_adapter);
          //  facebook = itemView.findViewById(R.id.textAdapterFacebook);
          //  instagram = itemView.findViewById(R.id.textAdapterInstagram);
        }
    }

}
