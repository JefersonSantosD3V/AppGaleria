package br.com.appgaleria.costura.diferente.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.appgaleria.costura.diferente.R;
import br.com.appgaleria.costura.diferente.model.ImagemUpload;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MyViewHolder>{

    private final List<ImagemUpload> urlsImagens;
    //private final Context context;

    public SliderAdapter(List<ImagemUpload> urlsImagens) {
        this.urlsImagens = urlsImagens;
        //this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide_imagem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        ImagemUpload imagemUpload = urlsImagens.get(position);

        Picasso.get().load(imagemUpload.getCaminhoImagem()).into(viewHolder.imgSlide);

        //Glide.with(context)
            //    .load(imagemUpload.getCaminhoImagem())
           //     .into(viewHolder.imgSlide);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getCount() {
        return urlsImagens.size();
    }

    static class MyViewHolder extends SliderViewAdapter.ViewHolder{

        ImageView imgSlide;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgSlide = itemView.findViewById(R.id.imgSlide);
        }
    }
}
