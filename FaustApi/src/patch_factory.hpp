#ifndef FAUSTAPI_PATCH_FACTORY_H_
#define FAUSTAPI_PATCH_FACTORY_H_

#include "patch.hpp"
#include <map>


namespace faust
{

class PatchFactory
{
public:

    void Init(FAUSTFLOAT* zero, int channels, int buffer_size, int sample_rate);

    faust::Patch* CreatePatch(std::string code, int id);

    void Free();

private:

    int channels_;
    int buffer_size_;
    int sample_rate_;
    FAUSTFLOAT* zero_;
    typedef faust::Patch* (*PatchCreatorFunction)(void);
    std::map<std::string, PatchCreatorFunction> patch_creator_functions_;

};
}

#endif // FAUSTAPI_PATCH_FACTORY_H_
