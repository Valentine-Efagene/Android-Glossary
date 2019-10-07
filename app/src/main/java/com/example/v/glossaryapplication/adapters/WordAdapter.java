package com.example.v.glossaryapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.v.glossaryapplication.DefinitionActivity;
import com.example.v.glossaryapplication.R;
import com.example.v.glossaryapplication.utilse.GlossaryModel;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {

    public List<GlossaryModel> data;

    public WordAdapter(){

    }
    public  void setData(List<GlossaryModel> data){
        this.data=data;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom Layout
        View wordView = inflater.inflate(R.layout.word_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(wordView, context);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
         GlossaryModel glossaryModel = data.get(position);
         holder.wordText.setText(glossaryModel.getWord());
        Log.d("word",""+glossaryModel.getWord()+ ","+glossaryModel.getDefinition());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Context context;
        public TextView wordText;

        public ViewHolder(View itemView,final Context context) {
            super(itemView);
            this.context = context;

            wordText = (TextView) itemView.findViewById(R.id.wordtext);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    GlossaryModel glossaryModel = data.get(position);

                    Intent intent =new Intent(context, DefinitionActivity.class);
                    intent.putExtra("WORD",glossaryModel.getWord());
                    intent.putExtra("DEFINITION",glossaryModel.getDefinition());

                    context.startActivity(intent);
                }
            });

        }

    }

}
