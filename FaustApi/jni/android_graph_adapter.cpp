#include "android_graph_adapter.hpp"
#include "DspFaust.hpp"

//GraphManager
void faust::AndroidGraphAdapter::Init(int channels, int buffer_size, int sample_rate)
{
    this->GraphManager::Init(channels, buffer_size, sample_rate);
    timer = audio::InitAndroidAudio(sample_rate, buffer_size);
    timer->init("", this);
}

void faust::AndroidGraphAdapter::Free()
{
    this->GraphManager::Free();
    timer->stop();
    delete timer;
}

//Android
void faust::AndroidGraphAdapter::StartAudio()
{
    timer->start();
}

void faust::AndroidGraphAdapter::StopAudio()
{
    timer->stop();
}

//Dsp
void faust::AndroidGraphAdapter::init(int fSampleRate)
{
}

int faust::AndroidGraphAdapter::getNumInputs()
{
    return 0;
}

int faust::AndroidGraphAdapter::getNumOutputs()
{
    return 2;
};

void faust::AndroidGraphAdapter::compute(unsigned int fBufferSize, float** fInputs, float** fOutputs)
{
    for(int i = 0; i < channels_; i++)
    {
        for(int j = 0; j < fBufferSize; j++)
        {
            output_[i][j] = 0;
        }
    }
    faust::Patch* patch;
    for(int i = 0; i < num_patches_; i++)
    {
        patch = patches_[i];
        patch->Compute(fBufferSize);
    }
    for(int i = 0; i < channels_; i++)
    {
        for(int j = 0; j < fBufferSize; j++)
        {
            fOutputs[i][j] = output_[i][j];
        }
    }
}
