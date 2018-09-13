#include "faust_api.hpp"

JNIEXPORT jlong JNICALL java_com_untref_synth3f_data_1layer_FaustApi_InitFaustApi(JNIEnv *env, jclass cls, jint channels, jint buffer_size, jint sample_rate)
{
    faust::GraphManager* graph_manager = new faust::GraphManager();
    graph_manager->Init(channels, buffer_size, sample_rate);
    return graph_manager;
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_DisposeFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer)
{
    faust::GraphManager* = graph_manager_pointer;
    if(graph_manager != NULL)
    {
        graph_manager->Free();
        delete graph_manager;
        graph_manager = NULL;
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_AddPatchFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jstirng jcode, jint id)
{
    const char* code = (char *) (*env)->GetStringUTFChars(env, jcode, NULL);
    faust::GraphManager* = graph_manager_pointer;
    if(graph_manager != NULL)
    {
        std::string _code(code);
        graph_manager->AddPatch(_code, id);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_RemovePatchFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jint id)
{
    faust::GraphManager* = graph_manager_pointer;
    if(graph_manager != NULL)
    {
        graph_manager->RemovePatch(id);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_SetValueFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jstring jname, jfloat value)
{
    const char* name = (char *) (*env)->GetStringUTFChars(env, jname, NULL);
    faust::GraphManager* = graph_manager_pointer;
    if(graph_manager != NULL)
    {
        std::string _name(name);
        graph_manager->SetValue(_name, value);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_ConnectFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jint source, jint outlet, jint target, jint inlet)
{
    faust::GraphManager* = graph_manager_pointer;
    if(graph_manager != NULL)
    {
        graph_manager->Connect(source, outlet, target, inlet);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_DisconnectFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jint source, jint outlet, jint target, jint inlet)
{
    faust::GraphManager* = graph_manager_pointer;
    if(graph_manager != NULL)
    {
        graph_manager->Disconnect(source, outlet, target, inlet);
    }
}

JNIEXPORT void JNICALL java_com_untref_synth3f_data_1layer_FaustApi_ProcessFaustApi(JNIEnv *env, jclass cls, jlong graph_manager_pointer, jfloatArray jdata, jint channels)
{
    float *data = (*env)->GetFloatArrayElements(env, jdata, NULL);
    faust::GraphManager* = graph_manager_pointer;
    if(graph_manager != NULL)
    {
        graph_manager->Process(data, channels);
    }
}
