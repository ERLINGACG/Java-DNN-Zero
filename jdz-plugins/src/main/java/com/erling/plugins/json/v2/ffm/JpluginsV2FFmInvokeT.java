package com.erling.plugins.json.v2.ffm;

import java.lang.foreign.Arena;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;

public class JpluginsV2FFmInvokeT {

    Linker linker = Linker.nativeLinker();
    SymbolLookup lookup ;

    public JpluginsV2FFmInvokeT(){
        lookup= SymbolLookup.libraryLookup(
                "E:\\ZeroPlan\\Java\\JDnn-Zero\\JDnnZero\\jdz-test\\libconfig\\share\\GeneralDnnLib_Zero_plugin_Json_v2.dll", Arena.ofAuto()
        );
    }

    public void lookup(){
        MemorySegment buildJsonData       = lookup.find("BuildJsonData").orElseThrow();
        MemorySegment buildLinkJsonObject = lookup.find("BuildLinkJsonObject").orElseThrow();
    }


}
