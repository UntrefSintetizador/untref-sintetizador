#ifndef JNI_DSPFAUST_H_
#define JNI_DSPFAUST_H_

namespace faust
{
class AndroidGraphAdapter;
}

#ifndef dsp
#define dsp faust::AndroidGraphAdapter
#endif

class audio
{
public:

    virtual bool init(const char* name, dsp* DSP);
    virtual bool start();
    virtual void stop();

    static audio* InitAndroidAudio(long srate, long bsize);

};


#endif // JNI_DSPFAUST_H_
