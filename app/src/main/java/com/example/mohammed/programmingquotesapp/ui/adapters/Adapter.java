package com.example.mohammed.programmingquotesapp.ui.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.data.QuoteContract;
import com.example.mohammed.programmingquotesapp.data.model.Quote;
import com.example.mohammed.programmingquotesapp.ui.activities.DetailsActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mohammed on 28/07/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>  {
    Context context;
    private  ViewHolderListener  mViewHolderListener;
    private View emptyView;
    private static  int mPosition;
    public   Cursor cursor;
    int green=0;
    int white=0;
    public Cursor getCursor(){
        return  this.cursor;
    }
    public void swapCursor(Cursor cursor){
        this.cursor=cursor;
        notifyDataSetChanged();
//        emptyView.setVisibility(null!=cursor?View.GONE:View.VISIBLE);
    }
    public Adapter(Context context,ViewHolderListener mViewHolderListener,View emptyView,Cursor cursor){
        this.context=context;
        ButterKnife.inject((Activity) context);
        this.emptyView=emptyView;
        this.mViewHolderListener=mViewHolderListener;
        this.cursor=cursor;
    }
    public static  int getPosition(){
        return mPosition;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(cursor.moveToPosition(position)){
            String author=cursor.getString(cursor.getColumnIndex(QuoteContract.Items.AUTHOR));
            String quote=cursor.getString(cursor.getColumnIndex(QuoteContract.Items.QUOTE));
            holder.tvAuthor.setText(author.substring(0,author.length()>20?20:author.length()-1));
            holder.tvQuote.setText(quote.substring(0,quote.length()>30?30:quote.length()-1));
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_quote);
            RoundedBitmapDrawable rounded= RoundedBitmapDrawableFactory.create(context.getResources(),icon);
            rounded.setCircular(true);
            holder.ivListItem.setImageDrawable(rounded);
        }

    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView tvQuote;
        TextView tvAuthor;
        ImageView ivListItem;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject((Activity) context);
            tvQuote= (TextView) itemView.findViewById(R.id.item_list_tv_quote);
            tvAuthor= (TextView) itemView.findViewById(R.id.item_list_tv_author);
            ivListItem= (ImageView) itemView.findViewById(R.id.item_list_iv);
            itemView.setOnClickListener(this);
            if (green == 0)
                green = itemView.getContext().getResources().getColor(R.color.green);
            if (white == 0)
                white = itemView.getContext().getResources().getColor(R.color.background_material_light);


        }

        @Override
        public void onClick(View v) {
            cursor.moveToPosition(this.getAdapterPosition());


//            Toast.makeText(context, ""+cursor.getInt(0)+"at adapter position "+this.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Quote quote=new Quote(cursor.getString(cursor.getColumnIndex(QuoteContract.Items.AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(QuoteContract.Items.QUOTE)),
                    cursor.getString(cursor.getColumnIndex(QuoteContract.Items.PERALINK)),
                    cursor.getString(cursor.getColumnIndex(QuoteContract.Items.ID)));
            mViewHolderListener.onItemSelected(quote,getAdapterPosition(),ivListItem);
        }
    }
    public interface ViewHolderListener{
        public void onItemSelected(Quote qoute,int position,ImageView iv);
    }
    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.bounce_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }
}
