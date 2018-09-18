#ifndef JNI_FAUST_API_H_
#define JNI_FAUST_API_H_

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_initFaustApi(JNIEnv* env, jclass cls, jint channels, jint buffer_size, jint sample_rate);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_disposeFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_addPatchFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jstring code, jint id);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_removePatchFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint id);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_setValueFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jstring name, jfloat value);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_connectFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint source, jint outlet, jint target, jint inlet);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_disconnectFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint source, jint outlet, jint target, jint inlet);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_processFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jfloatArray jdata, jint channels);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_startAudioFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer);

JNIEXPORT void JNICALL Java_com_untref_synth3f_data_1layer_FaustApi_stopAudioFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer);

#ifdef __cplusplus
}
#endif

#endif // JNI_FAUST_API_H_
