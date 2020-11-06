package com.untref.synth3f;

import com.untref.synth3f.presentation_layer.View.DragMenu;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DragMenuTest {
    @Test
    public void whenCreatedFirstButtonIsVisible() {
        int visible = 1;
        int gone = 0;
        int[] visibilities = new int[] {gone, gone};
        int firstButtonIndex = 0;

        DragMenu dragMenu = new DragMenu(visibilities, visible, gone);
        int firstButton = dragMenu.getVisibility(firstButtonIndex);

        assertThat(firstButton, is(visible));
    }

    @Test
    public void openDragMenuShowsSecondAndLastButton() {
        int visible = 1;
        int gone = 0;
        int numberOfButtons = 10;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, gone);
        DragMenu dragMenu = new DragMenu(visibilities, visible, gone);

        dragMenu.open();
        int secondButton = dragMenu.getVisibility(1);
        int lastButton = dragMenu.getVisibility(-1);

        assertThat(secondButton, is(visible));
        assertThat(lastButton, is(visible));
    }
}