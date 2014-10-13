/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtypecounter;

/**
 *
 * @author juankboix1309
 */
import java.awt.Color;
import java.util.Comparator;

public class RedBlackTree implements WordSet{
    
    private Comparator comparator;
    protected RedBlackTreeNode root = null;

   
    public RedBlackTree() {
    }



    /**
     * Adds a single data item to the tree. If there is already an item in the
     * tree that compares equal to the item being inserted, it is "overwritten"
     * by the new item. Overrides BinarySearchTree.add because the tree needs to
     * be adjusted after insertion.
     * @param data
     */
     
    public void add(Word data) {
        if (root == null) {
            root = new RedBlackTreeNode(data);
        }
        RedBlackTreeNode n = root;
        while (true) {
            int comparisonResult = compare((Word)data, (Word)n.getData());
            if (comparisonResult == 0) {
                n.setData(data);
                return;
            } else if (comparisonResult < 0) {
                if (n.getLeft() == null) {
                    n.setLeft( new RedBlackTreeNode(data));
                    adjustAfterInsertion( n.getLeft());
                    break;
                }
                n = n.getLeft();
            } else { // comparisonResult > 0
                if (n.getRight() == null) {
                    n.setRight( new RedBlackTreeNode(data));
                    adjustAfterInsertion( n.getRight());
                    break;
                }
                n = n.getRight();
            }
        }
    }

    
    private void adjustAfterInsertion(RedBlackTreeNode n) {
        // Step 1: color the node red
        setColor(n, Color.red);

        // Step 2: Correct double red problems, if they exist
        if (n != null && n != root && isRed(parentOf(n))) {

            // Step 2a (simplest): Recolor, and move up to see if more work
            // needed
            if (isRed(siblingOf(parentOf(n)))) {
                setColor(parentOf(n), Color.black);
                setColor(siblingOf(parentOf(n)), Color.black);
                setColor(grandparentOf(n), Color.red);
                adjustAfterInsertion(grandparentOf(n));
            }

            // Step 2b: Restructure for a parent who is the left child of the
            // grandparent. This will require a single right rotation if n is
            // also
            // a left child, or a left-right rotation otherwise.
            else if (parentOf(n) == leftOf(grandparentOf(n))) {
                if (n == rightOf(parentOf(n))) {
                    rotateLeft(n = parentOf(n));
                }
                setColor(parentOf(n), Color.black);
                setColor(grandparentOf(n), Color.red);
                rotateRight(grandparentOf(n));
            }

            // Step 2c: Restructure for a parent who is the right child of the
            // grandparent. This will require a single left rotation if n is
            // also
            // a right child, or a right-left rotation otherwise.
            else if (parentOf(n) == rightOf(grandparentOf(n))) {
                if (n == leftOf(parentOf(n))) {
                    rotateRight(n = parentOf(n));
                }
                setColor(parentOf(n), Color.black);
                setColor(grandparentOf(n), Color.red);
                rotateLeft(grandparentOf(n));
            }
        }

        // Step 3: Color the root black
        setColor(root, Color.black);
    }

    private Color colorOf(RedBlackTreeNode n) {
        return n == null ? Color.black : n.color;
    }

    private boolean isRed(RedBlackTreeNode n) {
        return n != null && colorOf(n) == Color.red;
    }

    private boolean isBlack(RedBlackTreeNode n) {
        return n == null || colorOf(n) == Color.black;
    }

    private void setColor(RedBlackTreeNode n, Color c) {
        if (n != null)
            n.color = c;
    }

    private RedBlackTreeNode parentOf(RedBlackTreeNode n) {
        return n == null ? null :  n.getParent();
    }

    private RedBlackTreeNode grandparentOf(RedBlackTreeNode n) {
        return (n == null || n.getParent() == null) ? null : n
                .getParent().getParent();
    }

    private RedBlackTreeNode siblingOf(RedBlackTreeNode n) {
        return (n == null || n.getParent() == null) ? null : (n == n
                .getParent().getLeft()) ? n.getParent().getRight()
                :  n.getParent().getLeft();
    }

    private RedBlackTreeNode leftOf(RedBlackTreeNode n) {
        return n == null ? null :  n.getLeft();
    }

    private RedBlackTreeNode rightOf(RedBlackTreeNode n) {
        return n == null ? null :  n.getRight();
    }
    
    protected int compare(Word x, Word y) {
        if (comparator == null) {
            return ((Comparable)x).compareTo(y);
        } else {
            return comparator.compare(x, y);
        }
    }

    // Methods relating to nodes, not part of public interface. NO ES NECESARIO

    /**
     * Returns the root of the tree.
     */
    protected RedBlackTreeNode getRoot() {
        return root;
    }

    /**
     * Makes the given node the new root of the tree.TAMPOCO ES NESESARIO
     * 
     */
    protected void setRoot(RedBlackTreeNode node) {
        if (node != null) {
            node.removeFromParent();
        }
        root = node;
    }

    /**
     * Rotates left around the given node.
     */
    protected void rotateLeft(RedBlackTreeNode n) {
        if (n.getRight() == null) {
            return;
        }
        RedBlackTreeNode oldRight = n.getRight();
        n.setRight(oldRight.getLeft());
        if (n.getParent() == null) {
            root = oldRight;
        } else if (n.getParent().getLeft() == n) {
            n.getParent().setLeft(oldRight);
        } else {
            n.getParent().setRight(oldRight);
        }
        oldRight.setLeft(n);
    }

    /**
     * Rotates right around the given node.
     */
    protected void rotateRight(RedBlackTreeNode n) {
        if (n.getLeft() == null) {
            return;
        }
        RedBlackTreeNode oldLeft = n.getLeft();
        n.setLeft(oldLeft.getRight());
        if (n.getParent() == null) {
            root = oldLeft;
        } else if (n.getParent().getLeft() == n) {
            n.getParent().setLeft(oldLeft);
        } else {
            n.getParent().setRight(oldLeft);
        }
        oldLeft.setRight(n);
    }

    /**
     * Returns the rightmost node in the left subtree. este si
     */
    protected RedBlackTreeNode predecessor(RedBlackTreeNode node) {
        RedBlackTreeNode n = node.getLeft();
        if (n != null) {
            while (n.getRight() != null) {
                n = n.getRight();
            }
        }
        return n;
    }

    /**
     * A special helper method that returns the node containing
     * an object that compares equal to the given object.  This
     * is used in both contains and remove.
     *
     */
    public Word get(Word data)
    {
        for (RedBlackTreeNode n = root; n != null;) {
            int comparisonResult = compare(data, n.getData());
            if (comparisonResult == 0) {
                return n.getData();
            } else if (comparisonResult < 0) {
                n = n.getLeft();
            } else {
                n = n.getRight();
            }
        }
        return null;
        
    }
    protected RedBlackTreeNode nodeContaining(Word data) {
        for (RedBlackTreeNode n = root; n != null;) {
            int comparisonResult = compare(data, n.getData());
            if (comparisonResult == 0) {
                return n;
            } else if (comparisonResult < 0) {
                n = n.getLeft();
            } else {
                n = n.getRight();
            }
        }
        return null;
    }
}