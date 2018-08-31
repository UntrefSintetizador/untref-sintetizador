#ifndef FAUSTAPI_GRAPH_MANAGER_H_
#define FAUSTAPI_GRAPH_MANAGER_H_

#include "patch.hpp"
#include "patch_factory.hpp"

namespace faust
{

class GraphManager
{
public:

    void Init(int channels, int buffer_size, int sample_rate);

    void AddPatch(std::string code, int id);

    void RemovePatch(int id);

    void SetValue(std::string name, float value);

    void Connect(int source, int outlet, int target, int inlet);

    void Disconnect(int source, int outlet, int target, int inlet);

    void Process(float* data, int channels);

    void Free();

private:

    faust::PatchFactory patch_factory_;
    faust::Patch **patches_;
    std::map<int, faust::Patch*> id_map_;
    std::map<std::string, float*> parameter_map_;
    int num_patches_;
    int patches_size_;
    int channels_;
    int buffer_size_;
    int sample_rate_;
    FAUSTFLOAT* zero_;
    FAUSTFLOAT** output_;

    void ResizeArray();
};
}

#endif // FAUSTAPI_GRAPH_MANAGER_H_
