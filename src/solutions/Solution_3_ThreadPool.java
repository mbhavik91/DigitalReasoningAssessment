/** Part 3 of Remote Programming Exercise**/

package solutions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import utility.*;

/*@author Bhavik*/
@XmlRootElement(name="root")
public class Solution_3_ThreadPool {

	/**
	 * @param args
	 */
	
	private static final String XML_FILE = "solution3.xml";
	private static final String INPUT_ZIP_FILE = "C:/Users/Bhavik/Desktop/ServicesEngineerAssessment/NLP_test/nlp_data.zip";
    private static final String OUTPUT_FOLDER = "C:/outputzip1";
    public static final String NER_FILE_PATH = "C:\\Users\\Bhavik\\Desktop\\ServicesEngineerAssessment\\NLP_test\\NER.txt";
    
    @XmlElement(name="file")
    public static List<ProcessData> listOfProcessDataObjects = Collections.synchronizedList(new ArrayList<ProcessData>());
    
    public static Set<String> encounteredNamedEntity = Collections.synchronizedSet(new HashSet<String>());
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List<String> listOfFileLocation = new ArrayList<String>();
		
		Solution_3_ThreadPool threadPoolObj = new Solution_3_ThreadPool();
		
		//Step 1 : Unzip the given file
		if(threadPoolObj.unZipFile(INPUT_ZIP_FILE, OUTPUT_FOLDER)){
			System.out.println("Unzipping Complete !");
			
			//Step 2 : Get the path of all the files including sub-folders of OUTPUT_FOLDER/nlp_data			
			listOfFileLocation = threadPoolObj.getListOfFileLocation(new File(OUTPUT_FOLDER+"/nlp_data"));
			
			//Step 3: Start the thread pool and process all the files present			
			if(threadPoolObj.startThreadPool(listOfFileLocation)){
				
				if(threadPoolObj.createXML(threadPoolObj,XML_FILE))
				{
					System.out.println("XML Output created succesfully");
					for (String s : encounteredNamedEntity) {
					    System.out.println(s);
					}
					//System.out.println(encounteredNamedEntity.size());
					System.out.println("All the task finished !");
				}
				
			}

		}
		

	}
	/**
	 * Maps user-defined data structure which holds processed text data into XML and creates an XML file with the output
	 * @param dataObj : Data Structure to be mapped to XML
	 * @param xmlFile : Name of output XML File
	 * @return : true if XML data mapped successfully else returns false
	 */
	private boolean createXML(Solution_3_ThreadPool threadPoolObj, String xmlFile) {
		// TODO Auto-generated method stub
		try{
		String xmlString=null;
		Marshaller m = (JAXBContext.newInstance(Solution_3_ThreadPool.class)).createMarshaller();
		StringWriter sw = new StringWriter();
		m.marshal(threadPoolObj, sw);
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
	 * Represents a ThreadPool which paralelly processeses Text Files with a max of 5 worker Threads at a time  
	 * @param listOfFileLocation : List of location Files to be processed
	 * @return : true if ThreadPool works successfully else returns false   
	 */
	private boolean startThreadPool(List<String> listOfFileLocation) {
		try
		{
		if(listOfFileLocation.size()>0){
			//Thread Pool
			ExecutorService executor = Executors.newFixedThreadPool(5);
			for (int i = 0; i < listOfFileLocation.size(); i++) {
				Runnable worker = new WorkerThread(listOfFileLocation.get(i));
				executor.execute(worker);
			}
			//executor.shutdown();
			executor.shutdown();
			while (!executor.isTerminated()) {
			}		
		}
		return true;
		}
		catch (Exception e) {
			return false;	
		}		
	}

	/**
	 * Parses through a Directory and creates a list of absolute paths of all files lying within that directory
	 * @param file : Input Directory Path 
	 * @return : List of absolute paths of all files to be processed later
	 */
	private List<String> getListOfFileLocation(File file) {
		// TODO Auto-generated method stub
		List<String> filePathList = new ArrayList<String>();
	    for (final File fileEntry : file.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            getListOfFileLocation(fileEntry);
	        } else {
	        	filePathList.add(fileEntry.getAbsolutePath());
	            //System.out.println(fileEntry.getAbsolutePath());
	        }
	    }
	    return filePathList;
	}

	/**
	 * Unzips a zipped input directory to a a local folder of files to be processed     
	 * @param inputZipFile  
	 * @param outputFolder : Directory of files
	 * @return : true if zip file is unzipped successfully else returns false 
	 */
	private boolean unZipFile(String inputZipFile, String outputFolder) {

	     byte[] b = new byte[1024];

	     try{
	    	//create output directory if it not exists
	    	File myFolder = new File(outputFolder);
	    	if(!myFolder.exists()){
	    		myFolder.mkdir();
	    	}
	    	//get the zip file content
	    	ZipInputStream zipInptStream =
	    		new ZipInputStream(new FileInputStream(inputZipFile));
	    	//get the zipped file list entry
	    	ZipEntry zipEntry = zipInptStream.getNextEntry();

	    	while(zipEntry!=null){
	    		
	    		String folderName = null ;
	    		if(zipEntry.isDirectory()) { 
	    			folderName = zipEntry.getName();
	    			File dir = new File(outputFolder+File.separator+folderName);
	    			dir.mkdirs();
	    			zipEntry = zipInptStream.getNextEntry();
	    		}
	    		else
	    		{
	    	   String fileName = zipEntry.getName();
	    	   //System.out.println(fileName);
	    	   File newFile = new File(outputFolder+File.separator+fileName);
	    	   //System.out.println(" OutputFolder : "+outputFolder + "/" + fileName);
	    	   //System.out.println("NewFile "+newFile);
	           //System.out.println("file unzip : "+ newFile.getAbsoluteFile());
	            
	           new File(newFile.getParent()).mkdirs();
	           //create all non exists folders
	           //else you will hit FileNotFoundException for compressed folder
	           //System.out.println("New File Parents "+newFile.getParentFile());
	           
	            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
	            int len;
	            while ((len = zipInptStream.read(b)) > 0) {
	            	fileOutputStream.write(b, 0, len);
	            }
	            fileOutputStream.close();
	            zipEntry = zipInptStream.getNextEntry();
	            }	
	    	}
	    	zipInptStream.closeEntry();
	    	zipInptStream.close();
	    	//System.out.println("Done");
	    }catch(IOException ex){
	    	ex.printStackTrace();
	    	return false;
	    }
	    return true;
	   }

}
