/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtypecounter;

/**
 *
 * @author William
 */
import java.util.*;

/**
 * An implementation of splay trees.
 * <p>
 * Uses the element type's "natural ordering" to compare elements.
 * <p>
 * The code is optimised for clarity rather than efficiency.
 */
public class SplayTree<A> implements WordSet{

  ////////////////////////////////////////////////////////////////////
  // The basics: Tree nodes and splaying
  ////////////////////////////////////////////////////////////////////

  /**
   * Tree nodes.
   * <p>
   * The parent and child links are private to ensure that when a
   * child link is updated, the parent link of the child (if any) is
   * updated at the same time.
   */
  private class Node {
    /** The parent node. Null for the topmost sentinel node. */
    private Node parent;

    /** The left subtree. Null if the subtree is empty. */
    private Node left;

    /** The right subtree. Null if the subtree is empty. */
    private Node right;

    /** The node contents. Only null for the sentinel node. */
    public Word contents;

    /**
     * Creates a node. Sets all parent/child pointers to null.
     */
    public Node(Word contents) {
      this.contents = contents;
    }

    /**
     * Sets the left child.
     *
     * @param child The new left child. Can be null.
     */
    public void setLeft(Node child) {
      left = child;
      if (child != null) {
        child.parent = this;
      }
    }

    /**
     * Sets the right child.
     *
     * @param child The new right child. Can be null.
     */
    public void setRight(Node child) {
      right = child;
      if (child != null) {
        child.parent = this;
      }
    }

    /**
     * Returns the parent.
     */
    public Node parent() {
        return parent;
    }

    /**
     * Returns the left child.
     */
    public Node left() {
        return left;
    }

    /**
     * Returns the right child.
     */
    public Node right() {
        return right;
    }
  }

  /**
   * A sentinel node whose right child is null and whose left child
   * points to the tree's root (which is null if the tree is empty).
   */
  private Node sentinel;

  /**
   * Creates an empty tree.
   */
  public SplayTree() {
    sentinel = new Node(null);
  }

  /**
   * Rotates the given node past its parent.
   *
   * @param child The node. Must be non-null, and must have a parent
   *              which is a proper tree node (not the sentinel).
   */
  private void rotateUp(Node child) {
    assert child != null &&
           child.parent() != null &&
           child.parent().parent() != null;

    Node parent      = child.parent();
    Node grandparent = parent.parent();

    // Move child below grandparent (which could be the sentinel).
    if (grandparent.left() == parent) {
      grandparent.setLeft(child);
    } else {
      grandparent.setRight(child);
    }

    // Move parent below child, and one of child's children below
    // parent.
    if (parent.left() == child) {
      parent.setLeft(child.right());
      child.setRight(parent);
    } else {
      parent.setRight(child.left());
      child.setLeft(parent);
    }
  }

  /**
   * Splays the tree.
   *
   * @param toTop The node which should be splayed to the top. Must be
   *              a non-null tree member, or the sentinel, in which
   *              case no rotations are performed.
   */
  private void splay(Node toTop) {
    assert toTop != null;

    if (toTop == sentinel) {
      return;
    }

    while (toTop.parent() != sentinel) {
      Node parent      = toTop.parent();
      Node grandparent = parent.parent();

      if (grandparent == sentinel) {
        // Zig or zag.
        rotateUp(toTop);
      } else {
        if (grandparent.left()  == parent && parent.left()  == toTop
              ||
            grandparent.right() == parent && parent.right() == toTop) {
          // Zig-zig or zag-zag.
          rotateUp(parent);
          rotateUp(toTop);
        } else {
          // Zig-zag or zag-zig.
          rotateUp(toTop);
          rotateUp(toTop);
        }
      }
    }
  }

  ////////////////////////////////////////////////////////////////////
  // Searching for a given node/element
  ////////////////////////////////////////////////////////////////////

  /**
   * The result of searching for a given node using
   * <code>findMatch</code>.
   */
  private class Match {
    /**
     * Did the search find a matching node?
     */
    public boolean matchFound;

    /**
     * In case of a match this is the matching node, otherwise it
     * is the last accessed node (or the sentinel if the tree is
     * empty).
     */
    public Node node;

    /**
     * Is the thing searched for smaller than
     * <code>node.contents</code>? (This field is used to avoid an
     * extra comparison in <code>insert</code>.) Special case: If
     * <code>node</code> is the sentinel, then this field is set to
     * <code>true</code>.
     */
    public boolean smallerThanNode;

    /**
     * Creates a match. The <code>Node</code> must be non-null.
     */
    public Match
      (boolean matchFound, Node node, boolean smallerThanNode) {
      assert node != null;
      assert node != sentinel || smallerThanNode == true;

      this.matchFound      = matchFound;
      this.node            = node;
      this.smallerThanNode = smallerThanNode;
    }
  }

  /**
   * Finds the node containing the given element, if any. <i>Does
   * not splay the tree.</i>
   *
   * @param a The element, which must be non-null.
   * @return  A <code>Match</code>.
   */
  private Match findMatch(Word a) {
    assert a != null;

    // The search starts in the root, not the sentinel.
    Node n = sentinel.left();

    if (n == null) {
      return new Match(false, sentinel, true);
    }

    // This method is implemented iteratively rather than recursively
    // to avoid stack overflow. (Java does not require that tail-calls
    // are optimised.)
    while (true) {
      int c = a.compareTo(n.contents);
      if (c == 0) {
        return new Match(true, n, false);
      } else if (c < 0) {
        if (n.left()  == null) return new Match(false, n, true);
        else                   n = n.left();
      } else {
        if (n.right() == null) return new Match(false, n, false);
        else                   n = n.right();
      }
    }
  }

  /**
   * Finds the node containing the given element, if any.
   *
   * @param a The element, which must be non-null.
   * @return  The matching node, if any, otherwise null.
   */
  private Node find(Word a) {
    assert a != null;

    Match m = findMatch(a);
    splay(m.node);
    return m.matchFound ? m.node : null;
  }

  /**
   * Checks if a given element is a member of the tree.
   *
   * @param a The element, which must be non-null.
   */
  public boolean member(Word a) {
    assert a != null;

    return (find(a) != null);
  }

  ////////////////////////////////////////////////////////////////////
  // Insertion
  ////////////////////////////////////////////////////////////////////

  /**
   * Inserts the given element into the tree. If an equal element
   * already exists in the tree, then it is replaced.
   *
   * @param a The element, which must be non-null.
   */
  @Override
  public void add(Word a) {
    assert a != null;

    Match m = findMatch(a);

    if (m.matchFound) {
      // The element already exists in the tree.
      m.node.contents = a;

      splay(m.node);
    } else {
      Node parent  = m.node;
      Node newNode = new Node(a);

      if (m.smallerThanNode) {
        parent.setLeft(newNode);
      } else {
        parent.setRight(newNode);
      }

      splay(newNode);
    }
  }

  ////////////////////////////////////////////////////////////////////
  // Finding/deleting minimum elements
  ////////////////////////////////////////////////////////////////////

  /**
   * Returns the node containing the tree's minimum element.
   *
   * @return The node containing the minimum element.
   * @throws NoSuchElementException if the tree is empty.
   */
  private Node findMinNode() {
    Node n = sentinel.left();

    if (n == null) {
      throw new NoSuchElementException("empty tree");
    }

    while (n.left() != null) {
      n = n.left();
    }

    splay(n);

    return n;
  }

  /**
   * Returns the tree's minimum element.
   *
   * @return The minimum element.
   * @throws NoSuchElementException if the tree is empty.
   */
  public Word findMin() {
    return findMinNode().contents;
  }

  /**
   * Removes and returns the tree's minimum element.
   *
   * @return The minimum element.
   * @throws NoSuchElementException if the tree is empty.
   */
  public Word deleteMin() {
    Node minimum = findMinNode();

    // Delete the node, which does not have a left child, and is now
    // located at the top of the tree.
    sentinel.setLeft(minimum.right());

    return minimum.contents;
  }

  ////////////////////////////////////////////////////////////////////
  // Deleting a given element
  ////////////////////////////////////////////////////////////////////

  /**
   * Removes and returns the given element from the tree.
   *
   * @param a The given element, which must be non-null.
   * @return  The removed element.
   * @throws  NoSuchElementException if the element is not present in
   *          the tree.
   */
  @Override
  public Word get(Word a) {
    
    assert a != null;

    Node n = find(a);

    if (n == null) {
      return null; 
    } else {
      // Due to the splaying n is at the top of the tree.
      if (n.right() == null) {
        // Remove n from the tree.
        sentinel.setLeft(n.left());
      } else {
        // Remove n and the left subtree from the tree.
        sentinel.setLeft(n.right());

        // The right subtree of n is non-empty (because n.right is
        // non-null), so if we run findMin, then the former right
        // subtree's minimum element will be the root of the tree.
        findMin();

        // The minimum element does not have a left child, so we can
        // easily reinsert n's left subtree.
        sentinel.left().setLeft(n.left());
      }

      return n.contents;
    }
  }

}