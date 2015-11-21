package edu.upenn.cis573.plagiarism;
import java.util.Map;


public class Main {
	
	/*
	 * DO NOT CHANGE THIS CODE!
	 */
	
	public static void main(String[] args) {
		System.out.println("Starting plagiarism detector");

		PlagiarismDetector pd = new PlagiarismDetector("docs");

		long start = System.currentTimeMillis();
		Map<String, Integer> list = pd.detectPlagiarism(4, 4);
		long end = System.currentTimeMillis();

		// this is just here to make sure the results are still the same
		for (String key : list.keySet()) {
			System.out.println(key + ": " + list.get(key));
		}
		
		System.out.println("----------------");
		long time = end - start;
		System.out.println("Took " + time + "ms");
		
	}

	
}
