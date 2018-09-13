LOCAL_PATH := $(call my-dir)

CFLAGS := -DPD -DHAVE_UNISTD_H -DHAVE_LIBDL -DUSEAPI_DUMMY -w

include $(CLEAR_VARS)

LOCAL_MODULE := faust_api
LOCAL_CFLAGS := $(CFLAGS)
FILE_LIST := $(wildcard $(LOCAL_PATH)/src/*.hpp) $(wildcard $(LOCAL_PATH)/src/patchs/*.hpp) $(wildcard $(LOCAL_PATH)/src/*.cpp) $(wildcard $(LOCAL_PATH)/src/patchs/*.cpp)
LOCAL_SRC_FILES := $(FILE_LIST:$(LOCAL_PATH)/%=%)
include $(BUILD_SHARED_LIBRARY)
