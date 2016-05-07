package com.candlelight.fragments;

import com.rancard.kudi.client.async.Kudi;

/**
 * Created by rancard on 8/13/15.
 */
public interface  MainFragment {
    Kudi kudiInstance = Kudi.newInstance("http://192.168.43.247:8080/wallet/api/v1");
    Kudi.Session session = kudiInstance.getSession("877");
}
