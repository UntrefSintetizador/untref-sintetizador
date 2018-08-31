//Reemplazar dummy por el nombre del patch, respetando el uso de mayusculas y minusculas

#include "../patch.hpp"
#include "dummy.hpp"

std::string faust::Dummy::GetCode()
{
    return "dummy";
}

faust::Patch* faust::Dummy::Create()
{
    return new Dummy;
}

int faust::Dummy::GetNumInputs()
{
    //Copiar GetNumInputs
    return 0;
}

int faust::Dummy::GetNumOutputs()
{
    //Copiar GetNumOutputs
    return 0;
}

int faust::Dummy::GetInputRate(int channel)
{
    //Copiar GetInputRate
    return 0;
}
int faust::Dummy::GetOutputRate(int channel)
{
    //Copiar GetOutputRate
    return 0;
}

void faust::Dummy::ClassInit(int samplingFreq)
{
    //Copiar ClassInit
}

void faust::Dummy::InstanceConstants(int samplingFreq)
{
    //Copiar InstanceConstants
}

void faust::Dummy::InstanceResetUserInterface()
{
    //Copiar InstanceResetUserInterface
}

void faust::Dummy::InstanceClear()
{
    //Copiar InstanceClear
}

std::pair<std::string, float*>* faust::Dummy::GetParameterList(int* num_parameters)
{
    *num_parameters = 0; //Indicar cantidad de parametros
    std::pair<std::string, float*>* parameter_list = new std::pair<std::string, float*>[*num_parameters];
    std::pair<std::string, float*> parameter;
    //Incluir los parametros de buildUserInterface de la siguiente forma:
    //parameter.first = "freq";
    //parameter.second = &fHslider1;
    //parameter_list[0] = parameter;
    //parameter.first = "gain";
    //parameter.second = &fHslider0;
    //parameter_list[1] = parameter;
    //parameter.first = "gate";
    //parameter.second = &fCheckbox0;
    //parameter_list[2] = parameter;
    return parameter_list;
}

void faust::Dummy::Compute(int count)
{
    //Copiar Compute
    //Asignar los inputs y outputs desde inputs_ y outputs_ al inicio de la funcion
    //EJ: FAUSTFLOAT* output0 = outputs_[0];
    //Reemplazar mydsp_faustpowerX_f por FaustPowerX_f
}
