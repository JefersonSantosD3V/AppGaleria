package br.com.appgaleria.costura.diferente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.model.Aviamento;
import br.com.appgaleria.costura.diferente.model.Contato;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MyViewHolder> {

    private List<Contato> listaContatos;

    public ContatoAdapter(List<Contato> lista) {
        this.listaContatos = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                             .inflate(R.layout.lista_contato_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contato contato = listaContatos.get(position);
        holder.contato.setText(contato.getNome());
    }

    @Override
    public int getItemCount() {
        return this.listaContatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView contato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contato = itemView.findViewById(R.id.textListaContato);
        }
    }

}
