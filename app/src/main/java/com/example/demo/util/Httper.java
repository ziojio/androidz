package com.example.demo.util;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class Httper {
    private final OkHttpClient client;

    private Httper(Builder builder) {
        this.client = builder.client;
    }

    public void cancel(String tag) {
        Dispatcher dispatcher = client.dispatcher();
        List<Call> queuedCalls = dispatcher.queuedCalls();
        List<Call> runningCalls = dispatcher.runningCalls();
        for (Call call : queuedCalls) {
            String str = (String) call.request().tag();
            if (Objects.equals(tag, str)) {
                call.cancel();
            }
        }
        for (Call call : runningCalls) {
            String str = (String) call.request().tag();
            if (Objects.equals(tag, str)) {
                call.cancel();
            }
        }
    }

    public static final class Builder {
        private OkHttpClient client;

        public Builder() {
        }

        private Builder(Httper httper) {
            client = httper.client;
        }

        public Builder setClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder setBaseUrl(String url) {
            client.newBuilder().connectTimeout(10, TimeUnit.SECONDS);
            return this;
        }

        public Httper build() {
            if (client == null) {
                client = new OkHttpClient();
            }
            return new Httper(this);
        }
    }

    public Builder newBuilder() {
        return new Builder(this);
    }
}
