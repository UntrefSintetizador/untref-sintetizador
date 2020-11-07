package com.untref.synth3f;

import com.untref.synth3f.presentation_layer.View.DragMenu;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DragMenuTest {

    private int visible;
    private int gone;
    private int pageSize;

    @Before
    public void setUp() {
        visible = 1;
        gone = 0;
        pageSize = 4;
    }

    @Test
    public void whenCreatedFirstButtonIsVisible() {
        int[] visibilities = new int[] {gone, gone};
        int firstButtonIndex = 0;

        DragMenu dragMenu = new DragMenu(visibilities, pageSize, visible, gone);
        int firstButton = dragMenu.getVisibility(firstButtonIndex);

        assertThat(firstButton, is(visible));
    }

    @Test
    public void openDragMenuShowsSecondAndLastButton() {
        int numberOfButtons = 10;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, gone);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, visible, gone);

        dragMenu.open();
        int secondButton = dragMenu.getVisibility(1);
        int lastButton = dragMenu.getVisibility(-1);

        assertThat(secondButton, is(visible));
        assertThat(lastButton, is(visible));
    }

    @Test
    public void closeDragMenuHidesSecondAndLastButton() {
        int numberOfButtons = 10;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, gone);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, visible, gone);

        dragMenu.open();
        dragMenu.close();
        int secondButton = dragMenu.getVisibility(1);
        int lastButton = dragMenu.getVisibility(-1);

        assertThat(secondButton, is(gone));
        assertThat(lastButton, is(gone));
    }

    @Test
    public void openDragMenuShowsButtonsThatFitInAPage() {
        int numberOfButtons = 8;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, gone);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, visible, gone);

        dragMenu.open();
        int firstButtonInPage = dragMenu.getVisibility(2);
        int secondButtonInPage = dragMenu.getVisibility(3);
        int thirdButtonInPage = dragMenu.getVisibility(4);
        int fourthButtonInPage = dragMenu.getVisibility(5);
        int fifthButtonInPage = dragMenu.getVisibility(6);

        assertThat(firstButtonInPage, is(visible));
        assertThat(secondButtonInPage, is(visible));
        assertThat(thirdButtonInPage, is(visible));
        assertThat(fourthButtonInPage, is(visible));
        assertThat(fifthButtonInPage, is(gone));
    }
}