LOCAL_PATH       :=  $(call my-dir)
include              $(CLEAR_VARS)
LOCAL_MODULE     :=  security
LOCAL_SRC_FILES  :=  jnitest.cpp
LOCAL_LDLIBS     :=  -L$(SYSROOT)/usr/lib -llog
include              $(BUILD_SHARED_LIBRARY)