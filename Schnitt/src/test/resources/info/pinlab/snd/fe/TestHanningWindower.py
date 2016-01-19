#! coding: utf-8

import Fe
import ConfigParser
import sys
import os

if __name__ == "__main__":
    # setting ini file: FeParamSheet.ini
    inifile = ConfigParser.SafeConfigParser()
    inifile.read("./FeParamSheet.ini")

    section = "test_resource"
    wav_file = inifile.get(section, "wav_file")

    section = "default_parameter"
    windowtype = inifile.get(section, "windowtype")
    window_len = int(inifile.get(section, "window_len"))
    hz         = int(inifile.get(section, "sampling_rate"))

    section = "output"
    output = inifile.get(section, "output_file")
    
    #print "win: , "windowtype
    #print "wavfile: ", wav_file
    #print "win len: ",  window_len
    #print "hz: ", hz

    fe = Fe.Fe()
    fe.set_wavfile(wav_file)        \
        .set_windowtype(windowtype) \
        .set_hz(hz)                 \
        .set_windowlen(window_len)  \

    # read int value of wav samples
    fe.do_wav2int()
    int_samples = fe.get_intsamples()
    #print "int_samples", len(int_samples)
    windowed_samples = fe.do_windowning(int_samples)
    #print windowed_samples
    f = open("test_int.txt", "w")
    for i in range(0, len(int_samples)):
        f.write(str(int_samples[i]) + "\n")
    f.close()

    f = open(output, "w")
    for i in range(0, len(windowed_samples)):
        f.write(str(windowed_samples[i]) + "\n")
    f.close()

    print "finish"