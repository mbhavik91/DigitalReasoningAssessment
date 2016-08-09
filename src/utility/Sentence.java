/**
 * 
 */
package utility;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Bhavik
 *
 */
public class Sentence {
	@XmlElement(name="word")
	public List<String> words = new ArrayList<String>();
	@XmlElement(name="noun")
	public List<String> nouns = new ArrayList<String>();



}
