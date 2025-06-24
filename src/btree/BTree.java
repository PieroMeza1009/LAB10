package btree;

import java.util.HashMap;
import java.util.Map;

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
                node.keys.set(node.count - 1, null);
                node.count--;
            } else {
                // Caso 2: clave en nodo interno
                BNode<E> predNode = node.childs.get(pos[0]);
                if (predNode.count >= orden / 2) {
                    E pred = getPredecessor(predNode);
                    node.keys.set(pos[0], pred);
                    delete(predNode, pred);
                } else {
                    BNode<E> succNode = node.childs.get(pos[0] + 1);
                    if (succNode.count >= orden / 2) {
                        E succ = getSuccessor(succNode);
                        node.keys.set(pos[0], succ);
                        delete(succNode, succ);
                    } else {
                        merge(node, pos[0]);
                        delete(predNode, key);
                    }
                }
            }
        } else {
            if (node.childs.get(pos[0]) == null) {
                System.out.println("La clave " + key + " no existe.");
                return;
            }

            BNode<E> child = node.childs.get(pos[0]);
            if (child.count < orden / 2) {
                fill(node, pos[0]);
            }

            delete(node.childs.get(pos[0]), key);
        }
    }

    private E getPredecessor(BNode<E> node) {
        while (node.childs.get(node.count) != null) {
            node = node.childs.get(node.count);
        }
        return node.keys.get(node.count - 1);
    }
    private E getSuccessor(BNode<E> node) {
        while (node.childs.get(0) != null) {
            node = node.childs.get(0);
        }
        return node.keys.get(0);
    }
    private void fill(BNode<E> node, int idx) {
        if (idx != 0 && node.childs.get(idx - 1).count >= orden / 2) {
            borrowFromPrev(node, idx);
        } else if (idx != node.count && node.childs.get(idx + 1).count >= orden / 2) {
            borrowFromNext(node, idx);
        } else {
            if (idx != node.count) {
                merge(node, idx);
            } else {
                merge(node, idx - 1);
            }
        }
    }
    private void borrowFromPrev(BNode<E> node, int idx) {
        BNode<E> child = node.childs.get(idx);
        BNode<E> sibling = node.childs.get(idx - 1);

        for (int i = child.count - 1; i >= 0; i--) {
            child.keys.set(i + 1, child.keys.get(i));
        }
        child.keys.set(0, node.keys.get(idx - 1));

        if (child.childs.get(0) != null) {
            for (int i = child.count; i >= 0; i--) {
                child.childs.set(i + 1, child.childs.get(i));
            }
            child.childs.set(0, sibling.childs.get(sibling.count));
        }

        node.keys.set(idx - 1, sibling.keys.get(sibling.count - 1));
        sibling.count--;
        child.count++;
    }

    private void borrowFromNext(BNode<E> node, int idx) {
        BNode<E> child = node.childs.get(idx);
        BNode<E> sibling = node.childs.get(idx + 1);

        child.keys.set(child.count, node.keys.get(idx));
        node.keys.set(idx, sibling.keys.get(0));
        for (int i = 0; i < sibling.count - 1; i++) {
            sibling.keys.set(i, sibling.keys.get(i + 1));
        }
        sibling.keys.set(sibling.count - 1, null);

        if (sibling.childs.get(0) != null) {
            child.childs.set(child.count + 1, sibling.childs.get(0));
            for (int i = 0; i < sibling.count; i++) {
                sibling.childs.set(i, sibling.childs.get(i + 1));
            }
        }

        child.count++;
        sibling.count--;
    }
    private void merge(BNode<E> node, int idx) {
        BNode<E> child = node.childs.get(idx);
        BNode<E> sibling = node.childs.get(idx + 1);

        child.keys.set(child.count, node.keys.get(idx));
        for (int i = 0; i < sibling.count; i++) {
            child.keys.set(child.count + 1 + i, sibling.keys.get(i));
        }

        if (sibling.childs.get(0) != null) {
            for (int i = 0; i <= sibling.count; i++) {
                child.childs.set(child.count + 1 + i, sibling.childs.get(i));
            }
        }

        for (int i = idx; i < node.count - 1; i++) {
            node.keys.set(i, node.keys.get(i + 1));
            node.childs.set(i + 1, node.childs.get(i + 2));
        }

        child.count += sibling.count + 1;
        node.count--;
    }

    // ========== EJERCICIO 3 ==========
    public static BTree<Integer> building_BTree(String filename) throws ItemNoFound {
        BTree<Integer> tree = null;
        Map<Integer, BNode<Integer>> nodos = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            int orden = Integer.parseInt(br.readLine().trim());
            tree = new BTree<>(orden);

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split(",");
                int nivel = Integer.parseInt(partes[0].trim());
                int idNodo = Integer.parseInt(partes[1].trim());

                BNode<Integer> nodo = new BNode<>(orden);
                nodo.idNode = idNodo;

                for (int i = 2; i < partes.length; i++) {
                    int clave = Integer.parseInt(partes[i].trim());
                    nodo.keys.set(i - 2, clave);
                    nodo.count++;
                }

                if (nodo.count > orden - 1) {


}
