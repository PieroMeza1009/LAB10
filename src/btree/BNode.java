package btree;

import java.util.ArrayList;

public class BNode<E extends Comparable<E>> {
    protected static int globalId = 0;
    protected int idNode;
    protected ArrayList<E> keys;
    protected ArrayList<BNode<E>> childs;
    protected int count;