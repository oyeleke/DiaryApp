package com.example.user.diaryapplication.model;

import java.util.Date;

/**
 * Created by timi on 28/06/2018.
 */

public class DiaryModel {

    private String diaryId;
    private String userId;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;

    public DiaryModel(){}


    public DiaryModel(String diaryId, String userId, String title, String content, String createdAt, String updatedAt) {
        this.diaryId = diaryId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(String diaryId) {
        this.diaryId = diaryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
