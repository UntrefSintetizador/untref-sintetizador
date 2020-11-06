package com.untref.synth3f;

import com.untref.synth3f.presentation_layer.View.DragMenu;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DragMenuTest {
    @Test
    public void whenCreatedFirstButtonIsVisible() throws Exception {
        int visible = 1;
        int gone = 0;
        int[] visibilities = new int[] {gone, gone};
        int firstButtonIndex = 0;

        DragMenu dragMenu = new DragMenu(visibilities, visible, gone);
        int firstButton = dragMenu.getVisibility(firstButtonIndex);

        assertThat(firstButton, is(visible));
    }
}