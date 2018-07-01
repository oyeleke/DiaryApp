package com.example.user.diaryapplication.diaryItemsList;

import com.example.user.diaryapplication.model.DiaryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DiaryItemsPresenter implements DiaryItemsContract.presenter {

    private DiaryItemsContract.view view;
    private ArrayList<DiaryModel> diaryModelArrayList;
    private DatabaseReference databaseReference;
    private ArrayList<DiaryModel>tempDairyList = new ArrayList<>();

    public DiaryItemsPresenter(DiaryItemsContract.view view, ArrayList<DiaryModel> diaryModelArrayList, DatabaseReference databaseReference) {
        this.view = view;
        this.diaryModelArrayList = diaryModelArrayList;
        this.databaseReference = databaseReference;
    }

    public void getValueFromDatabase(){
        view.showLoading();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tempDairyList.clear();

                for (DataSnapshot diarySnapshot : dataSnapshot.getChildren()){
                    DiaryModel diaryModel = diarySnapshot.getValue(DiaryModel.class);
                    tempDairyList.add(diaryModel);
                }
                setData(tempDairyList);
                view.dismissLoading();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public String getTitle(int i) {
        return diaryModelArrayList.get(i).getTitle();
    }

    @Override
    public String getCreatedAt(int i) {
        return  diaryModelArrayList.get(i).getCreatedAt();
                //DateUtils.formatDate(diaryModelArrayList.get(i).getCreatedAt(), DateUtils.FULL_DATE);
    }

    @Override
    public String getUpdatedAt(int i) {
        return diaryModelArrayList.get(i).getUpdatedAt();
    }

    @Override
    public int getTotalNoOfItems() {
        return diaryModelArrayList.size();
    }

    @Override
    public DiaryModel getItem(int i) {
        return diaryModelArrayList.get(i);
    }

    @Override
    public void getDiaryItemClicked(DiaryModel diaryModel) {
        view.getdiaryItemClicked(diaryModel);
    }

    public void setData(ArrayList<DiaryModel> diaryModels) {
        this.diaryModelArrayList = diaryModels;
        if (diaryModelArrayList != null){
            view.ondiaryItemsListSuccess();
        }
    }

}
