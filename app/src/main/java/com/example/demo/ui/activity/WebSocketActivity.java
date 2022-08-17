package com.example.demo.ui.activity;

import android.os.Bundle;

import com.example.demo.databinding.ActivityWebsocketBinding;
import com.example.demo.ui.base.BaseActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import timber.log.Timber;

public class WebSocketActivity extends BaseActivity {

    private ActivityWebsocketBinding binding;

    private WebSocket webSocket;

    private WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Timber.d("onOpen: " + response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Timber.d("onMessage: " + text);
            runOnUiThread(() -> {
                binding.text.setText(text);
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Timber.d("onMessage: " + bytes.utf8());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Timber.d("onClosing: code=%d, reason=%s", code, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Timber.d("onClosed: code=%d, reason=%s", code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Timber.d(t, "onFailure: response: " + response);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebsocketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.connect.setOnClickListener(v -> connect());
        binding.close.setOnClickListener(v -> close());
        binding.send.setOnClickListener(v -> send(binding.message.getText().toString().trim()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(-1, "");
        }
    }

    private void connect() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url("ws://echo.websocket.org")
                .build();
        webSocket = client.newWebSocket(request, webSocketListener);
    }

    private void close() {
        if (webSocket != null) {
            webSocket.close(-1, "");
        }
    }

    private void send(String text) {
        if (webSocket != null) {
            webSocket.send(text);
        }
    }

}
