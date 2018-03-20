package com.proto.oauth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Dennis on 20/03/2018.
 */

public class ProtoAuthenticatorService extends Service {

    public IBinder onBind(Intent intent){
        ProtoAuthenticator authenticator = new ProtoAuthenticator(this);
        return authenticator.getIBinder();
    }
}