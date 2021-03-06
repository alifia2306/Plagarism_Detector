package edu.upenn.cis573.plagiarism;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/*
 * This class implements a simple plagiarism detection algorithm.
 * 
 * Your goal is to improve its execution time by identifying three major inefficiencies
 * (each of which will result in at least a 10% reduction in execution time compared to the
 * original), as well as at least seven minor inefficiencies.
 */


public class PlagiarismDetector {
	
	private String dirName; // the name of the directory containing the corpus
	HashMap<String,Set<String>> phraseMap;
	public PlagiarismDetector(String name) {
		dirName = name;
		phraseMap =  new HashMap<String, Set<String>>();
	}
	
	 
	
	
	/*
	 * This method reads the given file and then converts it into a List of Strings.
	 * It does not include punctuation and converts all words in the file to uppercase.
	 */
	private List<String> readFile(String filename) {

		if (filename == null) return null;
		
		List<String> words = new ArrayList<String>();
		
		try {
			Scanner in = new Scanner(new File(filename));
			while (in.hasNext()) {
				words.add(in.next().replaceAll("[^a-zA-Z]", "").toUpperCase());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return words;
	}


	
	/*
	 * This method reads a file and converts it into a Set/List of distinct phrases,
	 * each of size "window". The Strings in each phrase are whitespace-separated.
	 */
	private Set<String> createPhrases(String filename, int window) {
		if (filename == null || window < 1) return null;
		
		List<String> words = readFile(filename);
		
		Set<String> phrases = new HashSet<String>();
		
		for (int i = 0; i < words.size() - window + 1; i++) {
			String phrase = "";
			for (int j = 0; j < window; j++) {
				phrase += words.get(i+j) + " ";
			}

			if (phrases.contains(phrase) == false) 
				phrases.add(phrase);

		}
		
		return phrases;
	}

	

	/*
	 * Returns a Map (sorted by the value of the Integer, in non-descending order) indicating
	 * the number of matches of phrases of size windowSize or greater between each document in the corpus
	 * 
	 * Note that you may NOT remove this method or change its signature or specification!
	 */
	public Map<String, Integer> detectPlagiarism(int windowSize, int threshold) {
		File dirFile = new File(dirName);
		String[] files = dirFile.list();
		
		Map<Integer, ArrayList<String>> numberOfMatches = new TreeMap<Integer, ArrayList<String>>();
		Map<String, Integer> matchesList = new HashMap<String, Integer>();
		
		for (int i = 0; i < files.length; i++) {
			String file1 = files[i];

			for (int j = 0; j < files.length; j++) { 
				
//				if(i == j){
//					continue;
//				}
				
				String file2 = files[j];
				
//				if (numberOfMatches.containsKey(file2 + "-" + file1) || numberOfMatches.containsKey(file1 + "-" + file2)) {
//					continue;
//				}
				
				Set<String> file1Phrases = createPhrases(dirName + "/" + file1, windowSize); ; 
				Set<String> file2Phrases = createPhrases(dirName + "/" + file2, windowSize); ; 
//				if(phraseMap.containsKey(file1)){
//					file1Phrases = phraseMap.get(file1);
//				}
//				else{
//					file1Phrases = createPhrases(dirName + "/" + file1, windowSize); 
//					phraseMap.put(file1, file1Phrases);
//				}
//				
//				if(phraseMap.containsKey(file2)){
//					file2Phrases = phraseMap.get(file2);
//				}
//				else{
//					file2Phrases = createPhrases(dirName + "/" + file2, windowSize); 
//					phraseMap.put(file2, file2Phrases);
//				}
				 
				
				if (file1Phrases == null || file2Phrases == null)
					return null;
				
				Set<String> matches = findMatches(file1Phrases, file2Phrases);
				
				if (matches == null)
					return null;
								
				if (matches.size() > threshold) {
					String key = file1 + "-" + file2;
					if (matchesList.containsKey(file2 + "-" + file1) == false && file1.equals(file2) == false) {
						if(numberOfMatches.containsKey(matches.size())){
							numberOfMatches.get(matches.size()).add(key);
						}
						else{
							ArrayList<String> list = new ArrayList<String>();
							list.add(key);
							numberOfMatches.put(matches.size(),list);
						}
						matchesList.put(key, matches.size());
					}
				}				
			}
			
		}		
		
		return sortResults(numberOfMatches);
	}
	
	/*
	 * Returns a Set of Strings that occur in both of the Set parameters.
	 * However, the comparison is case-insensitive.
	 */
	private Set<String> findMatches(Set<String> myPhrases, Set<String> yourPhrases) {
	
		Set<String> matches = new HashSet<String>();
		
		if (myPhrases != null && yourPhrases != null) {
		
			for (String mine : myPhrases) {
				for (String yours : yourPhrases) {
					if (mine.equalsIgnoreCase(yours)) {
						matches.add(mine);
					}
				}
			}
		}
		return matches;
	}
	
	/*
	 * Returns a LinkedHashMap in which the elements of the Map parameter
	 * are sorted according to the value of the Integer, in non-ascending order.
	 */
	public LinkedHashMap<String, Integer> sortResults(Map<Integer, ArrayList<String>> possibleMatches) {
		
		// Because this approach modifies the Map as a side effect of printing 
		// the results, it is necessary to make a copy of the original Map
		Map<String, Integer> copy = new TreeMap<String, Integer>();
		LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();
		for(Entry<Integer, ArrayList<String>> entry : possibleMatches.entrySet()) {
			int maxValue = entry.getKey();
			List<String> maxKeyList = entry.getValue();
			for(String s: maxKeyList){
				list.put(s, maxValue);
			}
		}
//		for (String key : possibleMatches.keySet()) {
//			copy.put(key, possibleMatches.get(key));
//		}	
//		
		
//
//		for (int i = 0; i < copy.size(); i++) {
//			int maxValue = 0;
//			String maxKey = null;
//			for (String key : copy.keySet()) {
//				if (copy.get(key) > maxValue) {
//					maxValue = copy.get(key);
//					maxKey = key;
//				}
//			}
//			
//			list.put(maxKey, maxValue);
//			
//			if (copy.containsKey(maxKey)) {
//				copy.put(maxKey, -1);
//			}
//		}
		
		return list;
	}

}
