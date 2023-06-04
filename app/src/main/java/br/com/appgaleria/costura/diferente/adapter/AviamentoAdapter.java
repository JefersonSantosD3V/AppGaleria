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

public class AviamentoAdapter extends RecyclerView.Adapter<AviamentoAdapter.MyViewHolder> {

    private List<Aviamento> listaAviamentos;

    public AviamentoAdapter(List<Aviamento> lista) {
        this.listaAviamentos = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_aviamento_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Aviamento aviamento = listaAviamentos.get(position);
        holder.aviamento.setText(aviamento.getNome());
    }

    @Override
    public int getItemCount() {
        return this.listaAviamentos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView aviamento;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            aviamento = itemView.findViewById(R.id.aviamento_recyclerView);
        }
    }

}
