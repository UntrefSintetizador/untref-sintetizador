#include "patch_factory.hpp"
#include "patchs/dac.hpp"
#include "patchs/vco.hpp"
#include "patchs/eg.hpp"

void faust::PatchFactory::Init(FAUSTFLOAT* zero, int channels, int buffer_size, int sample_rate){
    zero_ = zero;
    channels_ = channels;
    buffer_size_ = buffer_size;
    sample_rate_ = sample_rate;
    patch_creator_functions_["vco"] = &faust::Vco::Create;
    patch_creator_functions_["eg"] = &faust::Eg::Create;
    patch_creator_functions_["dac"] = &faust::Dac::Create;
}

faust::Patch* faust::PatchFactory::CreatePatch(std::string code, int id){
    faust::Patch* patch = patch_creator_functions_[code]();
    patch->Init(zero_, buffer_size_, sample_rate_, id);
    return patch;
}

void faust::PatchFactory::Free(){
    patch_creator_functions_.clear();
}
