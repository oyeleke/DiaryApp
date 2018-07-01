package com.example.user.diaryapplication.diaryItemsList;


import com.example.user.diaryapplication.model.DiaryModel;

public interface DiaryItemsContract {

    interface view {

        void ondiaryItemsListSuccess();

        void getdiaryItemClicked(DiaryModel diaryModel);

        void showLoading();

        void dismissLoading();

    }

    interface presenter {

        String getTitle(int i);

        String getCreatedAt(int i);

        String getUpdatedAt(int i);

        int getTotalNoOfItems();

        DiaryModel getItem(int i);

        void getDiaryItemClicked(DiaryModel diaryModel);

        void getValueFromDatabase();

    }

    interface adapterView {
        void setTitle(String title);

        void setCreatedAt(String createdAt);

        void setUpdatedAt(String updatedAt);

        void onItemClicked(DiaryModel diaryModel);

    }
}
