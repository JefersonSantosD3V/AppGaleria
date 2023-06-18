package br.com.appgaleria.costura.diferente.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.model.Aviamento;

public class AviamentoAdapter extends RecyclerView.Adapter<AviamentoAdapter.MyViewHolder> {

    List<Aviamento> listaAviamentos;
    Context contexto;
    private OnClickListener onClickListener;

    public void setFiltroList(List<Aviamento> filtro){
        this.listaAviamentos = filtro;
        notifyDataSetChanged();
    }

    public AviamentoAdapter(List<Aviamento> lista,Context contexto,OnClickListener onClickListener)
    {
        this.listaAviamentos = lista;
        this.contexto = contexto;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.lista_aviamento_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Aviamento aviamento = listaAviamentos.get(position);

        Picasso.get().load(aviamento.getUrlImagem()).into(holder.img_aviamento);
        holder.nome.setText(aviamento.getNome());
        holder.descricao.setText(aviamento.getDescricao());
        holder.quantidade.setText(aviamento.getQuantidade().toString());

        holder.itemView.setOnClickListener(v -> onClickListener.OnClick(aviamento));

    }

    @Override
    public int getItemCount() {
        return listaAviamentos.size();
    }

    public interface OnClickListener{
        void OnClick(Aviamento aviamento);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome,descricao,quantidade;
        ImageView img_aviamento;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textAdapterAviamento_Nome);
            descricao = itemView.findViewById(R.id.textAdapterAviamento_Descricao);
            quantidade = itemView.findViewById(R.id.textAdapterAviamento_Quantidade);
            img_aviamento = itemView.findViewById(R.id.imgAdapterAviamento_Foto);
        }
    }

}