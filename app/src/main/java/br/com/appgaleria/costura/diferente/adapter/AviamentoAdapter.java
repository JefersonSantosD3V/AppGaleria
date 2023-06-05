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

public class AviamentoAdapter extends RecyclerView.Adapter<AviamentoAdapter.MyViewHolder> {

    List<Aviamento> listaAviamentos;
    Context contexto;

    public void setFiltroList(List<Aviamento> filtro){
        this.listaAviamentos = filtro;
        notifyDataSetChanged();
    }

    public AviamentoAdapter(List<Aviamento> lista,Context contexto)
    {
        this.listaAviamentos = lista;
        this.contexto = contexto;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_aviamento_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Aviamento aviamento = listaAviamentos.get(position);

        holder.nome.setText(aviamento.getNome());
        holder.descricao.setText(aviamento.getDescricao());
        holder.quantidade.setText(aviamento.getQuantidade().toString());
        //holder.foto.setText(aviamento.getFoto());
    }

    @Override
    public int getItemCount() {
        return listaAviamentos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome,descricao,quantidade;
        CircleImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textAdapterAviamento_Nome);
            descricao = itemView.findViewById(R.id.textAdapterAviamento_Descricao);
            quantidade = itemView.findViewById(R.id.textAdapterAviamento_Quantidade);
            img = itemView.findViewById(R.id.imgAdapterAviamento_Foto);
        }
    }

}