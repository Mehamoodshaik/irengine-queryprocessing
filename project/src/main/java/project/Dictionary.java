package project;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Dictionary {

	public HashMap<String, Integer> generateWordDictionary(HashMap<String, List<String>> documents){
	       HashMap<String, Integer> wordDict = new HashMap<>();
	       int id = 1;
	       List<String> wordList = new ArrayList<>();
	       documents.forEach((k,v) -> {
	           List<String> words = (List<String>)v;
	           for (int i = 0; i < words.size(); i++) {
	               wordList.add((String)words.get(i));
	           }
	       });
	       Collections.sort(wordList);
	       for (String word: wordList) {
	           if(!word.isEmpty() && !wordDict.containsKey(word))
	               wordDict.put(word,id++);
	       }
	       return wordDict;
	   }

   public HashMap generateDocumentDictionary(HashMap documents){
       HashMap documentDict = new HashMap<>();
       documents.forEach((k,v) -> {
           String value = (String) k;
           if(value.contains("-"))
               documentDict.put((String) k, value.split("-")[1]);
           else
               documentDict.put((String) k, value);
       });
       return documentDict;
   }
}

