#ifndef JNI_ANDROID_GRAPH_ADAPTER_H_
#define JNI_ANDROID_GRAPH_ADAPTER_H_

#include "../src/graph_manager.hpp"

class audio;

namespace faust
{

class AndroidGraphAdapter : public GraphManager
{
public:

    void virtual Init(int channels, int buffer_size, int sample_rate);

    void virtual Free();

    //Android
    void StartAudio();

    void StopAudio();

    //Dsp
    void init(int fSampleRate);

    int getNumInputs();

    int getNumOutputs();

    void compute(unsigned int fBufferSize, float** fInputs, float** fOutputs);

private:

    audio* timer;

};
}

#endif // JNI_ANDROID_GRAPH_ADAPTER_H_
