#include "graph_manager.hpp"
#include "patchs/dac.hpp"

#include <sstream>

void faust::GraphManager::Init(int channels, int buffer_size, int sample_rate)
{
    num_patches_ = 0;
    patches_size_ = 10;
    channels_ = channels;
    buffer_size_ = buffer_size;
    sample_rate_ = sample_rate;
    patches_ = new faust::Patch*[patches_size_];
    zero_ = new FAUSTFLOAT[buffer_size];
    output_ = new FAUSTFLOAT*[channels];
    for(int i = 0; i < channels; i++)
    {
        output_[i] = new FAUSTFLOAT[buffer_size];
    }
    patch_factory_.Init(zero_, channels, buffer_size, sample_rate);
}

void faust::GraphManager::AddPatch(std::string code, int id)
{
    if(patches_size_ == num_patches_)
    {
        ResizeArray();
    }
    Patch* patch = patch_factory_.CreatePatch(code, id);
    patches_[num_patches_] = patch;
    patch->SetPosition(num_patches_);
    id_map_[id] = patch;
    num_patches_ += 1;
    int num_parameters;
    std::string patch_id = std::to_string(id);
    std::stringstream full_name;
    std::pair<std::string, float*>* parameter_list = patch->GetParameterList(&num_parameters);
    for(int i = 0; i < num_parameters; i++)
    {
        full_name << code << "_" << parameter_list[i].first << "_" << patch_id;
        parameter_map_[full_name.str()] = parameter_list[i].second;
        full_name.str( std::string() );
        full_name.clear();
    }
    if(code.compare("dac") == 0)
    {
        ((faust::Dac*)patch)->SetChannels(channels_, output_, zero_);
    }
    delete[] parameter_list;
}

void faust::GraphManager::RemovePatch(int id)
{
    Patch* patch = id_map_[id];
    for(int i = patch->GetPosition(); i < num_patches_; i++)
    {
        patches_[i] = patches_[i + 1];
    }
    num_patches_ -= 1;
    int num_parameters;
    std::string code = patch->GetCode();
    std::string patch_id = std::to_string(id);
    std::stringstream full_name;
    std::pair<std::string, float*>* parameter_list = patch->GetParameterList(&num_parameters);
    for(int i = 0; i < num_parameters; i++)
    {
        full_name << code << "_" << parameter_list[i].first << "_" << patch_id;
        parameter_map_.erase(full_name.str());
        full_name.str( std::string() );
        full_name.clear();
    }
    delete[] parameter_list;
    delete patch;
}

void faust::GraphManager::SetValue(std::string name, float value)
{
    *parameter_map_[name] = value;
}

void faust::GraphManager::Connect(int source, int outlet, int target, int inlet)
{
    id_map_[target]->GetInputs()[inlet] = id_map_[source]->GetOutputs()[outlet];
}

void faust::GraphManager::Disconnect(int source, int outlet, int target, int inlet)
{
    id_map_[target]->GetInputs()[inlet] = zero_;
}

void faust::GraphManager::Process(float* data, int channels)
{
    FAUSTFLOAT* faust_data = static_cast<FAUSTFLOAT*>(data);
    faust::Patch* patch;
    for(int i = 0; i < channels_; i++)
    {
        for(int j = 0; j < buffer_size_; j++)
        {
            output_[i][j] = 0;
        }
    }
    for(int i = 0; i < num_patches_; i++)
    {
        patch = patches_[i];
        patch->Compute(buffer_size_);
    }
    int k = 0;
    for(int i = 0; i < buffer_size_; i++)
    {
        for(int j = 0; j < channels_; j++)
        {
            faust_data[k++] = output_[j][i];
        }
    }
}

void faust::GraphManager::ResizeArray()
{
    faust::Patch** patches_resized;
    patches_resized = new faust::Patch*[patches_size_ * 2];
    for(int i = 0; i < patches_size_; i++)
    {
        patches_resized[i] = patches_[i];
    }
    delete[] patches_;
    patches_ = patches_resized;
    patches_size_ *= 2;
}

void faust::GraphManager::Free()
{
    for(int i = 0; i < num_patches_; i++)
    {
        patches_[i]->Free();
        delete patches_[i];
    }
    delete[] patches_;
    for(int i = 0; i < channels_; i++)
    {
        delete output_[i];
    }
    delete[] output_;
    delete[] zero_;
    patch_factory_.Free();
}
