/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtypecounter;

/**
 *
 * @author William
 */
/**
 *  Java Program to Implement SplayTree
 **/
 
 /** Class Node **/
 public class SplayTreeNode
 {    
     SplayTreeNode left, right, parent;
     Word element;
 
     /** Constructor **/
     public SplayTreeNode()
     {
         this(0, null, null, null);
     }          
     /** Constructor **/
     public SplayTreeNode(Word ele)
     {
         this(ele, null, null, null);
     } 
     /** Constructor **/
     public SplayTreeNode(Word ele, SplayTreeNode left, SplayTreeNode right, SplayTreeNode parent)
     {
         this.left = left;
         this.right = right;
         this.parent = parent;
         this.element = ele;         
     }    
 }

