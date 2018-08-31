#ifndef FAUSTAPI_PATCHS_DAC_H_
#define FAUSTAPI_PATCHS_DAC_H_

#include "../patch.hpp"

namespace faust
{

class Dac : public Patch
{
public:

    ~Dac() {}

    static Patch* Create();

    virtual std::string GetCode();

    virtual int GetNumInputs();

    virtual int GetNumOutputs();

    virtual int GetInputRate(int channel);

    virtual int GetOutputRate(int channel);

    virtual void ClassInit(int samplingFreq);

    virtual void InstanceConstants(int samplingFreq);

    virtual void InstanceResetUserInterface();

    virtual void InstanceClear();

    virtual std::pair<std::string, float*>* GetParameterList(int* num_parameters);

    virtual void Compute(int count);

    void SetChannels(int channels, FAUSTFLOAT** outputs, FAUSTFLOAT* zero);

private:

    int channels_;

};
}

#endif // FAUSTAPI_PATCHS_DUMMY_H_
