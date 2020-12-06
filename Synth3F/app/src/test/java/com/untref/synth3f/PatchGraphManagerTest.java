package com.untref.synth3f;

import com.untref.synth3f.domain_layer.helpers.PatchGraphManager;
import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.DACPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.entities.VCOPatch;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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

    @Test
    public void connectTwoPatches() {
        Patch sourcePatch = new VCOPatch();
        Patch destinyPatch = new DACPatch();
        int outletId = 0;
        int inletId = 1;
        PatchGraphManager patchGraphManager = new PatchGraphManager();
        patchGraphManager.addPatch(sourcePatch);
        patchGraphManager.addPatch(destinyPatch);

        patchGraphManager.connect(sourcePatch.getId(), outletId, destinyPatch.getId(), inletId);

        Connection sourceOutput = sourcePatch.getOutputConnections().get(0);
        Connection destinyInput = destinyPatch.getInputConnections().get(0);
        assertThat(sourceOutput, is(destinyInput));
        assertThat(sourceOutput.getSourceOutlet(), is(outletId));
        assertThat(sourceOutput.getSourcePatch(), is(sourcePatch.getId()));
        assertThat(sourceOutput.getTargetInlet(), is(inletId));
        assertThat(sourceOutput.getTargetPatch(), is(destinyPatch.getId()));
    }

    @Test
    public void disconnectTwoPatches() {
        Patch sourcePatch = new VCOPatch();
        Patch destinyPatch = new DACPatch();
        int outletId = 0;
        int inletId = 1;
        PatchGraphManager patchGraphManager = new PatchGraphManager();
        patchGraphManager.addPatch(sourcePatch);
        patchGraphManager.addPatch(destinyPatch);
        Connection connection = patchGraphManager.connect(sourcePatch.getId(), outletId,
                                                          destinyPatch.getId(), inletId);

        patchGraphManager.disconnect(connection.getId());

        assertThat(sourcePatch.getOutputConnections().size(), is(0));
        assertThat(destinyPatch.getInputConnections().size(), is(0));
    }
}
