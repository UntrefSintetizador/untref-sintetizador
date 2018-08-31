#include "../patch.hpp"
#include "eg.hpp"

std::string faust::Eg::GetCode()
{
    return "eg";
}

faust::Patch* faust::Eg::Create()
{
    return new Eg;
}

int faust::Eg::GetNumInputs()
{
    return 0;
}

int faust::Eg::GetNumOutputs()
{
    return 1;
}

int faust::Eg::GetInputRate(int channel)
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
int faust::Eg::GetOutputRate(int channel)
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

void faust::Eg::ClassInit(int samplingFreq)
{

}

void faust::Eg::InstanceConstants(int samplingFreq)
{
    fSamplingFreq = samplingFreq;
    fConst0 = std::min(192000.0f, std::max(1.0f, float(fSamplingFreq)));
    fConst1 = (1.0f / fConst0);
    fConst2 = (1.0f / fConst0);
    fConst3 = (0.00520833349f * FaustPower3_f(fConst0));
}

void faust::Eg::InstanceResetUserInterface()
{
    fVslider0 = FAUSTFLOAT(0.5f);
    fButton0 = FAUSTFLOAT(0.0f);
    fVslider1 = FAUSTFLOAT(0.080000000000000002f);
    fVslider2 = FAUSTFLOAT(0.10000000000000001f);
    fVslider3 = FAUSTFLOAT(0.0f);
    fVslider4 = FAUSTFLOAT(0.070000000000000007f);
    fVslider5 = FAUSTFLOAT(440.0f);
}

void faust::Eg::InstanceClear()
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
        fVec1[l2] = 0.0f;

    }
    for (int l3 = 0; (l3 < 2); l3 = (l3 + 1))
    {
        fRec2[l3] = 0.0f;

    }
    for (int l4 = 0; (l4 < 2); l4 = (l4 + 1))
    {
        fRec1[l4] = 0.0f;

    }
    for (int l5 = 0; (l5 < 2); l5 = (l5 + 1))
    {
        fRec5[l5] = 0.0f;

    }
    for (int l6 = 0; (l6 < 2); l6 = (l6 + 1))
    {
        fRec6[l6] = 0.0f;

    }
    for (int l7 = 0; (l7 < 2); l7 = (l7 + 1))
    {
        fRec4[l7] = 0.0f;

    }
    for (int l8 = 0; (l8 < 2); l8 = (l8 + 1))
    {
        fRec7[l8] = 0.0f;

    }
    for (int l9 = 0; (l9 < 2); l9 = (l9 + 1))
    {
        fRec3[l9] = 0.0f;

    }
    for (int l10 = 0; (l10 < 2); l10 = (l10 + 1))
    {
        fRec8[l10] = 0.0f;

    }
    for (int l11 = 0; (l11 < 2); l11 = (l11 + 1))
    {
        fRec9[l11] = 0.0f;

    }
    for (int l12 = 0; (l12 < 2); l12 = (l12 + 1))
    {
        fRec11[l12] = 0.0f;

    }
    for (int l13 = 0; (l13 < 2); l13 = (l13 + 1))
    {
        fVec2[l13] = 0.0f;

    }
    for (int l14 = 0; (l14 < 2); l14 = (l14 + 1))
    {
        fVec3[l14] = 0.0f;

    }
    for (int l15 = 0; (l15 < 2); l15 = (l15 + 1))
    {
        fVec4[l15] = 0.0f;

    }
}

std::pair<std::string, float*>* faust::Eg::GetParameterList(int* num_parameters)
{
    *num_parameters = 7;
    std::pair<std::string, float*>* parameter_list = new std::pair<std::string, float*>[*num_parameters];
    std::pair<std::string, float*> parameter;
    parameter.first = "Attack";
    parameter.second = &fVslider3;
    parameter_list[0] = parameter;
    parameter.first = "Decay";
    parameter.second = &fVslider2;
    parameter_list[1] = parameter;
    parameter.first = "Release";
    parameter.second = &fVslider1;
    parameter_list[2] = parameter;
    parameter.first = "Sustain";
    parameter.second = &fVslider4;
    parameter_list[3] = parameter;
    parameter.first = "freq";
    parameter.second = &fVslider5;
    parameter_list[4] = parameter;
    parameter.first = "gain";
    parameter.second = &fVslider0;
    parameter_list[5] = parameter;
    parameter.first = "gate";
    parameter.second = &fButton0;
    parameter_list[6] = parameter;
    return parameter_list;
}

void faust::Eg::Compute(int count)
{
    FAUSTFLOAT* output0 = outputs_[0];
    float fSlow0 = (0.00100000005f * float(fVslider0));
    float fSlow1 = float(fButton0);
    int iSlow2 = (fSlow1 > 0.0f);
    int iSlow3 = (iSlow2 > 0);
    int iSlow4 = (fSlow1 == 0.0f);
    float fSlow5 = float(fVslider1);
    int iSlow6 = (iSlow4 > 0);
    float fSlow7 = float(fVslider2);
    float fSlow8 = float(fVslider3);
    float fSlow9 = float(fVslider4);
    float fSlow10 = (0.00100000005f * float(fVslider5));
    for (int i = 0; (i < count); i = (i + 1))
    {
        iVec0[0] = 1;
        fRec0[0] = (fSlow0 + (0.999000013f * fRec0[1]));
        fVec1[0] = fSlow1;
        int iTemp0 = ((fSlow1 == fVec1[1]) | iSlow4);
        float fTemp1 = (1.0f - (0.999000013f * float(iTemp0)));
        fRec2[0] = ((0.999000013f * (fRec2[1] * float(iTemp0))) + (fSlow5 * fTemp1));
        float fTemp2 = std::max(0.00100000005f, (fConst0 * fRec2[0]));
        fRec1[0] = (iSlow3?0.0f:std::min(fTemp2, (fRec1[1] + 1.0f)));
        fRec5[0] = ((0.999000013f * (fRec5[1] * float(iTemp0))) + (fSlow7 * fTemp1));
        fRec6[0] = ((0.999000013f * (fRec6[1] * float(iTemp0))) + (fSlow8 * fTemp1));
        float fTemp3 = (fConst0 * (fRec5[0] + fRec6[0]));
        fRec4[0] = (iSlow6?0.0f:std::min(fTemp3, (fRec4[1] + 1.0f)));
        float fTemp4 = (fConst0 * fRec6[0]);
        int iTemp5 = (fRec4[0] < fTemp4);
        fRec7[0] = ((0.999000013f * (fRec7[1] * float(iTemp0))) + (fSlow9 * fTemp1));
        float fTemp6 = (fSlow1 * fRec7[0]);
        fRec3[0] = (iSlow2?(float(iSlow2) * (iTemp5?((fRec4[0] < 0.0f)?0.0f:(iTemp5?(fConst1 * (fRec4[0] / fRec6[0])):1.0f)):((fRec4[0] < fTemp3)?((((fTemp6 + -1.0f) * (fRec4[0] - fTemp4)) / (0.0f - (fConst0 * (0.0f - fRec5[0])))) + 1.0f):fTemp6))):fRec3[1]);
        fRec8[0] = (fSlow10 + (0.999000013f * fRec8[1]));
        float fTemp7 = std::fabs(fRec8[0]);
        float fTemp8 = std::max(1.00000001e-07f, fTemp7);
        float fTemp9 = (fRec9[1] + (fConst2 * fTemp8));
        float fTemp10 = (fTemp9 + -1.0f);
        int iTemp11 = (fTemp10 < 0.0f);
        fRec9[0] = (iTemp11?fTemp9:fTemp10);
        float fRec10 = (iTemp11?fTemp9:(fTemp9 + (fTemp10 * (1.0f - (fConst0 / fTemp8)))));
        float fTemp12 = std::max(20.0f, fTemp7);
        float fTemp13 = (fRec11[1] + (fConst1 * fTemp12));
        fRec11[0] = (fTemp13 - std::floor(fTemp13));
        float fTemp14 = FaustPower2_f(((2.0f * fRec11[0]) + -1.0f));
        float fTemp15 = (fTemp14 * (fTemp14 + -2.0f));
        fVec2[0] = fTemp15;
        float fTemp16 = ((fTemp15 - fVec2[1]) / fTemp12);
        fVec3[0] = fTemp16;
        float fTemp17 = ((fTemp16 - fVec3[1]) / fTemp12);
        fVec4[0] = fTemp17;
        output0[i] = FAUSTFLOAT(((fRec0[0] * ((fRec1[0] < 0.0f)?fRec3[0]:((fRec1[0] < fTemp2)?(fRec3[0] + ((fRec1[0] * (0.0f - fRec3[0])) / fTemp2)):0.0f))) * ((float((500.0f > fRec8[0])) * ((2.0f * fRec10) + -1.0f)) + (fConst3 * (((float((500.0f < fRec8[0])) * (fTemp17 - fVec4[1])) * float(iVec0[3])) / fTemp12)))));
        for (int j0 = 3; (j0 > 0); j0 = (j0 - 1))
        {
            iVec0[j0] = iVec0[(j0 - 1)];

        }
        fRec0[1] = fRec0[0];
        fVec1[1] = fVec1[0];
        fRec2[1] = fRec2[0];
        fRec1[1] = fRec1[0];
        fRec5[1] = fRec5[0];
        fRec6[1] = fRec6[0];
        fRec4[1] = fRec4[0];
        fRec7[1] = fRec7[0];
        fRec3[1] = fRec3[0];
        fRec8[1] = fRec8[0];
        fRec9[1] = fRec9[0];
        fRec11[1] = fRec11[0];
        fVec2[1] = fVec2[0];
        fVec3[1] = fVec3[0];
        fVec4[1] = fVec4[0];

    }
}
