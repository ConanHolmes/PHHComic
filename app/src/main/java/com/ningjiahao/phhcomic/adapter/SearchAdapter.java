package com.ningjiahao.phhcomic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ningjiahao.phhcomic.ILoadSearchData;
import com.ningjiahao.phhcomic.R;

import com.ningjiahao.phhcomic.activity.ManHuaDetailActivity;
import com.ningjiahao.phhcomic.bean.FindContentTitleBean;
import com.ningjiahao.phhcomic.bean.SearchDefaultBean;
import com.ningjiahao.phhcomic.bean.SearchResultBean;
import com.ningjiahao.phhcomic.config.URLConstants;


import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by My on 2016/11/16.
 */

public class SearchAdapter extends RecyclerView.Adapter{

    public static final int TYPE1=1;
    public static final int TYPE2=2;

    public static final int TYPE3=3;

    public static final int TYPE4=4;

    private Context mContext;
    private List<Object>mList;
    private LayoutInflater mInflater;
    private String search;
    private int type;
    private ILoadSearchData iLoadSearchData;


    public SearchAdapter(List<Object> mList, Context mContext,String search,int type,ILoadSearchData iLoadSearchData) {
        this.mList = mList;
        this.mContext = mContext;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.search=search;
        this.type=type;
        this.iLoadSearchData=iLoadSearchData;


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0&&type!=TYPE4) {
            return TYPE1;
        } else if (type == TYPE2) {

            return TYPE2;
        } else {
            return TYPE3;
        }



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case TYPE1:
                view=mInflater.inflate(R.layout.item1_search,parent,false);
               return new ViewHolder1(view);
            case TYPE2:
                view=mInflater.inflate(R.layout.item_search_result,parent,false);
                return new ViewHolder2(view);
            case TYPE3:
                view=mInflater.inflate(R.layout.item_search_default,parent,false);
                return new ViewHolder3(view);
        }

return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder1) {
            ((ViewHolder1)holder).editText_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    search=((ViewHolder1)holder).editText_search.getEditableText().toString();
                }
                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            ((ViewHolder1)holder).editText_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ViewHolder1)holder).editText_search.setCursorVisible(true);

                }
            });
            ((ViewHolder1)holder).editText_search.setText(search);
            ((ViewHolder1)holder).imageView_find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text=((ViewHolder1)holder).editText_search.getEditableText().toString();
                    iLoadSearchData.updata(text);

                }
            });
            if (!TextUtils.isEmpty(search) & type == TYPE2) {
                ((ViewHolder1) holder).textView_result.setText("一共" + mList.size() + "个结果");
            }else if(!TextUtils.isEmpty(search)&type==TYPE3){
                ((ViewHolder1)holder).textView_result.setText("一共0个结果");
                ((ViewHolder1)holder).textView_recommend.setText("对不起没有找到结果，看看我们的推荐吧");
            }
        }

        if (holder instanceof ViewHolder2){


            Glide.with(mContext).load(URLConstants.BASE_IMAGE_URL+((SearchResultBean.CBean.SBean)(mList.get(position-1))).getAppicons())
                    .into(((ViewHolder2)holder).imageView_appi);
            ((ViewHolder2)holder).textView_name.setText(((SearchResultBean.CBean.SBean)(mList.get(position-1))).getName());
            ((ViewHolder2)holder).textView_tname.setText(((SearchResultBean.CBean.SBean)(mList.get(position-1))).getTname());
            ((ViewHolder2)holder).textView_cfyname.setText(((SearchResultBean.CBean.SBean)(mList.get(position-1))).getCfyname());
            ((ViewHolder2)holder).textView_author.setText(((SearchResultBean.CBean.SBean)(mList.get(position-1))).getAuthor());
            ((ViewHolder2)holder).textView_partname.setText("更新到"+((SearchResultBean.CBean.SBean)(mList.get(position-1))).getPartname());
            ((ViewHolder2)holder).textView_score.setText(((SearchResultBean.CBean.SBean)(mList.get(position-1))).getScore());
            ((ViewHolder2)holder).textView_updata.setText(((SearchResultBean.CBean.SBean)(mList.get(position-1))).getTh());
            ((ViewHolder2)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent=new Intent();
//                    intent.setClass(mContext, DetailActivity.class);
//                    String id=((SearchResultBean.CBean.SBean)(mList.get(position-1))).getId();
//                    intent.putExtra(URLConstants.KEY_DETAIL_ID,id);
//                    mContext.startActivity(intent);
                }
            });
        }
        if (holder instanceof ViewHolder3){

              int index=0;
                        switch (type){
                            case 3:
                                index=position-1;
                                Glide.with(mContext)
                                        .load(URLConstants.BASE_IMAGE_URL+
                                                ((SearchDefaultBean.CBean.SBean)(mList.get(index))).getAppicons())
                                        .placeholder(R.drawable.ticai_placeimage)
                                        .into(((ViewHolder3)holder).imageView);
                                ((ViewHolder3)holder).textView_name.setText(((SearchDefaultBean.CBean.SBean)(mList.get(index))).getName());

                                ((ViewHolder3)holder).textView_partname.setText(((SearchDefaultBean.CBean.SBean)(mList.get(index))).getPartname());
                                ((ViewHolder3)holder).textView_score.setText(((SearchDefaultBean.CBean.SBean)(mList.get(index))).getScore());

                                break;
                            case 4:
                                index=position;
                                Glide.with(mContext)
                                        .load(URLConstants.BASE_IMAGE_URL+
                                                ((SearchResultBean.CBean.SBean)(mList.get(index))).getAppicons())
                                        .placeholder(R.drawable.ticai_placeimage)
                                        .into(((ViewHolder3)holder).imageView);
                                ((ViewHolder3)holder).textView_name.setText(((SearchResultBean.CBean.SBean)(mList.get(index))).getName());

                                ((ViewHolder3)holder).textView_partname.setText(((SearchResultBean.CBean.SBean)(mList.get(index))).getPartname());
                                ((ViewHolder3)holder).textView_score.setText(((SearchResultBean.CBean.SBean)(mList.get(index))).getScore());

                        }


        }

    }

    @Override
    public int getItemCount() {

                if(type==TYPE4){
               return mList.size();
                }
        else {
                    return mList.size()+1;
                }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder{
        TextView textView_result,textView_recommend;
        EditText editText_search;
        ImageView imageView_find;
        public ViewHolder1(View itemView) {
            super(itemView);
            textView_result= (TextView) itemView.findViewById(R.id.textview_search_result);
            textView_recommend= (TextView) itemView.findViewById(R.id.textview_search_recommend);
            editText_search= (EditText) itemView.findViewById(R.id.edittext_search);
            imageView_find= (ImageView) itemView.findViewById(R.id.imageview_search_find);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder{

        private ImageView imageView_appi;
        private TextView textView_name,textView_tname,textView_cfyname,
        textView_author,textView_partname,textView_updata,textView_score;
        public ViewHolder2(View itemView) {
            super(itemView);
            imageView_appi= (ImageView) itemView.findViewById(R.id.imageview_search_item_appi);
            textView_name= (TextView) itemView.findViewById(R.id.textview_search_item_name);
            textView_tname= (TextView) itemView.findViewById(R.id.textview_search_item_tname);
            textView_cfyname= (TextView) itemView.findViewById(R.id.textview_search_item_cfyname);
            textView_author= (TextView) itemView.findViewById(R.id.textview_search_item_author);
            textView_partname= (TextView) itemView.findViewById(R.id.textview_search_item_partname);
            textView_updata= (TextView) itemView.findViewById(R.id.textview_search_item_updata);
            textView_score= (TextView) itemView.findViewById(R.id.textview_search_item_score);


        }
    }

    class ViewHolder3 extends RecyclerView.ViewHolder{
        private ImageView imageView;

        private TextView textView_score,textView_partname,textView_name;
        public ViewHolder3(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.imageview_search_default_item);
            textView_score= (TextView) itemView.findViewById(R.id.textview_search_default_score);
            textView_name= (TextView) itemView.findViewById(R.id.textview_search_default_name);
            textView_partname= (TextView) itemView.findViewById(R.id.textview_search_default_partname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }
    }
}
