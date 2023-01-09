package com.gnrc.telehealth.Network;

public interface NetworkListener {
    void onNetworkDisconnected();

    void onNetworkConnected(String type);
}
