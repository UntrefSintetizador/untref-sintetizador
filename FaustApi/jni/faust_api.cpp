#include "faust_api.hpp"
#include "android_graph_adapter.hpp"

JNIEXPORT jlong JNICALL java_com_untref_synth3f_data_1layer_FaustApi_InitFaustApi(JNIEnv *env, jclass cls, jint channels, jint buffer_size, jint sample_rate)
{
    faust::AndroidGraphAdapter* android_graph_adapter = new faust::AndroidGraphAdapter();
    android_graph_adapter->Init(channels, buffer_size, sample_rate);
    jlong android_graph_adapter_pointer;
    android_graph_adapter_pointer = jlong(android_graph_adapter);
    return android_graph_adapter_pointer;
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_DisposeFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer)
{
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        android_graph_adapter->Free();
        delete android_graph_adapter;
        android_graph_adapter = NULL;
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_AddPatchFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jstring jcode, jint id)
{
    const char* code = (char *) env->GetStringUTFChars(jcode, NULL);
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        std::string _code(code);
        android_graph_adapter->AddPatch(_code, id);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_RemovePatchFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint id)
{
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        android_graph_adapter->RemovePatch(id);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_SetValueFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jstring jname, jfloat value)
{
    const char* name = (char *) env->GetStringUTFChars(jname, NULL);
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        std::string _name(name);
        android_graph_adapter->SetValue(_name, value);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_ConnectFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint source, jint outlet, jint target, jint inlet)
{
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        android_graph_adapter->Connect(source, outlet, target, inlet);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_DisconnectFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jint source, jint outlet, jint target, jint inlet)
{
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        android_graph_adapter->Disconnect(source, outlet, target, inlet);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_ProcessFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer, jfloatArray jdata, jint channels)
{
    float *data = env->GetFloatArrayElements(jdata, NULL);
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        android_graph_adapter->Process(data, channels);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_StartAudioFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer)
{
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        android_graph_adapter->StartAudio();
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_StopAudioFaustApi(JNIEnv *env, jclass cls, jlong android_graph_adapter_pointer)
{
    faust::AndroidGraphAdapter* android_graph_adapter = (faust::AndroidGraphAdapter*)android_graph_adapter_pointer;
    if(android_graph_adapter != NULL)
    {
        android_graph_adapter->StopAudio();
    }
}
