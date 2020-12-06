package com.untref.synth3f;

import com.untref.synth3f.domain_layer.helpers.ScrollMenu;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ScrollMenuTest {

    private static final int VISIBLE = 0;
    private static final int GONE = 1;
    private final int pageSize = 2;

    @Test
    public void whenCreatedFirstButtonIsVisible() {
        int[] visibilities = new int[] {GONE, GONE};
        int firstButtonIndex = 0;

        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);
        int firstButton = scrollMenu.getVisibility(firstButtonIndex);

        assertThat(firstButton, is(VISIBLE));
    }

    @Test
    public void openScrollMenuShowsSecondAndLastButton() {
        int numberOfButtons = 10;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        int secondButton = scrollMenu.getVisibility(1);
        int lastButton = scrollMenu.getVisibility(-1);

        assertThat(secondButton, is(VISIBLE));
        assertThat(lastButton, is(VISIBLE));
    }

    @Test
    public void closeScrollMenuHidesSecondAndLastButton() {
        int numberOfButtons = 10;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.close();
        int secondButton = scrollMenu.getVisibility(1);
        int lastButton = scrollMenu.getVisibility(-1);

        assertThat(secondButton, is(GONE));
        assertThat(lastButton, is(GONE));
    }

    @Test
    public void openScrollMenuShowsButtonsThatFitInAPage() {
        int numberOfButtons = 6;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        int firstButtonInCurrentPage = scrollMenu.getVisibility(2);
        int secondButtonInCurrentPage = scrollMenu.getVisibility(3);
        int firstButtonInNextPage = scrollMenu.getVisibility(4);

        assertThat(firstButtonInCurrentPage, is(VISIBLE));
        assertThat(secondButtonInCurrentPage, is(VISIBLE));
        assertThat(firstButtonInNextPage, is(GONE));
    }

    @Test
    public void closeScrollMenuHidesButtonsThatFitInAPage() {
        int numberOfButtons = 6;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.close();
        int firstButtonInCurrentPage = scrollMenu.getVisibility(2);
        int secondButtonInCurrentPage = scrollMenu.getVisibility(3);
        int firstButtonInNextPage = scrollMenu.getVisibility(4);

        assertThat(firstButtonInCurrentPage, is(GONE));
        assertThat(secondButtonInCurrentPage, is(GONE));
        assertThat(firstButtonInNextPage, is(GONE));
    }

    @Test
    public void scrollRightOnceHidesCurrentPageAndShowsNext() {
        int numberOfButtons = 7;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.scrollRight();
        int firstButtonInCurrentPage = scrollMenu.getVisibility(2);
        int secondButtonInCurrentPage = scrollMenu.getVisibility(3);
        int firstButtonInNextPage = scrollMenu.getVisibility(4);
        int secondButtonInNextPage = scrollMenu.getVisibility(5);

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
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.scrollRight();
        scrollMenu.scrollRight();
        int firstButtonInFirstPage = scrollMenu.getVisibility(2);
        int secondButtonInFirstPage = scrollMenu.getVisibility(3);
        int firstButtonInSecondPage = scrollMenu.getVisibility(4);
        int secondButtonInSecondPage = scrollMenu.getVisibility(5);
        int firstButtonInThirdPage = scrollMenu.getVisibility(6);
        int secondButtonInThirdPage = scrollMenu.getVisibility(7);

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
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.scrollRight();
        scrollMenu.scrollRight();
        int firstButtonInFirstPage = scrollMenu.getVisibility(2);
        int secondButtonInFirstPage = scrollMenu.getVisibility(3);
        int firstButtonInSecondPage = scrollMenu.getVisibility(4);
        int secondButtonInSecondPage = scrollMenu.getVisibility(5);

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
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.scrollRight();
        scrollMenu.scrollRight();
        scrollMenu.scrollRight();
        int firstButton = scrollMenu.getVisibility(0);
        int secondButton = scrollMenu.getVisibility(1);
        int lastButton = scrollMenu.getVisibility(-1);

        assertThat(firstButton, is(VISIBLE));
        assertThat(secondButton, is(VISIBLE));
        assertThat(lastButton, is(VISIBLE));
    }

    @Test
    public void closeScrollMenuHidesButtonsInCurrentPage() {
        int numberOfButtons = 7;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.scrollRight();
        scrollMenu.close();
        int firstButtonInSecondPage = scrollMenu.getVisibility(4);
        int secondButtonInSecondPage = scrollMenu.getVisibility(5);

        assertThat(firstButtonInSecondPage, is(GONE));
        assertThat(secondButtonInSecondPage, is(GONE));
    }

    @Test
    public void reopenScrollMenuShowsButtonsInLastPageShown() {
        int numberOfButtons = 7;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.scrollRight();
        scrollMenu.close();
        scrollMenu.open();
        int firstButtonInSecondPage = scrollMenu.getVisibility(4);
        int secondButtonInSecondPage = scrollMenu.getVisibility(5);

        assertThat(firstButtonInSecondPage, is(VISIBLE));
        assertThat(secondButtonInSecondPage, is(VISIBLE));
    }

    @Test
    public void scrollLeftOnceInSecondPageShowsFirstPage() {
        int numberOfButtons = 9;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.scrollRight();
        scrollMenu.scrollLeft();
        int firstButtonInFirstPage = scrollMenu.getVisibility(2);
        int secondButtonInFirstPage = scrollMenu.getVisibility(3);
        int firstButtonInSecondPage = scrollMenu.getVisibility(4);
        int secondButtonInSecondPage = scrollMenu.getVisibility(5);

        assertThat(firstButtonInFirstPage, is(VISIBLE));
        assertThat(secondButtonInFirstPage, is(VISIBLE));
        assertThat(firstButtonInSecondPage, is(GONE));
        assertThat(secondButtonInSecondPage, is(GONE));
    }

    @Test
    public void scrollLeftFirstPageGoesToLastPage() {
        int numberOfButtons = 9;
        int[] visibilities = new int[numberOfButtons];
        Arrays.fill(visibilities, GONE);
        ScrollMenu scrollMenu = new ScrollMenu(visibilities, pageSize, VISIBLE, GONE);

        scrollMenu.open();
        scrollMenu.scrollLeft();
        int firstButtonInFirstPage = scrollMenu.getVisibility(2);
        int secondButtonInFirstPage = scrollMenu.getVisibility(3);
        int firstButtonInLastPage = scrollMenu.getVisibility(6);
        int secondButtonInLastPage = scrollMenu.getVisibility(7);

        assertThat(firstButtonInFirstPage, is(GONE));
        assertThat(secondButtonInFirstPage, is(GONE));
        assertThat(firstButtonInLastPage, is(VISIBLE));
        assertThat(secondButtonInLastPage, is(VISIBLE));
    }
}