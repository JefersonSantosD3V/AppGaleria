package br.com.appgaleria.costura.diferente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.model.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MyViewHolder> {
    List<Contato> listaContatos;
    Context contexto;
    private OnClickListener onClickListener;

    public void setFiltroList(List<Contato> filtro){
        this.listaContatos = filtro;
        notifyDataSetChanged();
    }

    public ContatoAdapter(List<Contato> lista, Context contexto, OnClickListener onClickListener)
    {
        this.listaContatos = lista;
        this.contexto = contexto;
        this.onClickListener = onClickListener;
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

        holder.itemView.setOnClickListener(v -> onClickListener.OnClick(contato));
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    public interface OnClickListener{
        void OnClick(Contato contato);
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
        }
    }

}