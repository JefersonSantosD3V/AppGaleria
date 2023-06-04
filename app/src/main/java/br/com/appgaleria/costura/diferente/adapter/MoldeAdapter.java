package br.com.appgaleria.costura.diferente.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.model.Molde;

public class MoldeAdapter extends RecyclerView.Adapter<MoldeAdapter.MyViewHolder>{

    private List<Molde> listaMoldes;

    public MoldeAdapter(List<Molde> lista) {
        this.listaMoldes = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_molde_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Molde molde = listaMoldes.get(position);
      //  holder.molde.setText(molde.getNome());
    }

    @Override
    public int getItemCount() {
        return this.listaMoldes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView molde;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            molde = itemView.findViewById(R.id.textListaMolde);
        }
    }
}
