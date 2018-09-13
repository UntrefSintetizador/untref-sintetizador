#ifndef JNI_FAUST_API_H_
#define JNI_FAUST_API_H_

#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL java_com_untref_synth3f_data_1layer_FaustApi_InitFaustApi(JNIEnv *env, jclass cls, jint channels, jint buffer_size, jint sample_rate);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_DisposeFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_AddPatchFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jstring code, jint id);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_RemovePatchFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint id);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_SetValueFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jstring name, jfloat value);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_ConnectFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint source, jint outlet, jint target, jint inlet);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_DisconnectFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint source, jint outlet, jint target, jint inlet);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_ProcessFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jfloatArray jdata, jint channels);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_StartAudioFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_StopAudioFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer);

#ifdef __cplusplus
}
#endif

#endif // JNI_FAUST_API_H_
