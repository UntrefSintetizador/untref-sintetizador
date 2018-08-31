#ifndef FAUSTAPI_FAUST_API_H_
#define FAUSTAPI_FAUST_API_H_

#include "graph_manager.hpp"

#ifdef __cplusplus
extern "C" {
#endif

extern __declspec(dllexport) faust::GraphManager* InitFaustApi(int channels, int buffer_size, int sample_rate);

extern __declspec(dllexport) void DisposeFaustApi(faust::GraphManager* graph_manager);

extern __declspec(dllexport) void AddPatchFaustApi(faust::GraphManager* graph_manager, const char* code, int id);

extern __declspec(dllexport) void RemovePatchFaustApi(faust::GraphManager* graph_manager, int id);

extern __declspec(dllexport) void SetValueFaustApi(faust::GraphManager* graph_manager, const char* name, float value);

extern __declspec(dllexport) void ConnectFaustApi(faust::GraphManager* graph_manager, int source, int outlet, int target, int inlet);

extern __declspec(dllexport) void DisconnectFaustApi(faust::GraphManager* graph_manager, int source, int outlet, int target, int inlet);

extern __declspec(dllexport) void ProcessFaustApi(faust::GraphManager* graph_manager, float* data, int channels);

#ifdef __cplusplus
}
#endif

#endif // FAUSTAPI_FAUST_API_H_
