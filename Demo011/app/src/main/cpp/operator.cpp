#include <jni.h>
#include <string>
#include "TStudent.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_ztercelstudio_demo011_MainActivity_stringFromJNI(JNIEnv* env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL
Java_com_ztercelstudio_demo011_MainActivity_add(JNIEnv* env, jobject obj, jint left, jint right) {
    return (left + right);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_ztercelstudio_demo011_MainActivity_sub(JNIEnv* env, jobject obj, jint left, jint right) {
    return (left - right);
}

extern "C" JNIEXPORT void JNICALL
Java_com_ztercelstudio_demo011_MainActivity_setName(JNIEnv* env, jobject obj, jstring name) {
    jboolean isCopy;
    TStudent::getInstance()->setName( env->GetStringUTFChars(name, &isCopy));
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ztercelstudio_demo011_MainActivity_getName(JNIEnv* env, jobject obj) {
    std::string name = TStudent::getInstance()->getName();
    return env->NewStringUTF(name.c_str());
}


extern "C" JNIEXPORT void JNICALL
Java_com_ztercelstudio_demo011_MainActivity_setAge(JNIEnv* env, jobject obj, jint age) {
    TStudent::getInstance()->setAge(age);
}

extern "C" JNIEXPORT int JNICALL
Java_com_ztercelstudio_demo011_MainActivity_getAge(JNIEnv* env, jobject obj) {
    return TStudent::getInstance()->getAge();
}
