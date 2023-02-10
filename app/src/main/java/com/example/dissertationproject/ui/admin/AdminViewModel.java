package com.example.dissertationproject.ui.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdminViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AdminViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Admin Panel");
    }

    public LiveData<String> getText() {
        return mText;
    }
}