/**
 * 
 */
package utility;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Bhavik
 *
 */
@XmlRootElement(name="root")
public class ProcessData {
	
	@XmlElement(name="sentence")
	public List<Sentence> sentences = new ArrayList<Sentence>();

}
