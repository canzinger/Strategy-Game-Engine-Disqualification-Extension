package at.ac.tuwien.ifs.sge.util.tree;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public interface Tree<E> extends Iterable<E> {

  Iterator<E> preIterator();

  Iterator<Tree<E>> preTreeIterator();

  Iterator<E> postIterator();

  Iterator<Tree<E>> postTreeIterator();

  Iterator<E> levelIterator();

  Iterator<Tree<E>> levelTreeIterator();

  E getNode();

  Tree<E> setNode(E e);

  Tree<E> getParent();

  void setParent(Tree<E> parent);

  Tree<E> getChild(int index);

  List<Tree<E>> getChildren();

  default boolean isRoot() {
    return getParent() == null;
  }

  default Tree<E> getRoot() {
    Tree<E> root = this;
    while (!root.isRoot()) {
      root = root.getParent();
    }
    return root;
  }

  void dropParent();

  void dropChild(int n);

  void dropChildren();

  default void reRoot(int n) {
    reRoot(getChild(n));
  }

  default void reRoot(Tree<E> tree) {
    this.setNode(tree.getNode());
    this.dropChildren();
    this.dropParent();
    for (Tree<E> child : tree.getChildren()) {
      this.add(child);
    }
  }

  default boolean isLeaf() {
    return getChildren() == null || getChildren().isEmpty();
  }

  boolean add(E e);

  boolean add(Tree<E> leaf);

  void clear();

  default boolean addAll(Collection<? extends E> c) {
    boolean addedSome = false;

    for (E e : c) {
      addedSome = this.add(e) || addedSome;
    }

    return addedSome;
  }


  default boolean contains(Object o) {
    if (o == null) {
      return false;
    }

    if (o.equals(getNode())) {
      return true;
    }
    boolean contains = false;

    for (Tree<E> child : getChildren()) {
      contains = contains || child.contains(o);
    }

    return contains;
  }

  /**
   * Sort only the direct children of this branch.
   *
   * @param comparator - the comparator
   */
  void sort(Comparator<E> comparator);


  default boolean containsAll(Collection<?> c) {
    boolean containsAll = true;

    for (Object o : c) {
      containsAll = containsAll && contains(o);
    }

    return containsAll;
  }

  default boolean isEmpty() {
    return isRoot() && isLeaf() && getNode() == null;
  }

  @Override
  default Iterator<E> iterator() {
    return preIterator();
  }


  boolean remove(Object o);

  default boolean removeAll(Collection<?> c) {
    boolean removedSome = false;

    for (Object o : c) {
      removedSome = this.remove(o) || removedSome;
    }

    return removedSome;
  }

  default int size() {
    int size = 1;
    for (Tree<E> child : getChildren()) {
      size += child.size();
    }
    return size;
  }


}
