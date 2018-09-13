CC = gcc
SOLIB_EXT = dll
FAUSTCSHARP = bin/FaustApi.$(SOLIB_EXT)
MINGW_LDFLAGS = -shared -Wl,--export-all-symbols -lws2_32 -lkernel32
CSHARP_LDFLAGS = $(MINGW_LDFLAGS) -Wl,--output-def=libs/FaustApi.def \
    -static-libgcc -Wl,--out-implib=libs/FaustApi.lib
FAUSTAPI = libs/FaustApi.$(SOLIB_EXT)
CXX=g++

SRC := src
SOURCES := $(wildcard $(SRC)/*.hpp) $(wildcard $(SRC)/*.cpp) $(wildcard $(SRC)/patchs/*.hpp) $(wildcard $(SRC)/patchs/*.cpp)

		
csharplib: $(FAUSTCSHARP)

$(FAUSTCSHARP): ${SOURCES:.c=.o}
	$(CXX) -o $(FAUSTCSHARP) $^ $(CSHARP_LDFLAGS) -lm -lpthread

clobber: 
	rm -f $(FAUSTAPI) ${FAUSTAPI:.$(SOLIB_EXT)=.lib}
	rm -f $(FAUSTCSHARP) ${FAUSTCSHARP:.$(SOLIB_EXT)=.lib} ${FAUSTCSHARP:.$(SOLIB_EXT)=.def}
