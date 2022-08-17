package com.example.demo.ui.activity;

import android.os.Bundle;

import com.example.demo.UIApp;
import com.example.demo.database.room.entity.UserInfo;
import com.example.demo.database.room.entity.UserInfoDao;
import com.example.demo.databinding.ActivityDatabaseBinding;
import com.example.demo.ui.base.BaseActivity;
import com.example.demo.util.AsyncTask;

import timber.log.Timber;

public class DataBaseActivity extends BaseActivity {
    private ActivityDatabaseBinding binding;
    private UserInfoDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("DataBase");
        setSupportActionBar(binding.toolbar);

        userDao = UIApp.getDB().userDao();

        binding.clear.setOnClickListener(v -> {
            Timber.d("clear ");
            userDao.deleteAll();
        });
        binding.insert.setOnClickListener(v -> {
            Timber.d("insert ");
            AsyncTask.doAction(() -> {
                UserInfo u = new UserInfo();
                u.setNickname("user-" + (1 + userDao.getAll().size()));
                userDao.insert(u);
            });
        });
        binding.query.setOnClickListener(v -> {
            Timber.d("query ");
            AsyncTask.doAction(() -> userDao.getAll(), list -> showMessage(list.toString()));
        });
    }

    private void showMessage(String msg) {
        binding.textShow.setText(msg);
    }

}