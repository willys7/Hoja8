/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordtypecounter;

/**
 *
 * @author William
 */
import java.util.ArrayList;

class SimpleSet implements WordSet {
	private ArrayList<Word> base;
	
	public SimpleSet()
	{
		base = new ArrayList<Word>();
	}
	
	public Word get(Word word)
	{
		int index = base.indexOf(word);
		if(index == -1) return null;
		return base.get(index);
	}
	
	public void add(Word wordObject)
	{
		base.add(wordObject);
	}
}
