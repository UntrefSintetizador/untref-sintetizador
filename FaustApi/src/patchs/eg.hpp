#ifndef FAUSTAPI_PATCHS_EG_H_
#define FAUSTAPI_PATCHS_EG_H_

#include "../patch.hpp"

namespace faust
{

class Eg : public Patch
{
public:

    ~Eg() {}

    virtual std::string GetCode();

    static Patch* Create();

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

    FAUSTFLOAT fVslider0;
    int iVec0[4];
    float fRec0[2];
    FAUSTFLOAT fButton0;
    float fVec1[2];
    int fSamplingFreq;
    float fConst0;
    FAUSTFLOAT fVslider1;
    float fRec2[2];
    float fRec1[2];
    FAUSTFLOAT fVslider2;
    float fRec5[2];
    FAUSTFLOAT fVslider3;
    float fRec6[2];
    float fRec4[2];
    FAUSTFLOAT fVslider4;
    float fRec7[2];
    float fConst1;
    float fRec3[2];
    FAUSTFLOAT fVslider5;
    float fRec8[2];
    float fConst2;
    float fRec9[2];
    float fConst3;
    float fRec11[2];
    float fVec2[2];
    float fVec3[2];
    float fVec4[2];

};
}

#endif // FAUSTAPI_PATCHS_EG_H_
