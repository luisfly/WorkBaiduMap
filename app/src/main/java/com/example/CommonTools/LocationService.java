package com.example.CommonTools;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationService extends Service {
    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Thread thread = new Thread();
        thread.start();
        thread.interrupt();
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
