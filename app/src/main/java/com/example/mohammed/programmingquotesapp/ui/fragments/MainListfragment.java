package com.example.mohammed.programmingquotesapp.ui.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.data.QuoteContract;
import com.example.mohammed.programmingquotesapp.data.model.Quote;
import com.example.mohammed.programmingquotesapp.interfaces.ListClickListener;
import com.example.mohammed.programmingquotesapp.sync.QuoteAppSyncAdapter;
import com.example.mohammed.programmingquotesapp.ui.activities.MainActivity;
import com.example.mohammed.programmingquotesapp.ui.adapters.Adapter;
import com.example.mohammed.programmingquotesapp.utilities.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mohammed on 28/07/2017.
 */

public class MainListfragment extends Fragment implements android.app.LoaderManager.LoaderCallbacks<Cursor>   {
    private static final int QUOTE_LOADER =10 ;
    private final String TAG=MainListfragment.class.getSimpleName();
    private android.app.LoaderManager.LoaderCallbacks<Cursor> mCallBAck;
    private int mPosition=RecyclerView.NO_POSITION;
    private ListClickListener mListClickListener;
    private  String [] columns={
            QuoteContract.TABLE+"."+QuoteContract.Items._ID,
            QuoteContract.Items.ID,
            QuoteContract.Items.AUTHOR,
            QuoteContract.Items.QUOTE,
            QuoteContract.Items.PERALINK
    };
    private Adapter mAdapter;
    static final int COL__ID=0;
    static final int COL_ID=1;
    static final int COL_AUTHOR=2;
    static final int COL_QUOTE=3;
    static final int COL_PERMALINK=4;
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }
    private TextView emptyView;

    public MainListfragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListClickListener = (ListClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onViewSelected");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallBAck=this;
        if (this.getActivity().getLoaderManager().getLoader(QUOTE_LOADER)!=null){
            this.getActivity().getLoaderManager().initLoader(QUOTE_LOADER,null,this);
        }else{
            this.getActivity().getLoaderManager().restartLoader(QUOTE_LOADER,null,this);
        }
    }

    RecyclerView quoteList;

    @Override
    public void onResume() {
        super.onResume();
        if(null!=quoteList){
//            Toast.makeText(this.getContext(), "mPosition :"+mPosition, Toast.LENGTH_SHORT).show();
            if(mPosition!=RecyclerView.NO_POSITION){
                if(null!=quoteList)
                    quoteList.smoothScrollToPosition(mPosition);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.main_list_fragment,container,false);
        this.getActivity().getLoaderManager().initLoader(QUOTE_LOADER,null,this);

        emptyView= (TextView) view.findViewById(R.id.emptyView);
        quoteList= (RecyclerView) view.findViewById(R.id.rv_quote_list);

        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        quoteList.setItemAnimator(itemAnimator);
        quoteList.setLayoutManager(new LinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false));

        if (null!=savedInstanceState){
            if (savedInstanceState.containsKey(getResources().getString(R.string.position))){
                mPosition=savedInstanceState.getInt(getResources().getString(R.string.position));
                if(mPosition!=RecyclerView.NO_POSITION){
                    if (null!=quoteList)
                        quoteList.smoothScrollToPosition(mPosition);
                }
            }
        }
        return view;
    }
    public void setListClickListener(ListClickListener mListClickListener){
        this.mListClickListener=mListClickListener;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
//        this.getActivity().getSupportLoaderManager().initLoader(QUOTE_LOADER,null,this);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

            Log.d(TAG,QuoteContract.Items.buildDirUri().toString());

            return new CursorLoader(this.getActivity(),
                    QuoteContract.Items.buildDirUri(),
                    columns,
                    null,
                    null,
                    null);


    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        mAdapter=new Adapter(this.getActivity(), new Adapter.ViewHolderListener() {
            @Override
            public void onItemSelected(Quote quote, int position, ImageView iv) {
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                MainListfragment.this.mPosition=position;
                mListClickListener.onClick(quote,mPosition,iv);
            }
        },emptyView,data);

        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            quoteList.smoothScrollToPosition(mPosition);
        }

        if(data.getCount()==0){
            getActivity().supportPostponeEnterTransition();
        }else{
            quoteList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    if(quoteList.getChildCount()>0)
                        quoteList.getViewTreeObserver().removeOnPreDrawListener(this);
                    int position=mAdapter.getPosition();
                    if(RecyclerView.NO_POSITION==position)
                        position=0;
                    RecyclerView.ViewHolder vh=quoteList.findViewHolderForAdapterPosition(position);
//                    if(vh!=null&&mAutiSelectMode)
//                        mForecastAdapter.selectView(vh);
//                    if(mholdForTranstion){
//                        getActivity().supportStartPostponedEnterTransition();
//                    }
                    return false;
                }
            });
        }
        quoteList.setAdapter(mAdapter);

        if (null!=getArguments()){
            mPosition=getArguments().getInt(getResources().getString(R.string.position));
            if(mPosition!=RecyclerView.NO_POSITION){
                if (null!=quoteList)
                    quoteList.smoothScrollToPosition(mPosition);
            }
        }
        updateEmptyView();
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }


    private  void updateEmptyView(){
        if(mAdapter.getItemCount()==0){

            String message=getString(R.string.empty_quote_list);
            int access= Utility.getNetworkState(getActivity(),-1);
           if(emptyView!=null){
                switch(access){
                    case QuoteAppSyncAdapter.NO_NETWORK:
                        message=getString(R.string.no_internet_access);
                        break;
                    case QuoteAppSyncAdapter.STATUS_SERVER_DOWN:
                        message=getString(R.string.server_not_found);
                        break;

                    default:{
                        if(!Utility.isNetworkConnected(getActivity()))
                            message=getString(R.string.no_internet_access);
                        break;
                    }
                }
                emptyView.setText(message);
               emptyView.setVisibility(View.VISIBLE);
               quoteList.setVisibility(View.INVISIBLE);
            }
        }else{
            emptyView.setVisibility(View.INVISIBLE);
            quoteList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.position),mPosition);

    }

    @Override
    public void onDestroy() {
        getLoaderManager().destroyLoader(QUOTE_LOADER);
        super.onDestroy();
    }
}
