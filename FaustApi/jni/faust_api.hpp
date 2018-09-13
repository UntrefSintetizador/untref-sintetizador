#ifndef FAUSTAPI_FAUST_API_H_
#define FAUSTAPI_FAUST_API_H_

#include <jni.h>
#include "graph_manager.hpp"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT long JNICALL java_com_untref_synth3f_data_1layer_FaustApi_InitFaustApi(JNIEnv *env, jclass cls, jint channels, jint buffer_size, jint sample_rate);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_DisposeFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_AddPatchFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jstring code, jint id);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_RemovePatchFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jint id);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_SetValueFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jstring name, jfloat value);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_ConnectFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jint source, jint outlet, jint target, jint inlet);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_DisconnectFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jint source, jint outlet, jint target, jint inlet);

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_ProcessFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jfloatArray jdata, jint channels);

#ifdef __cplusplus
}
#endif

#endif // FAUSTAPI_FAUST_API_H_
