package com.atech.foodapp.util;

/**
 * A generic class that holds a value with its loading status.
 * @param <T> Type of the resource
 * @author Aiyu
 */
public class DataState <T>{
    private DataState.Status status;
    private T data;
    private String message;

    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    public DataState(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> DataState<T> success(T data) {
        return new DataState<>(Status.SUCCESS, data, null);
    }

    public static <T> DataState<T> error(String msg, T data) {
        return new DataState<>(Status.ERROR, data, msg);
    }

    public static <T> DataState<T> loading(T data) {
        return new DataState<>(Status.LOADING, data, null);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
