package at.ac.tuwien.ifs.sge.util.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

public class DoubleLinkedTree<E> implements Tree<E> {

  private DoubleLinkedTree<E> parent = null;
  private E node = null;
  private List<DoubleLinkedTree<E>> children;

  public DoubleLinkedTree() {
    parent = null;
    node = null;
    children = new ArrayList<>();
  }

  public DoubleLinkedTree(E elem) {
    parent = null;
    node = elem;
    children = new ArrayList<>();
  }

  public DoubleLinkedTree(Tree<E> tree) {
    this.setParent(tree.getParent());
    node = tree.getNode();
    children = new ArrayList<>();
    for (Tree<E> child : tree.getChildren()) {
      if (child instanceof DoubleLinkedTree) {
        children.add((DoubleLinkedTree<E>) child);
      } else {
        children.add(new DoubleLinkedTree<>(child));
      }
    }
  }

  @Override
  public Iterator<E> preIterator() {
    Deque<DoubleLinkedTree<E>> outerStack = new ArrayDeque<>();
    if (node != null) {
      outerStack.add(this);
    }

    return new Iterator<E>() {

      Deque<DoubleLinkedTree<E>> stack = new ArrayDeque<>(outerStack);
      DoubleLinkedTree<E> current;

      @Override
      public boolean hasNext() {
        return !stack.isEmpty();
      }

      @Override
      public E next() {
        current = stack.pop();

        for (int i = current.children.size() - 1; i >= 0; i--) {
          stack.push(current);
        }

        return current.getNode();
      }
    };
  }

  @Override
  public Iterator<Tree<E>> preTreeIterator() {
    Deque<DoubleLinkedTree<E>> outerStack = new ArrayDeque<>();
    if (node != null) {
      outerStack.add(this);
    }

    return new Iterator<Tree<E>>() {

      Deque<DoubleLinkedTree<E>> stack = new ArrayDeque<>(outerStack);
      DoubleLinkedTree<E> current;

      @Override
      public boolean hasNext() {
        return !stack.isEmpty();
      }

      @Override
      public Tree<E> next() {
        current = stack.pop();

        for (int i = current.children.size() - 1; i >= 0; i--) {
          stack.push(current);
        }

        return current;
      }
    };
  }

  @Override
  public Iterator<E> postIterator() {

    Deque<DoubleLinkedTree<E>> outerStack = new ArrayDeque<>();
    outerStack.add(this);

    return new Iterator<E>() {

      Deque<DoubleLinkedTree<E>> stack = new ArrayDeque<>(outerStack);
      DoubleLinkedTree<E> current = null;
      DoubleLinkedTree<E> lastParent = null;
      E next;

      @Override
      public boolean hasNext() {
        return !stack.isEmpty() || (current != null && current.node != null);
      }

      @Override
      public E next() {
        while (!stack.isEmpty()) {
          current = stack.peek();
          if (current == lastParent || current.isLeaf()) {
            next = current.node;
            stack.pop();
            lastParent = current.parent;
            break;
          } else {
            for (int i = children.size() - 1; i >= 0; i--) {
              stack.push(children.get(i));
            }
          }
        }
        current = null;
        return next;
      }
    };
  }

  @Override
  public Iterator<Tree<E>> postTreeIterator() {

    Deque<DoubleLinkedTree<E>> outerStack = new ArrayDeque<>();
    outerStack.add(this);

    return new Iterator<Tree<E>>() {

      Deque<DoubleLinkedTree<E>> stack = new ArrayDeque<>(outerStack);
      DoubleLinkedTree<E> current = null;
      DoubleLinkedTree<E> lastParent = null;

      @Override
      public boolean hasNext() {
        return !stack.isEmpty() || (current != null);
      }

      @Override
      public Tree<E> next() {
        while (!stack.isEmpty()) {
          current = stack.peek();
          if (current == lastParent || current.isLeaf()) {
            Tree<E> toReturn = current;
            current = null;
            stack.pop();
            lastParent = current.parent;
            return toReturn;
          } else {
            for (int i = children.size() - 1; i >= 0; i--) {
              stack.push(children.get(i));
            }
          }
        }
        return null;
      }
    };
  }

  @Override
  public Iterator<E> levelIterator() {

    Deque<DoubleLinkedTree<E>> outerQueue = new ArrayDeque<>();
    outerQueue.add(this);

    return new Iterator<E>() {

      Deque<DoubleLinkedTree<E>> queue = new ArrayDeque<>(outerQueue);
      DoubleLinkedTree<E> current;

      @Override
      public boolean hasNext() {
        return !queue.isEmpty();
      }

      @Override
      public E next() {
        current = queue.remove();
        queue.addAll(current.children);
        return current.node;
      }
    };
  }

  @Override
  public Iterator<Tree<E>> levelTreeIterator() {

    Deque<DoubleLinkedTree<E>> outerQueue = new ArrayDeque<>();
    outerQueue.add(this);

    return new Iterator<Tree<E>>() {

      Deque<DoubleLinkedTree<E>> queue = new ArrayDeque<>(outerQueue);
      DoubleLinkedTree<E> current;

      @Override
      public boolean hasNext() {
        return !queue.isEmpty();
      }

      @Override
      public Tree<E> next() {
        current = queue.remove();
        queue.addAll(current.children);
        return current;
      }
    };
  }


  @Override
  public E getNode() {
    return node;
  }

  @Override
  public Tree<E> setNode(E e) {
    node = e;
    return this;
  }

  @Override
  public Tree<E> getParent() {
    return parent;
  }

  @Override
  public void setParent(Tree<E> parent) {
    if (parent instanceof DoubleLinkedTree) {
      this.parent = (DoubleLinkedTree<E>) parent;
    } else {
      this.parent = new DoubleLinkedTree<>(parent);
    }
  }

  @Override
  public Tree<E> getChild(int index) {
    return children.get(index);
  }

  @Override
  public List<Tree<E>> getChildren() {
    return new ArrayList<>(children);
  }

  @Override
  public void dropParent() {
    parent = null;
  }

  @Override
  public void dropChild(int n) {
    children.remove(n);
  }

  @Override
  public void dropChildren() {
    children.clear();
  }

  @Override
  public boolean add(E e) {
    return add(new DoubleLinkedTree<>(e));
  }

  public boolean add(Tree<E> leaf) {
    if (leaf instanceof DoubleLinkedTree) {
      DoubleLinkedTree<E> doubleLinkedTree = (DoubleLinkedTree<E>) leaf;
      doubleLinkedTree.parent = this;
      return children.add(doubleLinkedTree);
    }

    leaf.setParent(this);
    return children.add(new DoubleLinkedTree<>(leaf));
  }


  @Override
  public void clear() {
    parent = null;
    node = null;
    children.clear();
  }

  @Override
  public Iterator<E> iterator() {
    return preIterator();
  }

  @Override
  public boolean remove(Object o) {
    if (o == null) {
      return false;
    }

    if (o.equals(node)) {
      removeBranch();
      return true;
    }

    boolean removed = false;
    for (DoubleLinkedTree<E> child : children) {
      removed = removed || child.remove(o);
    }

    return removed;
  }

  @Override
  public void sort(Comparator<E> comparator) {
    children.sort((o1, o2) -> comparator.compare(o1.node, o2.node));
  }

  @Override
  public int size() {
    int size = 0;
    if (node != null) {
      size = 1;
    }
    for (DoubleLinkedTree<E> child : children) {
      size += child.size();
    }
    return size;
  }

  public boolean removeBranch() {
    boolean removed = false;
    for (DoubleLinkedTree<E> child : children) {
      child.parent = this.parent;
      removed = true;
    }
    return removed;
  }

}
