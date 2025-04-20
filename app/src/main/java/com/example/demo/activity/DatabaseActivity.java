package com.example.demo.activity;

import android.os.Bundle;

import com.example.demo.UIApp;
import com.example.demo.database.UserInfo;
import com.example.demo.database.UserInfoDao;
import com.example.demo.databinding.ActivityDatabaseBinding;
import com.example.demo.util.AsyncTask;
import com.example.demo.util.Timber;
import com.google.gson.Gson;

import java.util.UUID;
import java.util.stream.Collectors;


public class DatabaseActivity extends BaseActivity {
    private ActivityDatabaseBinding binding;
    private UserInfoDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        userDao = UIApp.getDB().userDao();
        binding.insert.setOnClickListener(v -> {
            AsyncTask.doAction(() -> {
                UserInfo u = new UserInfo();
                u.username = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
                u.password = "123456";
                u.nickname = "user-" + (userDao.count() + 1);
                Gson gson = new Gson();
                Timber.d("insert " + gson.toJson(u));
                userDao.insert(u);
            });
        });
        binding.query.setOnClickListener(v -> {
            AsyncTask.doAction(() -> userDao.getAll(), list -> {
                Timber.d("query " + list.size());
                Gson gson = new Gson();
                String json = list.stream().map(gson::toJson).collect(Collectors.joining("\n\n"));
                // Timber.d("list " + gson.toJson(list));
                showMessage(json);
            });
        });
        binding.clear.setOnClickListener(v -> {
            AsyncTask.doAction(() -> {
                Timber.d("clear ");
                userDao.deleteAll();
            });
        });
        binding.resetPrimaryKey.setOnClickListener(v -> {
            AsyncTask.doAction(() -> {
                Timber.d("reset PrimaryKey ");
                userDao.resetPrimaryKey();
            });
        });
    }

    private void showMessage(String msg) {
        binding.textShow.setText(msg);
    }

}