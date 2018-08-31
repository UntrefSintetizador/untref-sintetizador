//Reemplazar dummy por el nombre del patch, respetando el uso de mayusculas y minusculas
//Agregar a patch_factory.cpp :
//#include "patchs/dummy.hpp"
//y la linea patch_creator_functions_["dummy"] = &faust::Dummy::Create;
//en la funcion void faust::PatchFactory::Init()
#ifndef FAUSTAPI_PATCHS_DUMMY_H_
#define FAUSTAPI_PATCHS_DUMMY_H_

#include "../patch.hpp"

namespace faust
{

class Dummy : public Patch
{
public:

    ~Dummy() {}

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

//COPIAR ATRIBUTOS

};
}

#endif // FAUSTAPI_PATCHS_DUMMY_H_
