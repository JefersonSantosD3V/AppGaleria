package br.com.appgaleria.costura.diferente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.helper.ConfigFirebase;
import br.com.appgaleria.costura.diferente.model.Molde;

public class MoldeAdapter extends RecyclerView.Adapter<MoldeAdapter.MyViewHolder>{

    private List<Molde> listaMoldes;
    private Context context;
    private OnClickLister onClickLister;
    private OnClickFavorito onClickFavorito;
    private boolean favorito;
    private List<String> idsFavoritos;

    public MoldeAdapter(List<Molde> listaMoldes, Context context, OnClickLister onClickLister, MoldeAdapter.OnClickFavorito onClickFavorito, boolean favorito, List<String> idsFavoritos) {
        this.listaMoldes = listaMoldes;
        this.context = context;
        this.onClickLister = onClickLister;
        this.onClickFavorito = onClickFavorito;
        this.favorito = favorito;
        this.idsFavoritos = idsFavoritos;
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

        holder.txtNomeMolde.setText(molde.getNome());

        if(favorito){
            holder.likeButton.setLiked(idsFavoritos.contains(molde.getId()));
        }else {
            holder.likeButton.setVisibility(View.GONE);
        }

        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(ConfigFirebase.getAutentifica()){
                    onClickFavorito.onClickFavorito(molde);
                }else {
                    Toast.makeText(context, "Você não está autenticado no app.", Toast.LENGTH_SHORT).show();
                    holder.likeButton.setLiked(false);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                onClickFavorito.onClickFavorito(molde);
            }
        });

        for (int i = 0; i < molde.getUrlsImagens().size(); i++) {
            if (molde.getUrlsImagens().get(i).getIndex() == 0) {
                Glide.with(context)
                        .load(molde.getUrlsImagens().get(i).getCaminhoImagem())
                        .into(holder.imagemMolde);
            }
        }

        holder.txtDescricaoMolde.setText(molde.getDescricao());
        holder.itemView.setOnClickListener(v -> onClickLister.onClick(molde));

    }

    @Override
    public int getItemCount() {
        return this.listaMoldes.size();
    }

    public interface OnClickLister{
          void onClick(Molde molde);
    }

    public interface OnClickFavorito{
        void onClickFavorito(Molde molde);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagemMolde;
        TextView txtNomeMolde,txtDescricaoMolde;
        LikeButton likeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           imagemMolde = itemView.findViewById(R.id.imgAdapterMolde_Foto);
           txtNomeMolde = itemView.findViewById(R.id.textAdapterMolde_Nome);
           txtDescricaoMolde = itemView.findViewById(R.id.textAdapterMolde_Descrição);
           likeButton = itemView.findViewById(R.id.likeButton);
        }
    }
}
