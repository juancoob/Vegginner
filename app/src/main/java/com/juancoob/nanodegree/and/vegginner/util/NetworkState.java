package com.juancoob.nanodegree.and.vegginner.util;

/**
 * This class checks the network state using the paging library
 *
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class NetworkState {

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    static {
        LOADED = new NetworkState(Status.SUCCESS, Constants.SUCCESS);
        LOADING = new NetworkState(Status.RUNNING, Constants.RUNNING);
    }

    private final Status state;
    private final String msg;

    public NetworkState(Status state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public Status getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

}
