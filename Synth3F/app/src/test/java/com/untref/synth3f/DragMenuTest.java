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

    @Test
    public void scrollRightTwiceAdvancesTwoPages() {
        int numberOfButtons = 9;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.scrollRight();
        dragMenu.scrollRight();
        int firstButtonInFirstPage = dragMenu.getVisibility(2);
        int secondButtonInFirstPage = dragMenu.getVisibility(3);
        int firstButtonInSecondPage = dragMenu.getVisibility(4);
        int secondButtonInSecondPage = dragMenu.getVisibility(5);
        int firstButtonInThirdPage = dragMenu.getVisibility(6);
        int secondButtonInThirdPage = dragMenu.getVisibility(7);

        assertThat(firstButtonInFirstPage, is(GONE));
        assertThat(secondButtonInFirstPage, is(GONE));
        assertThat(firstButtonInSecondPage, is(GONE));
        assertThat(secondButtonInSecondPage, is(GONE));
        assertThat(firstButtonInThirdPage, is(VISIBLE));
        assertThat(secondButtonInThirdPage, is(VISIBLE));
    }

    @Test
    public void scrollRightLastPageGoesToFirstPage() {
        int numberOfButtons = 7;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.scrollRight();
        dragMenu.scrollRight();
        int firstButtonInFirstPage = dragMenu.getVisibility(2);
        int secondButtonInFirstPage = dragMenu.getVisibility(3);
        int firstButtonInSecondPage = dragMenu.getVisibility(4);
        int secondButtonInSecondPage = dragMenu.getVisibility(5);

        assertThat(firstButtonInFirstPage, is(VISIBLE));
        assertThat(secondButtonInFirstPage, is(VISIBLE));
        assertThat(firstButtonInSecondPage, is(GONE));
        assertThat(secondButtonInSecondPage, is(GONE));
    }

    @Test
    public void scrollRightLastPageKeepsFirstSecondAndLastButtonsVisible() {
        int numberOfButtons = 7;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.scrollRight();
        dragMenu.scrollRight();
        dragMenu.scrollRight();
        int firstButton = dragMenu.getVisibility(0);
        int secondButton = dragMenu.getVisibility(1);
        int lastButton = dragMenu.getVisibility(-1);

        assertThat(firstButton, is(VISIBLE));
        assertThat(secondButton, is(VISIBLE));
        assertThat(lastButton, is(VISIBLE));
    }

    @Test
    public void closeDragMenuHidesButtonsInCurrentPage() {
        int numberOfButtons = 7;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.scrollRight();
        dragMenu.close();
        int firstButtonInSecondPage = dragMenu.getVisibility(4);
        int secondButtonInSecondPage = dragMenu.getVisibility(5);

        assertThat(firstButtonInSecondPage, is(GONE));
        assertThat(secondButtonInSecondPage, is(GONE));
    }

    @Test
    public void reopenDragMenuShowsButtonsInLastPageShown() {
        int numberOfButtons = 7;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.scrollRight();
        dragMenu.close();
        dragMenu.open();
        int firstButtonInSecondPage = dragMenu.getVisibility(4);
        int secondButtonInSecondPage = dragMenu.getVisibility(5);

        assertThat(firstButtonInSecondPage, is(VISIBLE));
        assertThat(secondButtonInSecondPage, is(VISIBLE));
    }

    @Test
    public void scrollLeftOnceInSecondPageShowsFirstPage() {
        int numberOfButtons = 9;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        DragMenu dragMenu = new DragMenu(visibilities, pageSize, VISIBLE, GONE);

        dragMenu.open();
        dragMenu.scrollRight();
        dragMenu.scrollLeft();
        int firstButtonInFirstPage = dragMenu.getVisibility(2);
        int secondButtonInFirstPage = dragMenu.getVisibility(3);
        int firstButtonInSecondPage = dragMenu.getVisibility(4);
        int secondButtonInSecondPage = dragMenu.getVisibility(5);

        assertThat(firstButtonInFirstPage, is(VISIBLE));
        assertThat(secondButtonInFirstPage, is(VISIBLE));
        assertThat(firstButtonInSecondPage, is(GONE));
        assertThat(secondButtonInSecondPage, is(GONE));
    }

}