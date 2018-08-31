#ifndef FAUSTAPI_PATCHS_VCO_H_
#define FAUSTAPI_PATCHS_VCO_H_

#include "../patch.hpp"

namespace faust
{

class Vco : public Patch
{
public:

    ~Vco() {}

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

private:

    int fSamplingFreq;
    float fConst0;
    float fConst1;
    FAUSTFLOAT fCheckbox0;
    int iVec0[4];
    float fRec0[2];
    FAUSTFLOAT fHslider0;
    float fRec1[2];
    FAUSTFLOAT fHslider1;
    float fRec2[2];
    float fConst2;
    float fConst3;
    float fConst4;
    float fRec4[2];
    float fVec1[2];
    float fVec2[2];
    float fVec3[2];
    float fVec4[2];
    int IOTA;
    float fVec5[4096];
    float fRec3[2];
    float fConst5;
    float fVec6[4096];
    float fRec5[2];
};
}

#endif // FAUSTAPI_PATCHS_VCO_H_
