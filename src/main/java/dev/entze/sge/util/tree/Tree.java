package dev.entze.sge.util.tree;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public interface Tree<E> extends Collection<E> {

  Iterator<E> preIterator();

  Iterator<Tree<E>> preTreeIterator();

  Iterator<E> postIterator();

  Iterator<Tree<E>> postTreeIterator();

  Iterator<E> levelIterator();

  Iterator<Tree<E>> levelTreeIterator();

  E getNode();

  Tree<E> setNode(E e);

  Tree<E> getParent();

  Tree<E> getChild(int index);

  Collection<Tree<E>> getChildren();

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

  default boolean isLeaf() {
    return getChildren() == null || getChildren().isEmpty();
  }

  @Override
  default boolean addAll(Collection<? extends E> c) {
    boolean addedSome = false;

    for (E e : c) {
      addedSome = this.add(e) || addedSome;
    }

    return addedSome;
  }


  @Override
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
   * @param comparator - the comparator
   */
  void sort(Comparator<E> comparator);

  @Override
  default boolean containsAll(Collection<?> c) {
    boolean containsAll = true;

    for (Object o : c) {
      containsAll = containsAll && contains(o);
    }

    return containsAll;
  }

  @Override
  default boolean isEmpty() {
    return isRoot() && isLeaf() && getNode() == null;
  }

  @Override
  default Iterator<E> iterator() {
    return preIterator();
  }

  @Override
  default boolean removeAll(Collection<?> c) {
    boolean removedSome = false;

    for (Object o : c) {
      removedSome = this.remove(o) || removedSome;
    }

    return removedSome;
  }

  @Override
  default int size() {
    int size = 1;
    for (Tree<E> child : getChildren()) {
      size += child.size();
    }
    return size;
  }


  @Override
  default Object[] toArray() {
    Object[] object = new Object[size()];

    int i = 0;
    for (E e : this) {
      object[i++] = e;
    }

    return object;
  }

  static <T> Tree<T> sort(Tree<T> tree, Comparator<T> comparator) {
    tree.postTreeIterator().forEachRemaining(t -> t.sort(comparator));
    return tree;
  }


}
