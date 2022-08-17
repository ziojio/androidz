package com.example.demo.data.response;

public abstract class DataResultObserver<T> extends BaseObserver<DataResult<T>> {

    @Override
    public void onNext(DataResult<T> response) {
        if (response.code == 0) {
            onSuccess(response.data);
        } else {
            onError(response.code, response.message);
        }
    }

    public abstract void onSuccess(T t);

}