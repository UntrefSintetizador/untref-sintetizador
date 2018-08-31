CC = gcc
SOLIB_EXT = dll
FAUSTCSHARP = bin/FaustApi.$(SOLIB_EXT)
MINGW_LDFLAGS = -shared -Wl,--export-all-symbols -lws2_32 -lkernel32
CSHARP_LDFLAGS = $(MINGW_LDFLAGS) -Wl,--output-def=libs/FaustApi.def \
    -static-libgcc -Wl,--out-implib=libs/FaustApi.lib
FAUSTAPI = libs/FaustApi.$(SOLIB_EXT)
CXX=g++

FAUST_FILES = \
    src/faust_api.cpp \
    src/faust_api.hpp \
    src/graph_manager.cpp \
    src/graph_manager.hpp \
    src/patch.cpp \
    src/patch.hpp \
    src/patch_factory.cpp \
    src/patch_factory.hpp \
    src/patchs/dac.cpp \
    src/patchs/dac.hpp \
    src/patchs/dummy.cpp \
    src/patchs/dummy.hpp \
    src/patchs/eg.cpp \
    src/patchs/eg.hpp \
    src/patchs/vco.cpp \
    src/patchs/vco.hpp
		
csharplib: $(FAUSTCSHARP)

$(FAUSTCSHARP): ${FAUST_FILES:.c=.o}
	$(CXX) -o $(FAUSTCSHARP) $^ $(CSHARP_LDFLAGS) -lm -lpthread

clobber: 
	rm -f $(FAUSTAPI) ${FAUSTAPI:.$(SOLIB_EXT)=.lib} ${LIBPD:.$(SOLIB_EXT)=.def}
	rm -f $(FAUSTCSHARP) ${FAUSTCSHARP:.$(SOLIB_EXT)=.lib} ${FAUSTCSHARP:.$(SOLIB_EXT)=.def}
