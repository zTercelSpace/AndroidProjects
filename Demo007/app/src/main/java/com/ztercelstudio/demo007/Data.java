package com.ztercelstudio.demo007;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    private String  name;
    private int     age;

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {

        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    Data(Parcel p) {
        readFromParcel(p);
    }

    Data(String name, int age) {
        this.name   = name;
        this.age    = age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(name);
       dest.writeInt(age);
    }

    public void readFromParcel(Parcel source) {
        name   = source.readString();
        age    = source.readInt();
    }
}
