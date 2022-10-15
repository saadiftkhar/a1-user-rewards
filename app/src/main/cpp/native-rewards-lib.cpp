#include <jni.h>
#include <string>


const char *MY_GOOGLE_AD_ID = "ae362c30-cd82-4750-b129-1f6d1799b785";
const char *CLIENT_GOOGLE_AD_ID = "9bf12a66-1ab8-4823-a7c5-9976ef832ea7";

const char *REWARDS_BASE_URL = "http://softekapps.xyz/A1_rewards/";
const char *FCM_BASE_URL = "https://fcm.googleapis.com/";


extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_freespinslink_user_security_CLibController_getGoogleAdIds(JNIEnv *env, jobject jObj) {
    jobjectArray result;
    int i;
    const char *data[2] = {MY_GOOGLE_AD_ID, CLIENT_GOOGLE_AD_ID};
    result = (jobjectArray) env->NewObjectArray(
            2,
            env->FindClass("java/lang/String"),
            env->NewStringUTF(""));
    for (i = 0; i < 2; i++) env->SetObjectArrayElement(result, i, env->NewStringUTF(data[i]));

    return result;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_freespinslink_user_security_CLibController_getRewardsBaseUrl(JNIEnv *env, jobject jObj) {
    return env->NewStringUTF(REWARDS_BASE_URL);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_freespinslink_user_security_CLibController_getFcmBaseUrl(JNIEnv *env, jobject jObj) {
    return env->NewStringUTF(FCM_BASE_URL);
}