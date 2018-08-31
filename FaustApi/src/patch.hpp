#ifndef FAUSTAPI_PATCH_H_
#define FAUSTAPI_PATCH_H_

#include <algorithm>
#include <cmath>
#include <utility>
#include <string>

#ifndef FAUSTFLOAT
#define FAUSTFLOAT float
#endif

namespace faust
{

class Patch
{
public:

    virtual ~Patch() {};

    void Init(FAUSTFLOAT* zero, int bufferSize, int sample_rate, int id);

    int GetId();

    virtual std::string GetCode();

    int GetPosition();

    void SetPosition(int position);

    FAUSTFLOAT** GetInputs();

    FAUSTFLOAT** GetOutputs();

    static float FaustPower3_f(float value);

    static float FaustPower2_f(float value);

    void Free();

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

protected:

    FAUSTFLOAT** inputs_;
    FAUSTFLOAT** outputs_;

private :

    int id_;
    int position_;
};
}

#endif // FAUSTAPI_PATCH_H_
