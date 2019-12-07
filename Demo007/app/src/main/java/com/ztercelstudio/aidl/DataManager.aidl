// DataManager.aidl
package com.ztercelstudio.aidl;

import com.ztercelstudio.aidl.Data;
// Declare any non-default types here with import statements

interface DataManager {
    void setData(String name, int age);
    List<Data>  getData();
}
