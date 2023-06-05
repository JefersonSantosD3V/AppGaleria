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

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MyViewHolder> {
    List<Contato> listaContatos;
    Context contexto;

    public void setFiltroList(List<Contato> filtro){
        this.listaContatos = filtro;
        notifyDataSetChanged();
    }

    public ContatoAdapter(List<Contato> lista,Context contexto)
    {
        this.listaContatos = lista;
        this.contexto = contexto;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_contato_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contato contato = listaContatos.get(position);

        holder.nome.setText(contato.getNome());
        holder.email.setText(contato.getEmail());
        holder.telefone.setText(contato.getTelefone());
        //holder.facebook.setText(contato.getFacebook());
        //holder.instagram.setText(contato.getInstagram());
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome,telefone,email;
        CircleImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textAdapterContato_Nome);
            email = itemView.findViewById(R.id.textAdapterContato_Email);
            telefone = itemView.findViewById(R.id.textAdapterContato_Telefone);
            img = itemView.findViewById(R.id.img_list_contato_adapter);
            //  facebook = itemView.findViewById(R.id.textAdapterFacebook);
            //  instagram = itemView.findViewById(R.id.textAdapterInstagram);
        }
    }

}