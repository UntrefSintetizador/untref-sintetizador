package com.untref.synth3f;

import com.untref.synth3f.domain_layer.helpers.PatchGraphManager;
import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.DACPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.entities.VCOPatch;

import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatchGraphManagerTest {

    private static final int ZERO = 0;

    @Test
    public void addTwoPatches() {
        Patch onePatch = new VCOPatch();
        Patch otherPatch = new DACPatch();
        PatchGraphManager patchGraphManager = new PatchGraphManager();

        patchGraphManager.addPatch(onePatch);
        patchGraphManager.addPatch(otherPatch);
        Collection<Patch> addedPatches = patchGraphManager.getPatches();

        assertThat(addedPatches, hasItem(onePatch));
        assertThat(addedPatches, hasItem(otherPatch));
    }

    @Test
    public void addingAPatchMakesManagerSetUniqueIdToPatch() {
        Patch firstPatch = new VCOPatch();
        Patch secondPatch = new VCFPatch();
        Patch thirdPatch = new DACPatch();
        PatchGraphManager patchGraphManager = new PatchGraphManager();

        patchGraphManager.addPatch(firstPatch);
        patchGraphManager.addPatch(secondPatch);
        patchGraphManager.addPatch(thirdPatch);
        int firstPatchId = firstPatch.getId();
        int secondPatchId = secondPatch.getId();
        int thirdPatchId = thirdPatch.getId();

        assertThat(firstPatchId, is(not(secondPatchId)));
        assertThat(secondPatchId, is(not(thirdPatchId)));
        assertThat(thirdPatchId, is(not(firstPatchId)));
    }

    @Test
    public void connectTwoPatches() {
        Patch sourcePatch = new VCOPatch();
        Patch destinyPatch = new DACPatch();
        int outletConnectorId = 0;
        int inletConnectorId = 1;
        PatchGraphManager patchGraphManager = new PatchGraphManager();
        patchGraphManager.addPatch(sourcePatch);
        patchGraphManager.addPatch(destinyPatch);
        int destinyPatchId = destinyPatch.getId();

        patchGraphManager.connect(sourcePatch.getId(), outletConnectorId, destinyPatchId,
                                  inletConnectorId);
        Connection sourceOutput = sourcePatch.getOutputConnections().get(0);
        Connection destinyInput = destinyPatch.getInputConnections().get(0);
        int sourceOutputTargetInletId = sourceOutput.getTargetInlet();
        int sourceOutputTargetPatchId = sourceOutput.getTargetPatch();

        assertThat(sourceOutput, is(destinyInput));
        assertThat(sourceOutputTargetInletId, is(inletConnectorId));
        assertThat(sourceOutputTargetPatchId, is(destinyPatchId));
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
        int numberOfOutputConnectionsInSourcePatch = sourcePatch.getOutputConnections().size();
        int numberOfInputConnectionsInDestinyPatch = destinyPatch.getInputConnections().size();

        assertThat(numberOfOutputConnectionsInSourcePatch, is(ZERO));
        assertThat(numberOfInputConnectionsInDestinyPatch, is(ZERO));
    }

    @Test
    public void removeAPatchFromManager() {
        Patch patchToRemove = new VCOPatch();
        Patch otherPatch = new DACPatch();
        PatchGraphManager patchGraphManager = new PatchGraphManager();
        patchGraphManager.addPatch(patchToRemove);
        patchGraphManager.addPatch(otherPatch);

        patchGraphManager.removePatch(patchToRemove.getId());
        Collection<Patch> addedPatches = patchGraphManager.getPatches();

        assertThat(addedPatches, not(hasItem(patchToRemove)));
        assertThat(addedPatches, hasItem(otherPatch));
    }

    @Test
    public void removeAllPatchesFromManager() {
        Patch onePatchToRemove = new VCOPatch();
        Patch otherPatchToRemove = new DACPatch();
        PatchGraphManager patchGraphManager = new PatchGraphManager();
        patchGraphManager.addPatch(onePatchToRemove);
        patchGraphManager.addPatch(otherPatchToRemove);

        patchGraphManager.removeAllPatches();
        int numberOfAddedPatches = patchGraphManager.getPatches().size();

        assertThat(numberOfAddedPatches, is(ZERO));
    }

}
