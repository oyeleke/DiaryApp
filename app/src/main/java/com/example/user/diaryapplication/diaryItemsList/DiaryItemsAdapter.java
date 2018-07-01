package com.example.user.diaryapplication.diaryItemsList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.diaryapplication.R;
import com.example.user.diaryapplication.model.DiaryModel;
import com.example.user.diaryapplication.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;



public class DiaryItemsAdapter extends RecyclerView.Adapter<DiaryItemsAdapter.ViewHolder> {

    private DiaryItemsContract.presenter presenter;
    private Context context;

    public DiaryItemsAdapter(DiaryItemsContract.presenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.individual_diary_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setTitle(presenter.getTitle(position));
        holder.setCreatedAt(presenter.getCreatedAt(position));
        holder.setUpdatedAt(presenter.getUpdatedAt(position));
        holder.onItemClicked(presenter.getItem(position));
    }

    @Override
    public int getItemCount() {
        return presenter.getTotalNoOfItems();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements DiaryItemsContract.adapterView{
        @BindView(R.id.diary_item_title)
        TextView diaryItemTextView;
        @BindView(R.id.created_date)
        TextView dateCreatedTextView;
        @BindView(R.id.edited_date)
        TextView updatedDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void setTitle(String title) {
            diaryItemTextView.setText(title);
        }

        @Override
        public void setCreatedAt(String createdAt) {
            dateCreatedTextView.setText(Constants.CREATED_AT_PREFFIX+createdAt);
        }

        @Override
        public void setUpdatedAt(String updatedAt) {
            updatedDateTextView.setText(Constants.UPDATED_AT_PREFFIX+updatedAt);
        }

        @Override
        public void onItemClicked(final DiaryModel diaryModel) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.getDiaryItemClicked(diaryModel);
                }
            });
        }
    }
}
