package br.com.appgaleria.costura.diferente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.appgaleria.costura.diferente.R;

public class TamanhoDialogAdapter extends RecyclerView.Adapter<TamanhoDialogAdapter.MyViewHolder> {

    private final List<String> idsTamanhosSelecionadas;
    private final List<String> tamanhoList;
    private final Context context;
    private final OnClick onClick;

    public TamanhoDialogAdapter(List<String> idsTamanhosSelecionadas, List<String> tamanhoList, Context context, OnClick onClick) {
        this.idsTamanhosSelecionadas = idsTamanhosSelecionadas;
        this.tamanhoList = tamanhoList;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tamanho_dialog, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String tamanho = tamanhoList.get(position);

        holder.nomeTamanho.setText(tamanho);

        if(idsTamanhosSelecionadas.contains(tamanho)){
            holder.checkBox.setChecked(true);
        }

        holder.itemView.setOnClickListener(v -> {
            onClick.onClickListener(tamanho);

            holder.checkBox.setChecked(!holder.checkBox.isChecked());
        });
    }

    @Override
    public int getItemCount() {
        return tamanhoList.size();
    }

    public interface OnClick {
        void onClickListener(String tamanho);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTamanho;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTamanho = itemView.findViewById(R.id.nomeTamanho);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

}
