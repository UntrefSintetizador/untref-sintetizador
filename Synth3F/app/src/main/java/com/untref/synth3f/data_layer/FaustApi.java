package com.untref.synth3f.data_layer;

public class FaustApi {

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("faust_api");
    }

    public static native long initFaustApi(int channels, int buffer_size, int sample_rate);

    public static native void disposeFaustApi(long android_graph_adapter_pointer);

    public static native void addPatchFaustApi(long android_graph_adapter_pointer, String code, int id);

    public static native void removePatchFaustApi(long android_graph_adapter_pointer, int id);

    public static native void setValueFaustApi(long android_graph_adapter_pointer, String name, float value);

    public static native void connectFaustApi(long android_graph_adapter_pointer, int source, int outlet, int target, int inlet);

    public static native void disconnectFaustApi(long android_graph_adapter_pointer, int source, int outlet, int target, int inlet);

    public static native void processFaustApi(long android_graph_adapter_pointer, float[] jdata, int channels);

    public static native void startAudioFaustApi(long android_graph_adapter_pointer);

    public static native void stopAudioFaustApi(long android_graph_adapter_pointer);
}
