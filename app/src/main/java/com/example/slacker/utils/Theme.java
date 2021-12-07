package com.example.slacker.utils;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Theme {
    public int idx;

    public Theme(int idx) {
        this.idx = idx;
    }

    public Theme() {

    }

    public int getIdx() {
        return idx;
    }
}