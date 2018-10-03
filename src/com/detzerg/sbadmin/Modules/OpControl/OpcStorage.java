package com.detzerg.sbadmin.Modules.OpControl;

import com.detzerg.sbadmin.Modules.Config.SBaseConfig;
import com.detzerg.sbadmin.SpigotPlugin;

import java.util.Arrays;

public class OpcStorage extends SBaseConfig {

    public OpcStorage() {
        super(SpigotPlugin.getMain(), ".sbaop", false);
    }

    @Override
    public void setDefault(){
        config.set("hash", "");
        config.set("ops", Arrays.asList());
    }
}
