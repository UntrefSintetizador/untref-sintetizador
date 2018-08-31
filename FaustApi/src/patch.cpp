#include "patch.hpp"

void faust::Patch::Init(FAUSTFLOAT* zero, int buffer_size, int sample_rate, int id)
{
    id_ = id;
    inputs_ = new FAUSTFLOAT*[2];
    outputs_ = new FAUSTFLOAT*[2];
    for(int i = 0; i < 2; i++)
    {
        inputs_[i] = zero;
    }
    for(int i = 0; i < 2; i++)
    {
        outputs_[i] = new FAUSTFLOAT[buffer_size];
    }
    ClassInit(sample_rate);
    InstanceConstants(sample_rate);
    InstanceResetUserInterface();
    InstanceClear();
}

int faust::Patch::GetId()
{
    return id_;
}

std::string faust::Patch::GetCode()
{
    return nullptr;
}

int faust::Patch::GetPosition()
{
    return position_;
}

void faust::Patch::SetPosition(int position)
{
    position_ = position;
}

FAUSTFLOAT** faust::Patch::GetInputs()
{
    return inputs_;
}

FAUSTFLOAT** faust::Patch::GetOutputs()
{
    return outputs_;
}

float faust::Patch::FaustPower3_f(float value)
{
    return ((value * value) * value);
}

float faust::Patch::FaustPower2_f(float value)
{
    return (value * value);
}

void faust::Patch::Free()
{
    for(int i = 0; i < 2; i++)
    {
        delete[] outputs_[i];
    }
    delete inputs_;
    delete outputs_;
}

int faust::Patch::GetNumInputs()
{
    return 0;
}

int faust::Patch::GetNumOutputs()
{
    return 0;
}

int faust::Patch::GetInputRate(int channel)
{
    return 0;
}

int faust::Patch::GetOutputRate(int channel)
{
    return 0;
}

void faust::Patch::ClassInit(int samplingFreq) {}

void faust::Patch::InstanceConstants(int samplingFreq) {}

void faust::Patch::InstanceResetUserInterface() {}

void faust::Patch::InstanceClear() {}

std::pair<std::string, float*>* faust::Patch::GetParameterList(int* num_parameters)
{
    *num_parameters = 0;
    return nullptr;
}

void faust::Patch::Compute(int count) {}
