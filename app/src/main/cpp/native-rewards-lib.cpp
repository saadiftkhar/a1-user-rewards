#include <jni.h>
#include <string>


const char *GOOGLE_AD_ID_1 = "ae362c30-cd82-4750-b129-1f6d1799b785";
const char *GOOGLE_AD_ID_2 = "a2648bfc-bbe5-44a9-bd7e-7acc7f7c4d71";
const char *GOOGLE_AD_ID_3 = "9bf12a66-1ab8-4823-a7c5-9976ef832ea7";

const char *REWARDS_BASE_URL = "http://softekapps.xyz/A1_rewards/";
const char *FCM_BASE_URL = "https://fcm.googleapis.com/";


extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_freespinslink_user_security_CLibController_getGoogleAdIds(JNIEnv *env, jobject jObj) {
    jobjectArray result;
    int i;
    const char *data[3] = {GOOGLE_AD_ID_1, GOOGLE_AD_ID_2, GOOGLE_AD_ID_3};
    result = (jobjectArray) env->NewObjectArray(
            3,
            env->FindClass("java/lang/String"),
            env->NewStringUTF(""));
    for (i = 0; i < 3; i++) env->SetObjectArrayElement(result, i, env->NewStringUTF(data[i]));

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