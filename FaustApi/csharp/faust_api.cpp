#include "faust_api.hpp"

faust::GraphManager* InitFaustApi(int channels, int buffer_size, int sample_rate)
{
    faust::GraphManager* graph_manager = new faust::GraphManager();
    graph_manager->Init(channels, buffer_size, sample_rate);
    return graph_manager;
}

void DisposeFaustApi(faust::GraphManager* graph_manager)
{
    if(graph_manager != NULL)
    {
        graph_manager->Free();
        delete graph_manager;
        graph_manager = NULL;
    }
}

void AddPatchFaustApi(faust::GraphManager* graph_manager, const char* code, int id)
{
    if(graph_manager != NULL)
    {
        std::string _code(code);
        graph_manager->AddPatch(_code, id);
    }
}

void RemovePatchFaustApi(faust::GraphManager* graph_manager, int id)
{
    if(graph_manager != NULL)
    {
        graph_manager->RemovePatch(id);
    }
}

void SetValueFaustApi(faust::GraphManager* graph_manager, const char* name, float value)
{
    if(graph_manager != NULL)
    {
        std::string _name(name);
        graph_manager->SetValue(_name, value);
    }
}

void ConnectFaustApi(faust::GraphManager* graph_manager, int source, int outlet, int target, int inlet)
{
    if(graph_manager != NULL)
    {
        graph_manager->Connect(source, outlet, target, inlet);
    }
}

void DisconnectFaustApi(faust::GraphManager* graph_manager, int source, int outlet, int target, int inlet)
{
    if(graph_manager != NULL)
    {
        graph_manager->Disconnect(source, outlet, target, inlet);
    }
}

void ProcessFaustApi(faust::GraphManager* graph_manager, float* data, int channels)
{
    if(graph_manager != NULL)
    {
        graph_manager->Process(data, channels);
    }
}
