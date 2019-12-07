// IDataManager.aidl
package com.ztercelstudio.aidl;

// Declare any non-default types here with import statements

interface IDataManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void Notify(String command);
    String getCommand();
}
