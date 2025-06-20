package btree;

public class BTree<E extends Comparable<E>> {
    private BNode<E> root;
    private int orden;
    private boolean up;
    private BNode<E> nDes;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    // Verifica si el árbol está vacío
    public boolean isEmpty() {
        return this.root == null;
    }
    // Inserta una nueva clave en el árbol
    public void insert(E cl) {
        up = false;
        E mediana;
        BNode<E> pnew;

        mediana = push(this.root, cl);

        if (up) {
            pnew = new BNode<>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, nDes);
            this.root = pnew;
        }
    }

    // Inserción recursiva
    private E push(BNode<E> current, E cl) {
        int pos[] = new int[1];
        E mediana;

        if (current == null) {
            up = true;
            nDes = null;
            return cl;
        } else {
            boolean found = current.searchNode(cl, pos);

            if (found) {
                System.out.println("Item duplicado\n");
                up = false;
                return null;
            }

            mediana = push(current.childs.get(pos[0]), cl);

            if (up) {
                if (current.nodeFull(this.orden - 1)) {
                    mediana = dividedNode(current, mediana, pos[0]);
                } else {
                    putNode(current, mediana, nDes, pos[0]);
                    up = false;
                }
                return mediana;
            } else {
                return null;
            }
        }
    }
    // Inserta una clave y subárbol derecho en la posición indicada
    private void putNode(BNode<E> current, E cl, BNode<E> rd, int k) {
        for (int i = current.count - 1; i >= k; i--) {
            current.keys.set(i + 1, current.keys.get(i));
            current.childs.set(i + 2, current.childs.get(i + 1));
        }
        current.keys.set(k, cl);
        current.childs.set(k + 1, rd);
        current.count++;
    }
    // Divide un nodo lleno en dos y devuelve la mediana para subirla
    private E dividedNode(BNode<E> current, E cl, int k) {
        BNode<E> rd = nDes;
        int posMdna;
        int t = this.orden;
        posMdna = (k <= t / 2) ? t / 2 : t / 2 + 1;

        nDes = new BNode<>(this.orden);

        for (int i = posMdna; i < t - 1; i++) {
            nDes.keys.set(i - posMdna, current.keys.get(i));
            nDes.childs.set(i - posMdna + 1, current.childs.get(i + 1));
        }

        nDes.count = (t - 1) - posMdna;
        current.count = posMdna;

        if (k <= t / 2) {
            putNode(current, cl, rd, k);
        } else {
            putNode(nDes, cl, rd, k - posMdna);
        }

        E median = current.keys.get(current.count - 1);
        nDes.childs.set(0, current.childs.get(current.count));
        current.count--;

        return median;
    }


    // ===================== Actividad 3.3 =====================

    @Override
    public String toString() {
        String s = "";
        if (isEmpty()) {
            s += "BTree is empty...";
        } else {
            s = writeTree(this.root);
        }
        return s;
    }

    private String writeTree(BNode<E> current) {
        if (current == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("Node ").append(current.idNode).append(": [");

        for (int i = 0; i < current.count; i++) {
            sb.append(current.keys.get(i));
            if (i < current.count - 1) sb.append(" | ");
        }
        sb.append("]\n");
        for (int i = 0; i <= current.count; i++) {
            sb.append(writeTree(current.childs.get(i)));
        }

        return sb.toString();
    }



    // ===================== Ejercicio 01 =====================

    public boolean search(E cl) {
        if (isEmpty()) {
            System.out.println("El árbol está vacío.");
            return false;
        }

        return searchRecursive(root, cl);
    }

    private boolean searchRecursive(BNode<E> current, E cl) {
        if (current == null) return false;

        int[] pos = new int[1];
        boolean found = current.searchNode(cl, pos);

        if (found) {
            System.out.println(cl + " se encuentra en el nodo " + current.idNode + ", posición " + pos[0]);
            return true;
        } else {
            return searchRecursive(current.childs.get(pos[0]), cl);
        }
    }

    // ===================== EJERCICIO 02: Eliminar clave =====================

    public void remove(E key) {
        if (root == null) {
            System.out.println("El árbol está vacío.");
            return;
        }
        delete(root, key);

        if (root.count == 0) {
            if (root.childs.get(0) != null) {
                root = root.childs.get(0); // Hijo único se vuelve la nueva raíz
            } else {
                root = null;
            }
        }
    }
    private void delete(BNode<E> node, E key) {
        int[] pos = new int[1];
        boolean found = node.searchNode(key, pos);

        if (found) {
            if (node.childs.get(pos[0]) == null) {
                // Caso 1: clave en hoja
                for (int i = pos[0]; i < node.count - 1; i++) {
                    node.keys.set(i, node.keys.get(i + 1));
                }

}
