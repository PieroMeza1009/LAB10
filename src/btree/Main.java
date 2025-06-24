package btree;

import btree.BTree;
import excepciones.ItemNoFound;

public class Main {
    public static void main(String[] args) {
        // Crear manualmente un BTree con orden 4
        BTree<Integer> arbol = new BTree<>(4);

        System.out.println("=== INSERTANDO CLAVES ===");
        int[] claves = {25, 10, 16, 30, 40, 1, 3, 8, 12, 15, 18, 19, 21, 27, 28, 33, 36, 39, 42, 45};

        for (int clave : claves) {
            arbol.insert(clave);
        }


        System.out.println("\n=== ÁRBOL ACTUAL ===");
        System.out.println(arbol);

        System.out.println("\n=== BÚSQUEDA DE CLAVES ===");
        arbol.search(42);  // clave que existe
        arbol.search(100); // clave que no existe

        System.out.println("\n=== ELIMINACIÓN DE CLAVES ===");
        arbol.remove(16);
        arbol.remove(33);
        arbol.remove(10);

        System.out.println("\n=== ÁRBOL TRAS ELIMINACIONES ===");
        System.out.println(arbol);

        System.out.println("\n=== CARGA DESDE ARCHIVO arbolB.txt ===");
        try {
            BTree<Integer> arbolDesdeArchivo = BTree.building_BTree("arbolB.txt");
            System.out.println("Árbol cargado correctamente desde archivo:");
            System.out.println(arbolDesdeArchivo);
        } catch (ItemNoFound e) {
            System.out.println("❌ Error al construir el árbol desde archivo: " + e.getMessage());
        }

}
