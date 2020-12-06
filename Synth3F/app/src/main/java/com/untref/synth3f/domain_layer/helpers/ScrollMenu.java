package com.untref.synth3f.domain_layer.helpers;

/**
 * Helper para una vista que se pueda usar como menu cuyas opciones se dividan en paginas. Solo una
 * pagina quedara visible al usuario. Esta clase provee metodos para avanzar y retroceder de
 * pagina.
 */
public class ScrollMenu {

    private static final int FIRST_PAGE_FIRST_INDEX = 2;
    private final int[] viewVisibilities;
    private final int visible;
    private final int gone;
    private final int pageSize;
    private int pageFirstIndex = FIRST_PAGE_FIRST_INDEX;
    private int numberOfButtonsInPages;

    /**
     * Crea un nuevo helper de menu. La primer opcion se usa para abrir y cerrar el menu. La
     * segunda y la ultima opcion se usan para cambiar de pagina. El resto de las opciones se
     * se dividen en paginas, siendo la primera la pagina a mostrar cuando se abra el menu por
     * primera vez.
     *
     * @param viewVisibilities Visibilidades de las opciones. Debe contener unicamente valores
     *                         iguales a los establecidos en los parametros <code>visible</code> y
     *                         <code>gone</code>.
     * @param pageSize Tamanio de cada pagina.
     * @param visible Valor usado para indicar que una opcion esta visible.
     * @param gone Valor usado para indicar que una opcion no esta.
     */
    public ScrollMenu(int[] viewVisibilities, int pageSize, int visible, int gone) {
        this.viewVisibilities = viewVisibilities.clone();
        this.pageSize = pageSize;
        this.visible = visible;
        this.gone = gone;
        this.viewVisibilities[0] = visible;
        numberOfButtonsInPages = viewVisibilities.length - 1;
    }

    /**
     * Avanza una pagina de opciones. Si la pagina es la ultima, vuelve a la primera.
     */
    public void scrollLeft() {
        int previousPageFirstIndex = pageFirstIndex - pageSize;
        if (previousPageFirstIndex < FIRST_PAGE_FIRST_INDEX) {
            previousPageFirstIndex = numberOfButtonsInPages - pageSize;
        }
        for (int i = pageFirstIndex; i < pageFirstIndex + pageSize; i++) {
            viewVisibilities[i] = gone;
        }
        for (int i = previousPageFirstIndex; i < previousPageFirstIndex + pageSize; i++) {
            viewVisibilities[i] = visible;
        }
        pageFirstIndex = previousPageFirstIndex;
    }

    /**
     * Retrocede una pagina de opciones. Si la pagina es la primera, vuelve a la ultima.
     */
    public void scrollRight() {
        int nextPageFirstIndex = pageFirstIndex + pageSize;
        if (nextPageFirstIndex >= numberOfButtonsInPages) {
            nextPageFirstIndex = FIRST_PAGE_FIRST_INDEX;
        }
        int nextPageLastIndex = Math.min(nextPageFirstIndex + pageSize, numberOfButtonsInPages);
        for (int i = pageFirstIndex; i < pageFirstIndex + pageSize; i++) {
            viewVisibilities[i] = gone;
        }
        for (int i = nextPageFirstIndex; i < nextPageLastIndex; i++) {
            viewVisibilities[i] = visible;
        }
        pageFirstIndex = nextPageFirstIndex;
    }

    /**
     * Retorna el valor de visibilidad de una opcion del menu.
     *
     * @param viewIndex Indice correspondiente al array de visibilidades establecido en el
     *                  constructor. Si el indice es negativo, se cuenta a partir de la ultima
     *                  opcion, contando hacia atras. Por ej: si el indice es -1, se accedera al
     *                  valor de visibilidad de la ultima opcion del menu.
     * @return el valor de visibilidad de una opcion del menu.
     */
    public int getVisibility(int viewIndex) {
        if (viewIndex < 0) {
            viewIndex = viewVisibilities.length + viewIndex;
        }
        return viewVisibilities[viewIndex];
    }

    /**
     * Abre el menu, mostrando las opciones de la pagina actual, junto con las opciones para
     * cambiar de pagina.
     */
    public void open() {
        viewVisibilities[1] = visible;
        viewVisibilities[viewVisibilities.length - 1] = visible;
        for (int i = pageFirstIndex; i < pageFirstIndex + pageSize; i++) {
            viewVisibilities[i] = visible;
        }
    }

    /**
     * Cierra el menu, ocultando todas las opciones, salvo la opcion de abrir y cerrar el menu.
     */
    public void close() {
        viewVisibilities[1] = gone;
        viewVisibilities[viewVisibilities.length - 1] = gone;
        for (int i = FIRST_PAGE_FIRST_INDEX; i < numberOfButtonsInPages; i++) {
            viewVisibilities[i] = gone;
        }
    }
}
