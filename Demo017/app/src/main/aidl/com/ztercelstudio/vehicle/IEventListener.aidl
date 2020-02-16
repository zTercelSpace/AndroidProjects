// IEventListener.aidl
package com.ztercelstudio.vehicle;

// Declare any non-default types here with import statements

interface IEventListener {
    void handle(int event, in byte[] data);
}
