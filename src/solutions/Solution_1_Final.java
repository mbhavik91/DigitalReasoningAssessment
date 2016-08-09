/** Part 1 of Remote Programming Exercise**/
package solutions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import utility.ProcessData;
import utility.Sentence;

/**@author Bhavik**/

public class Solution_1_Final {

	/**
	 * @param args
	 */
	//Add the local File Path  
	private static final String FILE_PATH = "C:\\Users\\Bhavik\\Desktop\\ServicesEngineerAssessment\\NLP_test\\nlp_data.txt";
	private static final String XML_FILE = "solution1.xml";
	
	public static void main(String[] args) throws IOException {		
		
		List<String> sentencesFromData;
		
		//Step 1 : Read the input text file into StringBuilder						
		String data = readDataFile(FILE_PATH);		
		
		//Step 2 : Identify Sentence Boundaries		
		sentencesFromData = getSentences(data);
		
		//Step 3: Removes Symbols and tokenizes text into words		
		ProcessData dataObj = getProcessDataObject(sentencesFromData);
		
		//Step 4 : Create XML data of output
		if(createXML(dataObj,XML_FILE))
		{
			System.out.println("Task Completed");
		}	
	}

	/**
	 * Maps user-defined data structure which holds processed text data into XML and creates an XML file with the output
	 * @param dataObj : Data Structure to be mapped to XML
	 * @param xmlFile : Name of output XML File
	 * @return : true if XML data mapped successfully else returns false
	 */
	private static boolean createXML(ProcessData dataObj, String xmlFile) {
		try{
		String xmlString=null;
		Marshaller m = (JAXBContext.newInstance(ProcessData.class)).createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(dataObj, sw);
		xmlString = sw.toString();
		
		PrintWriter writer = new PrintWriter(xmlFile);
		writer.println(xmlString);
		writer.close();
		return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}

	/**
	 * Processes every sentence to get words from it and creates a user-defined Data Structure 
	 * to hold every identified word into its corresponding sentence.
	 * @param sentencesFromData : List of Individual processed sentences from input text file
	 * @return : A Data Structure which holds every identified word into its corresponding sentence
	 */
	private static ProcessData getProcessDataObject(List<String> sentencesFromData) {
		// TODO Auto-generated method stub
		ProcessData processedDataObj = new ProcessData();
		//List<String> sentences;
		List<String> words ;
		
		for (int j = 0; j < sentencesFromData.size(); j++) {
				Sentence sentObj = new Sentence();
				words = new ArrayList<String>();
				words.addAll(Arrays.asList(sentencesFromData.get(j).trim().replaceAll("(\\.)\\1+", "").split("\\s+")));
				
				for (int j2 = 0; j2 < words.size(); j2++) {					
					sentObj.words.add(removePunctuations(words.get(j2)));						
				}
			processedDataObj.sentences.add(sentObj);	
		}
		return processedDataObj;				
	}

	/**
	 *Removes symbols(including punctuation and whitespace) from a given String
	 * @param String input 
	 * @return String without Symbols
	 */
	private static String removePunctuations(String string) {
		//Regex will remove all the punctuation but it will retain "-". 
		//Second regex will remove all the consecutive "." whose count is greater than 1. 
		if(string.length() > 0){
			return string.replaceAll("[\\p{Punct}&&[^-]]", "").replaceAll("(\\.)\\1+", "");
		}
		return null;
	}

	/**
	 * Given a text of data this Function identifies sentence boundaries and returns a list of Sentences 
	 * @param data : Text input
	 * @return : List of Sentences 
	 */
	private static List<String> getSentences(String data) {		
		List<String> sentences = new ArrayList<String>();
		BreakIterator iterator = BreakIterator.getSentenceInstance();
		iterator.setText(data);
		int start = iterator.first();
		for (int end = iterator.next();
				end != BreakIterator.DONE;
				start = end, end = iterator.next()){
					sentences.add(data.substring(start,end));
		}
		return sentences;
	}

	/**
	 * Reads a given input File as a String 
	 * @param filePath : Path of the input file to be read
	 * @return : String with file data
	 * @throws IOException
	 */
	private static String readDataFile(String filePath) throws IOException {
		// TODO Auto-generated method stub
		StringBuffer data = new StringBuffer();
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		while((line = br.readLine()) != null){
			if(!line.trim().isEmpty()){
				data.append(line);
			}
		}
		br.close();
		return data.toString();
	}

}
