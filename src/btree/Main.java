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
}
