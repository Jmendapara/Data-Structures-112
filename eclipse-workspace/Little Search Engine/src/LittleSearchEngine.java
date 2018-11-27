package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */															
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 

			throws FileNotFoundException {
		HashMap<String, Occurrence> tempmap = new HashMap<String, Occurrence>();
		if (docFile == null)
		{throw new FileNotFoundException();}
		Scanner sc = new Scanner(new File(docFile));
		while (sc.hasNext()){
			String tempkey = getKeyword(sc.next());
			if (tempkey != null){
				if (tempmap.containsKey(tempkey)){
					Occurrence temp = tempmap.get(tempkey);
					temp.frequency++;}
				else{
					Occurrence temp = new Occurrence(docFile, 1);
					tempmap.put(tempkey, temp);}
			}
		}
		return tempmap;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		for (String key : kws.keySet())
		{ArrayList<Occurrence> tempocc = new ArrayList<Occurrence>();
		if (keywordsIndex.containsKey(key))
		{tempocc = keywordsIndex.get(key);}//if its already in the map
		tempocc.add(kws.get(key));
		insertLastOccurrence(tempocc);
		keywordsIndex.put(key, tempocc);
		}
	}

	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		if (word.equals("")){
			return null;}
		if(!Character.isLetter(word.charAt(0))) { //if its not a letter
			return null;}
		char pointer;
		int counter = 0;
		int terminate = 0;
		String thefinal = "";
		String tempstr = "";
		while (counter <=  word.length()-1){//traverse word
			pointer = word.charAt(counter);
			if (Character.isLetter(pointer)){
				if (terminate == 1){
					return null;}
				if(counter == word.length()-1){
					thefinal = word.substring(0, counter+1);}} 	
			else {if (terminate == 0){
				thefinal = word.substring(0, counter);}	
			if (pointer != '?' && pointer != ':'&& pointer != '.' 
					&& pointer != ','  && pointer != ';' && pointer != '!'){
				return null;}
			terminate = 1;}
			counter++;}
		tempstr = thefinal;
		thefinal = "";
		for (int k = 0; k<tempstr.length();k++){ //make everything lowercase
			thefinal = thefinal + Character.toLowerCase(tempstr.charAt(k));}
		if (noiseWords.contains(thefinal)){ //if its a noiseword
			return null;}
		return thefinal;
	}



	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		
		if(occs.size()<=1){
			return null;}
		
		else{
		Occurrence tempocc = occs.get(occs.size()-1);//last
		int mid = 0;
		occs.remove(occs.size()-1);
		int highend = occs.size()-1;
		int lowend = 0;
		int	i = tempocc.frequency;
		
		while(lowend <= highend) {//start binary 
			mid = (highend + lowend) / 2;
			Occurrence temp = occs.get(mid);
			int end = temp.frequency;
			
			if(end == i) {
				arr.add(mid);
				break;}
			
			else if(end < i) {
				highend = mid -1;
				arr.add(mid);}
			
			else if(end > i) {
				lowend = mid + 1;
				arr.add(mid);
				mid = mid+1;}}
		
		occs.add(mid, tempocc);
		return arr;}
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
			throws FileNotFoundException {
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		int counter = 0;
		ArrayList<Occurrence> tempocc = new ArrayList<Occurrence>();
		ArrayList<String> finalarr = new ArrayList<String>();

		if(keywordsIndex.get(kw1) == null && keywordsIndex.get(kw2) == null){ return null;} //if neither exist

		else if (keywordsIndex.get(kw2) == null){ //if the second one doesnt exist
			tempocc.addAll(keywordsIndex.get(kw1));

			while(!tempocc.isEmpty() && counter<5){
				finalarr.add(tempocc.remove(0).document);
				counter++;}
			return finalarr;}

		else if (keywordsIndex.get(kw1) == null){ //if the first one doesnt exist
			tempocc.addAll(keywordsIndex.get(kw2));

			while(!tempocc.isEmpty() && counter<5){
				finalarr.add(tempocc.remove(0).document);
				counter++;}
			return finalarr;}

		tempocc.addAll(keywordsIndex.get(kw2)); //if both exist then do rest
		tempocc.addAll(keywordsIndex.get(kw1));
		int high = -1;//max of five
		int ctr = 0;

		while(counter!=5 && !tempocc.isEmpty()){//max of 5

			while(ctr<tempocc.size() && tempocc.get(ctr) != null){

				if (high == -1){

					if (!finalarr.contains(tempocc.get(ctr).document)){
						high = ctr;}
					ctr++;
					continue;}

				if (tempocc.get(ctr).frequency > tempocc.get(high).frequency){
					if(!finalarr.contains(tempocc.get(ctr).document)){
						high = ctr;}} 

				else if (tempocc.get(ctr).frequency == tempocc.get(high).frequency){

					if(keywordsIndex.get(kw1).contains(tempocc.get(ctr))){

						if(!finalarr.contains(tempocc.get(ctr).document)){
							high = ctr;}
					}
				}
				ctr++;
			}

			if (high != -1)
			{
				finalarr.add(tempocc.remove(high).document);
			}

			ctr=0;
			counter++;
			high = -1;
		}//outter while 

		if (finalarr.isEmpty()){
			return null;
		}

		return finalarr;
	}

}
