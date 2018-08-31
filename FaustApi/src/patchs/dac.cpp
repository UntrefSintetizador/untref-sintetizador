#include "../patch.hpp"
#include "dac.hpp"

std::string faust::Dac::GetCode()
{
    return "dac";
}

faust::Patch* faust::Dac::Create()
{
    return new Dac;
}

int faust::Dac::GetNumInputs()
{
    return channels_;
}

int faust::Dac::GetNumOutputs()
{
    return 0;
}

int faust::Dac::GetInputRate(int channel)
{
    return 0;
}
int faust::Dac::GetOutputRate(int channel)
{
    return 0;
}

void faust::Dac::ClassInit(int samplingFreq)
{
}

void faust::Dac::InstanceConstants(int samplingFreq)
{
}

void faust::Dac::InstanceResetUserInterface()
{
}

void faust::Dac::InstanceClear()
{
}

std::pair<std::string, float*>* faust::Dac::GetParameterList(int* num_parameters)
{
    *num_parameters = 0;
    std::pair<std::string, float*>* parameter_list = new std::pair<std::string, float*>[*num_parameters];
    std::pair<std::string, float*> parameter;
    return parameter_list;
}

void faust::Dac::Compute(int count)
{
    for(int i = 0; i < channels_; i++)
    {
        for(int j = 0; j < count; j++)
        {
            outputs_[i][j] += inputs_[i][j];
        }
    }
}

void faust::Dac::SetChannels(int channels, FAUSTFLOAT** outputs, FAUSTFLOAT* zero)
{
    channels_ = channels;
    inputs_ = new FAUSTFLOAT*[channels];
    outputs_ = new FAUSTFLOAT*[channels];
    for (int i = 0; i < channels; i++){
        inputs_[i] = zero;
        outputs_[i] = outputs[i];
    }
}
