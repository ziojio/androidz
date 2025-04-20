package com.example.demo.database;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserInfo {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String password;
    public String nickname;
    @Nullable
    public String phone;
    @Nullable
    public String email;
}
