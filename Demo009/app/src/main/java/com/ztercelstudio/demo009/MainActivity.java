package com.ztercelstudio.demo009;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> mData = new ArrayList<String>();

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private Context mContext        = null;
        private List<String> mDataList  = null;

        RecyclerViewAdapter(Context context, List<String> data) {
            mContext    = context;
            mDataList   = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rvitem_layout, parent, false));
        }

        @Override
        public int getItemViewType(int position) {
            int r = super.getItemViewType(position);
            Log.d("T", "type" + r);
            return r;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d("T", ""+holder.getItemViewType());

            if ((position + 1) % 2 == 0) {
                holder.mTextView.setBackgroundColor(Color.RED);
            } else {
                holder.mTextView.setBackgroundColor(Color.BLUE);
            }
            holder.mTextView.setText(mDataList.get(position));
        }

        @Override
        public int getItemCount() {
            if (null != mDataList) {
                return mDataList.size();
            }

            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView = null;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mTextView = (TextView)itemView.findViewById(R.id.textView);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mData.add("小明");
        mData.add("小林");
        mData.add("小王");
        mData.add("小马");
        mData.add("小A");
        mData.add("小B");
        mData.add("小C");
        mData.add("小D");
        mData.add("小E");
        mData.add("小F");
        mData.add("小J");
        mData.add("小M");
        mData.add("小K");
        mData.add("小H");
        mData.add("小M");
        mData.add("小N");

        RecyclerView rvList = (RecyclerView)findViewById(R.id.rvList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setAdapter(new RecyclerViewAdapter(this, mData));
    }




}
