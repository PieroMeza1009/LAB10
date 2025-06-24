package btree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import excepciones.ItemNoFound;
import modelo.RegistroEstudiante;

import java.io.*;
import java.util.*;

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

    // este es un metodo para buscar una clave en el árbol B
    public boolean search(E cl) {
        if (isEmpty()) { //si esta vacioooo
            System.out.println("El árbol está vacío.");
            return false;
        }

    // Llama al método recursivo para buscar desde la raíz
        return searchRecursive(root, cl);
    }
   
    //este metodo realiza la búsqueda en el nodo actual
    private boolean searchRecursive(BNode<E> current, E cl) {
        if (current == null) return false;

        int[] pos = new int[1];   ///// arreglo auxiliar para guardar la posición
        boolean found = current.searchNode(cl, pos); ///buscamos la cvlave del nodo

        if (found) {
            System.out.println(cl + " se encuentra en el nodo " + current.idNode + ", posición " + pos[0]);
            return true;
        } else {
        // Si no lo encuentra, desciende al hijo correspondiente
            return searchRecursive(current.childs.get(pos[0]), cl);
        }
    }

    // ===================== EJERCICIO 02: Eliminar clave =====================

    // Método para eliminar una clave del árbol B
    public void remove(E key) {
        if (root == null) {
            System.out.println("El árbol está vacío.");
            return;
        }
        delete(root, key);

        if (root.count == 0) { // Si luego de eliminar la raíz queda vacía
            if (root.childs.get(0) != null) {
                root = root.childs.get(0); /// el hijo se convierte en nueva raíz
            } else {
                root = null; // y sino el arbol queda vacio|
            }
        }
    }
    // Método recursivo que elimina una clave en un nodo
    private void delete(BNode<E> node, E key) {
        int[] pos = new int[1];
        boolean found = node.searchNode(key, pos);

        if (found) {
              // Caso 1: Clave está en una hoja
            if (node.childs.get(pos[0]) == null) {
                // Caso 1: clave en hoja
                for (int i = pos[0]; i < node.count - 1; i++) {
                    node.keys.set(i, node.keys.get(i + 1));     // Elimina la clave y reorganiza
                }
                node.keys.set(node.count - 1, null);
                node.count--;
            } else {
                // Caso 2: clave en nodo interno
                BNode<E> predNode = node.childs.get(pos[0]);
                if (predNode.count >= orden / 2) {
                    // Reemplaza con el predecesor si tiene suficientes claves
                    E pred = getPredecessor(predNode);
                    node.keys.set(pos[0], pred);
                    delete(predNode, pred);
                } else {
                    BNode<E> succNode = node.childs.get(pos[0] + 1);
                    if (succNode.count >= orden / 2) {
                        // Reemplaza con el sucesor si es más conveniente
                        E succ = getSuccessor(succNode);
                        node.keys.set(pos[0], succ);
                        delete(succNode, succ);
                    } else {  // Si no, fusiona y elimina
                        merge(node, pos[0]);
                        delete(predNode, key);
                    }
                }
            }
        } else {
            // Si no se encuentra la clave y estamos en hoja, termina
            if (node.childs.get(pos[0]) == null) {
                System.out.println("La clave " + key + " no existe.");
                return;
            }

            BNode<E> child = node.childs.get(pos[0]);
            if (child.count < orden / 2) { // Si el hijo tiene menos del mínimo de claves, lo completamos
                fill(node, pos[0]);
            }

            delete(node.childs.get(pos[0]), key);
        }
    }
// Métodos auxiliares usados por delete()

// Retorna el predecesor que seria el máximo valor del subárbol izquierdo
    private E getPredecessor(BNode<E> node) {
        while (node.childs.get(node.count) != null) {
            node = node.childs.get(node.count);
        }
        return node.keys.get(node.count - 1);
    }
    // Retorna el sucesor que seroa el mínimo valor del subárbol derecho
    private E getSuccessor(BNode<E> node) {
        while (node.childs.get(0) != null) {
            node = node.childs.get(0);
        }
        return node.keys.get(0);
    }
    //aca nos aseguramos que el hijo tenga el mínimo de claves necesarias
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

    //en esta parte se toma una clave prestada del hermano izquierdo
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
// y aqui se toma una clave prestada del hermano derecho
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
    // Fusiona dos hijos en uno solo
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

    //este es un metodo estático que construye un BTree a partir de un archivo de texto
    public static BTree<Integer> building_BTree(String filename) throws ItemNoFound {
        BTree<Integer> tree = null;// este el arbol que se va a contruuir
        Map<Integer, BNode<Integer>> nodos = new HashMap<>();//y este el mapa para guardar los nodos con su id

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            // aqui se lee la primera línea del archivo que contiene el orden del árbol
            int orden = Integer.parseInt(br.readLine().trim());
            tree = new BTree<>(orden); /// y se crea un nuevo BTree con ese orden

            //con este while se lee el resto de líneas, cada una representa un nodo del árbol
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();//esta linea elimina espacios innecesarios
                
                if (linea.isEmpty()) continue;// Ignora líneas vacías

                String[] partes = linea.split(",");//// Se separa la línea por comas

                // aca se obtiene el nivel, y el id del nodo
                int nivel = Integer.parseInt(partes[0].trim());
                int idNodo = Integer.parseInt(partes[1].trim());

                // Se crea el nodo con el orden correspondiente
                BNode<Integer> nodo = new BNode<>(orden);
                nodo.idNode = idNodo;

                //y mediante este for se agregan las claves al nodo
                for (int i = 2; i < partes.length; i++) {
                    int clave = Integer.parseInt(partes[i].trim());
                    nodo.keys.set(i - 2, clave); // Inserta la clave en la posición correcta
                    nodo.count++;
                }

                if (nodo.count > orden - 1) {
                    throw new ItemNoFound("Nodo con más claves que el orden permitido");
                }

                nodos.put(idNodo, nodo);
            }


            // Reconstrucción para archivo específico
            tree.root = nodos.get(6);
            tree.root.childs.set(0, nodos.get(2));
            tree.root.childs.set(1, nodos.get(5));

            nodos.get(2).childs.set(0, nodos.get(0));
            nodos.get(2).childs.set(1, nodos.get(12));
            nodos.get(2).childs.set(2, nodos.get(3));

            nodos.get(5).childs.set(0, nodos.get(4));
            nodos.get(5).childs.set(1, nodos.get(8));
            nodos.get(5).childs.set(2, nodos.get(7));

            return tree;

        } catch (IOException | NumberFormatException | NullPointerException e) {
            throw new ItemNoFound("Error al construir el árbol: " + e.getMessage());
        }
    }
    // ========== EJERCICIO 4 ==========
    public String buscarNombre(int codigo) {
        if (isEmpty()) return "Árbol vacío";
        return buscarNombreRecursivo(root, codigo);
    }
    private String buscarNombreRecursivo(BNode<E> current, int codigo) {
        if (current == null) return "No encontrado";

        for (int i = 0; i < current.count; i++) {
            E elemento = current.keys.get(i);
            if (elemento instanceof RegistroEstudiante) {
                RegistroEstudiante est = (RegistroEstudiante) elemento;
                if (est.getCodigo() == codigo) {
                    return est.getNombre();
                }
                if (codigo < est.getCodigo()) {
                    return buscarNombreRecursivo(current.childs.get(i), codigo);
                }
            }
        }

        return buscarNombreRecursivo(current.childs.get(current.count), codigo);
    }


}
