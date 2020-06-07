package com.example.android.simulation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.simulation.databinding.FieldBinding;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.FieldViewHolder> {

    private List<String> types;
    @NonNull
    @Override
    public RecyclerAdapter.FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FieldBinding fieldBinding = FieldBinding.inflate(layoutInflater, parent, false);
        return new FieldViewHolder(fieldBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        String type = types.get(position);
        holder.bind(type);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull FieldViewHolder holder) {
        holder.itemView.clearAnimation();
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FieldViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    @Override
    public int getItemCount() {
        if(types==null){
            return 0;
        }else {
            return types.size();
        }
    }

    public void setData(List<String> types){
        this.types = types;
        notifyDataSetChanged();
    }

    class FieldViewHolder extends RecyclerView.ViewHolder{

        private FieldBinding fieldBinding;

        public FieldViewHolder(FieldBinding fieldBinding) {
            super(fieldBinding.getRoot());
            this.fieldBinding = fieldBinding;
        }

        void bind(String type){
            fieldBinding.setType(type);
            fieldBinding.setWomen(Grid.TYPE_WOMAN);
            fieldBinding.executePendingBindings();
        }

        void unbind(){
            if(fieldBinding!=null){
                fieldBinding.unbind();
            }
        }
    }
}
