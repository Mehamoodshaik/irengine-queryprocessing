package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Implementer {

	private String documentNumber;
	private String documentText;
	private int append;
	private HashMap<String, String> documents;
	
	
	

	public HashMap readDocuments() {
	   documentText="";
	   documents = new HashMap<>();
	   try {
	       for (int i = 1; i <= 15; i++) {
	           String path = "/ft911/ft911_"+i;
	           File file = new File(path);
	           Scanner content = new Scanner(file);
	           while (content.hasNextLine()) {
	               String line = content.nextLine();
	               if (line.startsWith("<DOCNO>") && line.endsWith("</DOCNO>"))
	                   documentNumber = line.substring(line.indexOf(">")+1, line.lastIndexOf("<"));
	               if (line.equalsIgnoreCase("</TEXT>")) {
	                   documents.put(documentNumber, documentText);
	                   append = 0;
	                   documentText = "";
	               }
	               if(append == 1)
	                   documentText = documentText+" "+line;
	               if (line.equalsIgnoreCase("<TEXT>"))
	                   append = 1;
	           }
	       }
	   } catch (FileNotFoundException exception) {
	       System.out.println("An error occurred\n" + exception.getMessage());
	   }
	   return documents;
	}


    

	public Scanner extractQueries(){
	   Scanner fileContent = null;
	   try {
	       String queryPath = "topics.txt";
	       File queryFile = new File(queryPath);
	       fileContent = new Scanner(queryFile);

	   } catch(FileNotFoundException exception){
	       System.out.println("An error occurred\n" + exception.getMessage());
	   }
	   return fileContent;
	}


	public HashMap extractQueriesOnTitle(Scanner content) {
	   String queryId = "";
	   String queryText = "";
	   HashMap queries = new HashMap();
	       while (content.hasNextLine()) {
	           String line = content.nextLine();
	           if (line.startsWith("<num>")) {
	               queryId = line.substring(line.indexOf(":") + 2, line.length());
	           }
	           if (line.startsWith("<title>")) {
	               queryText = line.substring(line.indexOf(">") + 2, line.length());
	               queries.put(queryId, queryText);


	               queryId = "";
	               queryText = "";
	           }
	       }
	   return queries;
	}


	public HashMap extractQueriesOnTitleAndDescription(Scanner content) {
	   String queryId = "";
	   String queryText = "";
	   boolean isDescription = false;
	   HashMap queries = new HashMap();
	   while (content.hasNextLine()) {
	       String line = content.nextLine();
	       if (line.startsWith("<num>")) {
	           queryId = line.substring(line.indexOf(":") + 2, line.length());
	       }
	       if (line.startsWith("<title>")) {
	           queryText += line.substring(line.indexOf(">") + 2, line.length());
	       }
	       if(line.startsWith("<narr>")) {
	           isDescription = false;
	           queries.put(queryId, queryText);
	           queryId = "";
	           queryText = "";
	       }
	       if(isDescription)
	           queryText += " "+line;
	       if(line.startsWith("<desc>"))
	           isDescription = true;
	   }
	   return queries;
	}


	public HashMap extractQueriesOnTitleAndNarration(Scanner content) {
	   String queryId = "";
	   String queryText = "";
	   boolean isNarrative = false;
	   HashMap queries = new HashMap();
	   while (content.hasNextLine()) {
	       String line = content.nextLine();
	       if (line.startsWith("<num>")) {
	           queryId = line.substring(line.indexOf(":") + 2, line.length());
	       }
	       if (line.startsWith("<title>")) {
	           queryText += line.substring(line.indexOf(">") + 2, line.length());
	       }
	       if(line.startsWith("</top>")) {
	           isNarrative = false;
	           queries.put(queryId, queryText);
	           queryId = "";
	           queryText = "";
	       }
	       if(isNarrative)
	           queryText += " "+line;
	       if(line.startsWith("<narr>"))
	           isNarrative = true;
	   }
	   return queries;
	}


	public List<String> getStopwords() {
	   List<String> stopwordlist = null;
	   try {
	       String path = "stopwordlist.txt";
	       stopwordlist = new ArrayList<>();
	       File file = new File(path);
	       Scanner sw = new Scanner(file);
	       while (sw.hasNextLine()) {
	           stopwordlist.add(sw.nextLine().replaceAll("\\s",""));
	       }
	   } catch (FileNotFoundException exception) {
	       System.out.println("An error occurred\n" + exception.getMessage());
	   }
	   return stopwordlist;
	}
	
	public boolean isNumericString(String s){
		   for (char c: s.toCharArray()) {
		       if(Character.isDigit(c))
		           return true;
		   }
		   return false;
		}
	
	public HashMap tokenizeText(HashMap documents) {
		HashMap<String, List<String>> docs1 = new HashMap<>();
		String punctuation = "[\\p{Punct}\\s]+";
		documents.forEach((k,v) -> {
		   docs1.put(k.toString(), Arrays.stream(v.toString().split(punctuation)).toList());
		});

		HashMap<String, List<String>> docs2 = new HashMap<>();
		docs1.forEach((k,v) -> {
			   List<String> withOutNumbers = new ArrayList<>();
			   List<String> values = (List<String>)v;
			   for (int i = 0; i < values.size(); i++) {
			       if(!values.get(i).isEmpty() && !isNumericString(values.get(i)))
			           withOutNumbers.add(values.get(i).toLowerCase());
			   }
			   docs2.put(k,withOutNumbers);
			});

		HashMap<String, List<String>> docs3 = new HashMap<>();
		List<String> stopwords = getStopwords();
		   docs2.forEach((k,v) -> {
		       List<String> withOutStopwords = new ArrayList<>();
		       List<String> values = (List<String>)v;
		       for (int i = 0; i < values.size(); i++) {
		           if(!stopwords.contains(values.get(i)))
		               withOutStopwords.add(values.get(i));
		       }
		       docs3.put(k,withOutStopwords);
		   });

			HashMap<String, List<String>> finalDocs = new HashMap<>();
		   Porter stemmer = new Porter();
		   docs3.forEach((k,v) -> {
		      List<String> stemmedWords = new ArrayList<>();
		      List<String> values = (List<String>)v;
		      for (int i = 0; i < values.size(); i++) {
		              stemmedWords.add(stemmer.stripAffixes(values.get(i)));
		      }
		      finalDocs.put(k,stemmedWords);
		   });
		   
		   return finalDocs;

	}


	Dictionary dictionary = new Dictionary();
	public HashMap buildForwardIndex(HashMap documents){
	   HashMap documentDictionary = dictionary.generateDocumentDictionary(documents);  //build document dictionary
	   HashMap<String, HashMap<String,Integer>> forwardIndex = new HashMap<>();
	   documents.forEach((k,v) -> {
	       List words = (List)v;
	       HashMap<String,Integer> frquency = new HashMap<>();
	       for (int i = 0; i < words.size(); i++) {
	           String word = (String) words.get(i);
	           if (frquency.containsKey(word))
	               frquency.put(word, frquency.get(words.get(i)) + 1);
	           else
	               frquency.put(word, 1);
	       }
	       forwardIndex.put((String) documentDictionary.get(k),frquency);
	   });
	   return forwardIndex;
	}

	public HashMap<String,HashMap<String,Integer>> buildInvertedIndex(HashMap documents){
		   HashMap documentDictionary = dictionary.generateDocumentDictionary(documents);  //build document dictionary
		   HashMap<String, HashMap<String,Integer>> invertedIndex = new HashMap<>();
		   documents.forEach((k,v) -> {
		       List words = (List)v;
		       String curDocID = (String) documentDictionary.get(k);
		       HashMap<String,Integer> curWordInd;
		       for (int i = 0; i < words.size(); i++) {
		           curWordInd = new HashMap<>();
		           String word = (String) words.get(i);
		           if (invertedIndex.containsKey(word)){
		               curWordInd = invertedIndex.get(word);
		               if(curWordInd.containsKey(curDocID))
		                   curWordInd.put(curDocID, curWordInd.get(curDocID)+1);
		               else
		                   curWordInd.put(curDocID, 1);
		           }
		           else
		               curWordInd.put(curDocID, 1);
		           invertedIndex.put(word, curWordInd);
		       }
		   });
		   return invertedIndex;
		}

		HashMap forwardIndex;
		HashMap<String, HashMap<String, Integer>> invertedIndex;
		DecimalFormat df = new DecimalFormat("#.#########");


		public HashMap<String,Double> calculateIdfWeights(HashMap documents) {
		   forwardIndex = buildForwardIndex(documents);
		   invertedIndex = buildInvertedIndex(documents);

		   HashMap<String, Double> idfWeights = new HashMap<>(); 
		   int totalNumberOfDocuments = forwardIndex.size();


		   invertedIndex.forEach((term, listOfDocuments) -> {
		       idfWeights.put(term, Math.log10(((double)(totalNumberOfDocuments)) / listOfDocuments.size()));
		   });

		   return idfWeights;
		}


		public HashMap<String, TreeMap<String, Double>> findScores(HashMap documents, HashMap queries) {
			   HashMap<String, TreeMap<String, Double>> result = new HashMap<>();
			   HashMap idfWeights = calculateIdfWeights(documents);
			   queries.forEach((id, terms) -> {
			           TreeMap<String, Double> scores = new TreeMap<>();
			           HashMap<String, HashMap<String, Integer>> termFrequency;
			           HashMap<String, List<String>> query = new HashMap<>();
			           query.put((String) id, (List<String>) terms);
			           termFrequency = buildInvertedIndex(query);
			           termFrequency.forEach((term,frequencyList) -> {
			               if(invertedIndex.containsKey(term)) {
			                   double idfOfTerm = (double) idfWeights.get(term);
			                   double weightOfQueryTerm = frequencyList.get(id) * idfOfTerm;
			                   HashMap<String, Integer> postingsList = invertedIndex.get(term);
			                   postingsList.forEach((docID, frequency) -> {
			                       double weightOfDocument = frequency * idfOfTerm;
			                       double productOfWeights = weightOfDocument * weightOfQueryTerm;
			                       if (scores.containsKey(docID))
			                           scores.put(docID, scores.get(docID) + productOfWeights);
			                       else
			                           scores.put(docID, productOfWeights);
			                   });
			               }
			           });
			           scores.forEach((docID, score) -> {
			               scores.put(docID, Double.parseDouble(df.format(score/((HashMap)forwardIndex.get(docID)).size())));
			           });
			           TreeMap<String,Double> sortedScores = (TreeMap) sortByValue(scores);
			           result.put((String)id,sortedScores);
			           query.remove(id);
			       });
			   return result;
			}

		public <K, V extends Comparable<V> > Map<K, V> sortByValue(final Map<K, V> map)
		{
		   Comparator<K> valueComparator = new Comparator<K>() {
		       public int compare(K k1, K k2)
		       {
		           int comp = map.get(k2).compareTo(
		                   map.get(k1));
		           if (comp == 0)
		               return 1;
		           else
		               return comp;
		       }
		   };
		   Map<K, V> sortedMap = new TreeMap<K, V>(valueComparator);
		   sortedMap.putAll(map);
		   return sortedMap;
		}


}
