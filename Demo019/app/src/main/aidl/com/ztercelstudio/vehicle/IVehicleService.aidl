// IVehicleService.aidl
package com.ztercelstudio.vehicle;

import com.ztercelstudio.vehicle.IEventListener;

// Declare any non-default types here with import statements
interface IVehicleService {
    void registerListener(String tag, in IEventListener listener);
    void unregisterListener(String tag, in IEventListener listener);
    void sendData(in byte[] data);
}
