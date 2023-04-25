package com.example.proyectodas2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListaElementos> mData;
    private LayoutInflater mInflater;
    private Context context;
    final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ListaElementos lista);
    }


    public ListAdapter(List<ListaElementos> lista, Context context, OnItemClickListener listener){
        this.mInflater=LayoutInflater.from (context);
        this.mData= lista;
        this.context=context;
        this.listener= listener;
    }

    public int getItemCount(){
        return mData.size ();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.from(parent.getContext ()).inflate(R.layout.activity_lista_elementos,parent ,false);
        return new ViewHolder (view);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.name.setText (mData.get(position).getName ());
        holder.imagen.setImageResource (mData.get (position).foId);
        holder.bindData (mData.get (position));
    }

    public void setItems (List<ListaElementos> items){
        mData=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagen;
        TextView name;


        ViewHolder(View itemView){
            super(itemView);
            imagen=itemView.findViewById (R.id.imagen1);
            name=itemView.findViewById (R.id.nombreDelAspecto);

        }

        void bindData(final ListaElementos lista){
            itemView.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    listener.onItemClick (lista);
                }
            });
        }
    }
}