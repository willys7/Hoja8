/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtypecounter;

/**
 *
 * @author William
 */

import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author juankboix1309
 */
public class tree implements WordSet
{
    TreeMap tree = new TreeMap();
    
    public void add(Word wordObject)
    {
        tree.put(wordObject, wordObject);
    }
    public Word get(Word word)
    {
         Object valor = tree.get(word);
         return (Word) valor;
        
    }
    
}
