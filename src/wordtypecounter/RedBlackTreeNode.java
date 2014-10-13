/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtypecounter;

import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juankboix1309
 */
/**
 * An implementation of the BinaryTreeNode interface in which
 * each node stores direct links to its left child, its right
 * child, and its parent.
 *
 * <p>RedBlackTreeNode objects are pretty mean: if one tries
 * to mix them up with different kinds of binary tree nodes,
 * and exception may be thrown.</p>
 */
public class RedBlackTreeNode{
    Word data;
    Color color = Color.black;
    RedBlackTreeNode parent;
    RedBlackTreeNode left;
    RedBlackTreeNode right;

    /**
     * Constructs a node as the root of its own one-element tree.
     * This is the only public constructor.  The only trees that
     * clients can make directly are simple one-element trees.
     */
    public RedBlackTreeNode(Word data) {
        this.data = data;
    }

    /**
     * Returns the data stored in this node.
     */
    public Word getData() {
        return data;
    }

    /**
     * Modifies the data stored in this node.
     */
    public void setData(Word data) {
        this.data = data;
    }

    /**
     * Returns the parent of this node, or null if this node is a root.
     */
    public RedBlackTreeNode getParent() {
      return parent;
    }

    /**
     * Returns the left child of this node, or null if it does
     * not have one.
     */
    public RedBlackTreeNode getLeft() {
      return left;
    }

    /**
     * Removes child from its current parent and inserts it as the
     * left child of this node.  If this node already has a left
     * child it is removed.
     * @exception IllegalArgumentException if the child is
     * an ancestor of this node, since that would make
     * a cycle in the tree.
     */
    public void setLeft(RedBlackTreeNode child) {
        // Ensure the child is not an ancestor.
        for (RedBlackTreeNode n = this; n != null; n = n.parent) {
            if (n == child) {
                throw new IllegalArgumentException();
            }
        }

        // Ensure that the child is an instance of RedBlackTreeNode.
        RedBlackTreeNode childNode = (RedBlackTreeNode)child;

        // Break old links, then reconnect properly.
        if (this.left != null) {
            left.parent = null;
        }
        if (childNode != null) {
            childNode.removeFromParent();
            childNode.parent = this;
        }
        this.left = childNode;
    }

    /**
     * Returns the right child of this node, or null if it does
     * not have one.
     */
    public RedBlackTreeNode getRight() {
      return right;
    }

    /**
     * Removes child from its current parent and inserts it as the
     * right child of this node.  If this node already has a right
     * child it is removed.
     * @exception IllegalArgumentException if the child is
     * an ancestor of this node, since that would make
     * a cycle in the tree.
     */
    public void setRight(RedBlackTreeNode child) {
        // Ensure the child is not an ancestor.
        for (RedBlackTreeNode n = this; n != null; n = n.parent) {
            if (n == child) {
                throw new IllegalArgumentException();
            }
        }

        // Ensure that the child is an instance of RedBlackTreeNode.
        RedBlackTreeNode childNode = (RedBlackTreeNode)child;

        // Break old links, then reconnect properly.
        if (right != null) {
            right.parent = null;
        }
        if (childNode != null) {
            childNode.removeFromParent();
            childNode.parent = this;
        }
        this.right = childNode;
    }

    /**
     * Removes this node, and all its descendants, from whatever
     * tree it is in.  Does nothing if this node is a root.
     */
    public void removeFromParent() {
        if (parent != null) {
            if (parent.left == this) {
                parent.left = null;
            } else if (parent.right == this) {
                parent.right = null;
            }
            this.parent = null;
        }
    }

}
