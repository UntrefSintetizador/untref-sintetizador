package com.untref.synth3f;

import com.untref.synth3f.domain_layer.helpers.PatchGraphManager;
import com.untref.synth3f.entities.DACPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.entities.VCOPatch;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatchGraphManagerTest {

    @Test
    public void addingAPatchMakesManagerSetUniqueIdToPatch() {
        Patch firstPatch = new VCOPatch();
        Patch secondPatch = new VCFPatch();
        Patch thirdPatch = new DACPatch();
        PatchGraphManager patchGraphManager = new PatchGraphManager();

        patchGraphManager.addPatch(firstPatch);
        patchGraphManager.addPatch(secondPatch);
        patchGraphManager.addPatch(thirdPatch);

        assertThat(firstPatch.getId(), is(not(secondPatch.getId())));
        assertThat(secondPatch.getId(), is(not(thirdPatch.getId())));
        assertThat(thirdPatch.getId(), is(not(firstPatch.getId())));
    }
}
