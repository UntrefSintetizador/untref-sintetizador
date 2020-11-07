package com.untref.synth3f;

import com.untref.synth3f.presentation_layer.View.DragMenu;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DragMenuTest {

    private static final int VISIBLE = 0;
    private static final int GONE = 1;
    private final int pageSize = 2;

    @Test
    public void whenCreatedFirstButtonIsVisible() {
        int[] visibilities = new int[] {GONE, GONE};
        int firstButtonIndex = 0;

        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);
        int firstButton = dragMenu.getVisibility(firstButtonIndex);

        assertThat(firstButton, is(VISIBLE));
    }

    @Test
    public void openDragMenuShowsSecondAndLastButton() {
        int numberOfButtons = 10;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        int secondButton = dragMenu.getVisibility(1);
        int lastButton = dragMenu.getVisibility(-1);

        assertThat(secondButton, is(VISIBLE));
        assertThat(lastButton, is(VISIBLE));
    }

    @Test
    public void closeDragMenuHidesSecondAndLastButton() {
        int numberOfButtons = 10;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.close();
        int secondButton = dragMenu.getVisibility(1);
        int lastButton = dragMenu.getVisibility(-1);

        assertThat(secondButton, is(GONE));
        assertThat(lastButton, is(GONE));
    }

    @Test
    public void openDragMenuShowsButtonsThatFitInAPage() {
        int numberOfButtons = 6;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        int firstButtonInCurrentPage = dragMenu.getVisibility(2);
        int secondButtonInCurrentPage = dragMenu.getVisibility(3);
        int firstButtonInNextPage = dragMenu.getVisibility(4);

        assertThat(firstButtonInCurrentPage, is(VISIBLE));
        assertThat(secondButtonInCurrentPage, is(VISIBLE));
        assertThat(firstButtonInNextPage, is(GONE));
    }

    @Test
    public void closeDragMenuHidesButtonsThatFitInAPage() {
        int numberOfButtons = 6;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.close();
        int firstButtonInCurrentPage = dragMenu.getVisibility(2);
        int secondButtonInCurrentPage = dragMenu.getVisibility(3);
        int firstButtonInNextPage = dragMenu.getVisibility(4);

        assertThat(firstButtonInCurrentPage, is(GONE));
        assertThat(secondButtonInCurrentPage, is(GONE));
        assertThat(firstButtonInNextPage, is(GONE));
    }

    @Test
    public void scrollRightOnceHidesCurrentPageAndShowsNext() {
        int numberOfButtons = 7;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.scrollRight();
        int firstButtonInCurrentPage = dragMenu.getVisibility(2);
        int secondButtonInCurrentPage = dragMenu.getVisibility(3);
        int firstButtonInNextPage = dragMenu.getVisibility(4);
        int secondButtonInNextPage = dragMenu.getVisibility(5);

        assertThat(firstButtonInCurrentPage, is(GONE));
        assertThat(secondButtonInCurrentPage, is(GONE));
        assertThat(firstButtonInNextPage, is(VISIBLE));
        assertThat(secondButtonInNextPage, is(VISIBLE));
    }
}