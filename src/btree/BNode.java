package btree;

import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    protected static int globalId = 0;
    protected int idNode;
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;

    public BNode(int n) {
        this.keys = new ArrayList<>(n);
        this.childs = new ArrayList<>(n + 1); // n+1 hijos posibles en B-Tree
        this.count = 0;
        this.idNode = globalId++; // ID único por nodo
        // Inicializa con null
        for (int i = 0; i < n; i++) {
            this.keys.add(null);
        }
        for (int i = 0; i < n + 1; i++) {
            this.childs.add(null);
        }
    }
    // Verifica si el nodo está lleno
    public boolean nodeFull(int maxKeys) {
        return count == maxKeys;
    }

    // Verifica si el nodo está vacío
    public boolean nodeEmpty() {
        return count == 0;
    }

    public boolean searchNode(E key, int[] pos) {
        int i = 0;
        while (i < count && keys.get(i).compareTo(key) < 0) {
            i++;
        }

        if (i < count && keys.get(i).compareTo(key) == 0) {
            pos[0] = i;
            return true; // Encontrado
        } else {
            pos[0] = i;
            return false; // No encontrado, pos indica el hijo a descender
        }
    }

    // Devuelve las claves del nodo como cadena
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node ID: ").append(idNode).append(" | Keys: ");
        for (int i = 0; i < count; i++) {
            sb.append(keys.get(i));
            if (i < count - 1) sb.append(", ");
        }
        return sb.toString();
    }
}
