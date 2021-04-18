package com.ankita.dell.quiz_app.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ankita.dell.quiz_app.R;

/**
 * Created by Dell on 19-Jan-19.
 */

public class ScoreDetailViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_name,txt_score;
    public ScoreDetailViewHolder(View itemView) {
        super(itemView);

        txt_name=(TextView)itemView.findViewById(R.id.txt_name);
        txt_score=(TextView)itemView.findViewById(R.id.txt_score);
    }
}
