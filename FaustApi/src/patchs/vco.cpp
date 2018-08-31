#include "../patch.hpp"
#include "vco.hpp"

std::string faust::Vco::GetCode()
{
    return "vco";
}

faust::Patch* faust::Vco::Create()
{
    return new Vco;
}

int faust::Vco::GetNumInputs()
{
    return 0;
}

int faust::Vco::GetNumOutputs()
{
    return 1;
}

int faust::Vco::GetInputRate(int channel)
{
    int rate;
    switch (channel)
    {
    default:
    {
        rate = -1;
        break;
    }

    }
    return rate;

}
int faust::Vco::GetOutputRate(int channel)
{
    int rate;
    switch (channel)
    {
    case 0:
    {
        rate = 1;
        break;
    }
    default:
    {
        rate = -1;
        break;
    }

    }
    return rate;

}

void faust::Vco::ClassInit(int samplingFreq)
{

}

void faust::Vco::InstanceConstants(int samplingFreq)
{
    fSamplingFreq = samplingFreq;
    fConst0 = std::min(192000.0f, std::max(1.0f, float(fSamplingFreq)));
    fConst1 = (4.0f / fConst0);
    fConst2 = (0.00520833349f * FaustPower3_f(fConst0));
    fConst3 = (0.5f * fConst0);
    fConst4 = (1.0f / fConst0);
    fConst5 = (0.25f * fConst0);

}

void faust::Vco::InstanceResetUserInterface()
{
    fCheckbox0 = FAUSTFLOAT(0.0f);
    fHslider0 = FAUSTFLOAT(0.5f);
    fHslider1 = FAUSTFLOAT(440.0f);
}

void faust::Vco::InstanceClear()
{
    for (int l0 = 0; (l0 < 4); l0 = (l0 + 1))
    {
        iVec0[l0] = 0;

    }
    for (int l1 = 0; (l1 < 2); l1 = (l1 + 1))
    {
        fRec0[l1] = 0.0f;

    }
    for (int l2 = 0; (l2 < 2); l2 = (l2 + 1))
    {
        fRec1[l2] = 0.0f;

    }
    for (int l3 = 0; (l3 < 2); l3 = (l3 + 1))
    {
        fRec2[l3] = 0.0f;

    }
    for (int l4 = 0; (l4 < 2); l4 = (l4 + 1))
    {
        fRec4[l4] = 0.0f;

    }
    for (int l5 = 0; (l5 < 2); l5 = (l5 + 1))
    {
        fVec1[l5] = 0.0f;

    }
    for (int l6 = 0; (l6 < 2); l6 = (l6 + 1))
    {
        fVec2[l6] = 0.0f;

    }
    for (int l7 = 0; (l7 < 2); l7 = (l7 + 1))
    {
        fVec3[l7] = 0.0f;

    }
    for (int l8 = 0; (l8 < 2); l8 = (l8 + 1))
    {
        fVec4[l8] = 0.0f;

    }
    IOTA = 0;
    for (int l9 = 0; (l9 < 4096); l9 = (l9 + 1))
    {
        fVec5[l9] = 0.0f;

    }
    for (int l10 = 0; (l10 < 2); l10 = (l10 + 1))
    {
        fRec3[l10] = 0.0f;

    }
    for (int l11 = 0; (l11 < 4096); l11 = (l11 + 1))
    {
        fVec6[l11] = 0.0f;

    }
    for (int l12 = 0; (l12 < 2); l12 = (l12 + 1))
    {
        fRec5[l12] = 0.0f;

    }

}

std::pair<std::string, float*>* faust::Vco::GetParameterList(int* num_parameters)
{
    *num_parameters = 3;
    std::pair<std::string, float*>* parameter_list = new std::pair<std::string, float*>[*num_parameters];
    std::pair<std::string, float*> parameter;
    parameter.first = "freq";
    parameter.second = &fHslider1;
    parameter_list[0] = parameter;
    parameter.first = "gain";
    parameter.second = &fHslider0;
    parameter_list[1] = parameter;
    parameter.first = "gate";
    parameter.second = &fCheckbox0;
    parameter_list[2] = parameter;
    return parameter_list;
}

void faust::Vco::Compute(int count)
{
    FAUSTFLOAT* output0 = outputs_[0];
    float fSlow0 = (0.00100000005f * float(fCheckbox0));
    float fSlow1 = (0.00100000005f * float(fHslider0));
    float fSlow2 = (0.00100000005f * float(fHslider1));
    for (int i = 0; (i < count); i = (i + 1))
    {
        iVec0[0] = 1;
        fRec0[0] = (fSlow0 + (0.999000013f * fRec0[1]));
        fRec1[0] = (fSlow1 + (0.999000013f * fRec1[1]));
        fRec2[0] = (fSlow2 + (0.999000013f * fRec2[1]));
        float fTemp0 = std::max(fRec2[0], 23.4489498f);
        float fTemp1 = std::max(0.0f, std::min(2047.0f, (fConst3 / fTemp0)));
        int iTemp2 = int(fTemp1);
        float fTemp3 = (fTemp1 - float(iTemp2));
        float fTemp4 = std::max(20.0f, std::fabs(fTemp0));
        float fTemp5 = (fRec4[1] + (fConst4 * fTemp4));
        fRec4[0] = (fTemp5 - std::floor(fTemp5));
        float fTemp6 = FaustPower2_f(((2.0f * fRec4[0]) + -1.0f));
        fVec1[0] = fTemp6;
        float fTemp7 = (fTemp6 * (fTemp6 + -2.0f));
        fVec2[0] = fTemp7;
        float fTemp8 = ((fTemp7 - fVec2[1]) / fTemp4);
        fVec3[0] = fTemp8;
        float fTemp9 = ((fTemp8 - fVec3[1]) / fTemp4);
        fVec4[0] = fTemp9;
        float fTemp10 = (((fTemp9 - fVec4[1]) * float(iVec0[3])) / fTemp4);
        fVec5[(IOTA & 4095)] = fTemp10;
        int iTemp11 = (iTemp2 + 1);
        float fTemp12 = (float(iTemp2) + (1.0f - fTemp1));
        fRec3[0] = ((0.999000013f * fRec3[1]) - (fConst2 * (((fTemp3 * fVec5[((IOTA - iTemp11) & 4095)]) + (fTemp12 * fVec5[((IOTA - iTemp2) & 4095)])) - fTemp10)));
        float fTemp13 = (((fTemp6 - fVec1[1]) * float(iVec0[1])) / fTemp4);
        fVec6[(IOTA & 4095)] = fTemp13;
        fRec5[0] = ((0.999000013f * fRec5[1]) - (fConst5 * (((fTemp3 * fVec6[((IOTA - iTemp11) & 4095)]) + (fTemp12 * fVec6[((IOTA - iTemp2) & 4095)])) - fTemp13)));
        output0[i] = FAUSTFLOAT((fConst1 * (((fRec0[0] * fRec1[0]) * fRec2[0]) * ((float((500.0f < fRec2[0])) * fRec3[0]) + (float((500.0f > fRec2[0])) * fRec5[0])))));
        for (int j0 = 3; (j0 > 0); j0 = (j0 - 1))
        {
            iVec0[j0] = iVec0[(j0 - 1)];

        }
        fRec0[1] = fRec0[0];
        fRec1[1] = fRec1[0];
        fRec2[1] = fRec2[0];
        fRec4[1] = fRec4[0];
        fVec1[1] = fVec1[0];
        fVec2[1] = fVec2[0];
        fVec3[1] = fVec3[0];
        fVec4[1] = fVec4[0];
        IOTA = (IOTA + 1);
        fRec3[1] = fRec3[0];
        fRec5[1] = fRec5[0];
    }
}
