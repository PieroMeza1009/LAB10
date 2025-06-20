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
