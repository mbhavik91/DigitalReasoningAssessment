# ServicesEngineerAssessment
##Digital reasoning


Assumptions :

1. The first task asked to process Symbols(including punctuation and whitespace). I have written a function which removes 
punctuation and whitespace and other symbols using a regex expression . The words could have been saved with symbols but 
I decided to remove them before processing the text files as I thought what if this code have a further scope in NLP and 
there is some processing or handling of individual words . So retrieved all words as a list of each individual word (without any 
symbol involved).

2. The decision about choosing a Data Structure was based on how I would visualize the text file processed and split and 
represented. So I had a simple user-defined structure which would define the text file as List of sentences; which itself 
will be a List of words and nouns. So the structure representing each file will look like 
FileData :{
	
			List ( List<Word> && List<Noun> ) Sentences ();
		  }
which maps to an XML Data as
	    <sentence>
		<word></word>..
		<noun></noun>..
		</sentence>
Note: List of Words also include nouns : This I thought important to keep it this way so that what if I need to 
retrieve entire list of words of a sentence. This had to be separated from other data / information about the sentence 
which can hold nouns (List<nouns>) or say adjectives (List<adjecttive>) or verbs(List<verb>) and so on. This structure 
will represent every aspect/information about the sentence independently . 
