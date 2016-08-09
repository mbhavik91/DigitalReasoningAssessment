/** Part 3 of Remote Programming Exercise : WorkerThread**/
package solutions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.ProcessData;
import utility.Sentence;

/*@author Bhavik*/
public class WorkerThread implements Runnable{

	private String filepath;
	
	public WorkerThread(String string) {
		// TODO Auto-generated constructor stub
		filepath = string;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		ProcessData dataObj;
		try {
			dataObj = createProcessDataObject(filepath);
			Solution_3_ThreadPool.listOfProcessDataObjects.add(dataObj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception In Worker Thread");
			e.printStackTrace();
		}
	}

	private ProcessData createProcessDataObject(String filepath2) throws IOException {		
		
		List<String> sentencesFromData;
		
		//Step 1 : Read the input text file into StringBuilder				
		String data = readDataFile(filepath2);
		
		//Step 2 : Read NER data
		List<String> ner = new ArrayList<String>();
		ner = readNERFile(Solution_3_ThreadPool.NER_FILE_PATH);
		
		//Step 3 : Get all the sentences from data		
		sentencesFromData = getSentences(data);
		
		//Step 4: Create object of type Process data
		ProcessData dataObj = getProcessDataObject(sentencesFromData, ner);
		
		return dataObj;
	}

	/**
	 * Processes every sentence to get words from it and creates a user-defined Data Structure 
	 * to hold every identified word into its corresponding sentence.
	 * @param sentencesFromData : List of Individual processed sentences from input text file
	 * @return : A Data Structure which holds every identified word into its corresponding sentence
	 */
	private ProcessData getProcessDataObject(List<String> sentencesFromData, List<String> ner) {
		// TODO Auto-generated method stub
		ProcessData processedDataObj = new ProcessData();
		//List<String> sentences;
		List<String> words ;
		
		for (int j = 0; j < sentencesFromData.size(); j++) {
				Sentence sentObj = new Sentence();
				
				String sentence = sentencesFromData.get(j);
				for (int i = 0; i < ner.size(); i++) {
					String noun = ner.get(i);
					if(isPresent(sentence, noun)){
						System.out.println(ner.get(i));
						Solution_3_ThreadPool.encounteredNamedEntity.add(ner.get(i));
						sentObj.nouns.add(ner.get(i));
					}
				}
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
	 * Checks if a particular named entity(noun) with a pattern is present in s sentence 
	 * @param sentence : Input sentence
	 * @param noun : Named Entity
	 * @return : If exact match with a pattern is found returns true else false
	 */
	private boolean isPresent(String sentence, String noun) {
		// TODO Auto-generated method stub
		sentence = removePunctuations(sentence);
		String pattern = "\\b"+noun+"\\b";
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(sentence);
        return m.find();
	}

	/**
	 *Removes symbols(including punctuation and whitespace) from a given String
	 * @param String input 
	 * @return String without Symbols
	 */
	private String removePunctuations(String sentence) {
		//Regex will remove all the punctuation but it will retain "-". 
		//Second regex will remove all the consecutive "." whose count is greater than 1. 
		if(sentence.length() > 0){
			return sentence.replaceAll("[\\p{Punct}&&[^-]]", "").replaceAll("(\\.)\\1+", "");
		}
		return null;
	}

	/**
	 * Given a text of data this Function identifies sentence boundaries and returns a list of Sentences 
	 * @param data : Text input
	 * @return : List of Sentences 
	 */
	private List<String> getSentences(String data) {
		// TODO Auto-generated method stub
		List<String> sentences = new ArrayList<String>();
		BreakIterator iterator = BreakIterator.getSentenceInstance();
		iterator.setText(data);
		int start = iterator.first();
		for (int end = iterator.next();
				end != BreakIterator.DONE;
				start = end, end = iterator.next()){
					sentences.add(data.substring(start,end));
					//System.out.println(source.substring(start,end));
		}
		return sentences;
	}

	/**
	 * Reads named entity File 
	 * @param nerFilePath : Path of the ner file saved
	 * @return : List of Named ENtities (nouns) present in ner file
	 * @throws IOException
	 */
	private List<String> readNERFile(String nerFilePath) throws IOException {
		// TODO Auto-generated method stub
		List<String> data = new ArrayList<String>();
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(nerFilePath));
		
		while((line = br.readLine()) != null){
			if(!line.trim().isEmpty()){
				data.add(line);							
			}
		}
		br.close();
		return data;
	}

	/**
	 * Reads a given input File as a String 
	 * @param filePath : Path of the input file to be read
	 * @return : String with file data
	 * @throws IOException
	 */
	private String readDataFile(String filepath2) throws IOException {
		// TODO Auto-generated method stub
		StringBuffer data = new StringBuffer();
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(filepath2));
		while((line = br.readLine()) != null){
			if(!line.trim().isEmpty()){
				line = line.replaceAll("[+^,×Ã—]*", "").replaceAll("\n", "");
				data.append(line);
			}
		}
		br.close();
		return data.toString();
	}

}
