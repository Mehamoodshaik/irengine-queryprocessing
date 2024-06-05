package project;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IR_Indexer {

	public static void main(String[] args) {
		Implementer implementer = new Implementer();

		HashMap docs = implementer.readDocuments();

		docs = implementer.tokenizeText(docs);

		Scanner queryFiles = implementer.extractQueries();

		HashMap queryListTitle;
		HashMap queryListTitleNarration;
		HashMap queryListDescription;

		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream("phase3_output_3_title.txt"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		System.setOut(out);

		// query - title
		queryListTitle = implementer.extractQueriesOnTitle(queryFiles);

		updatedQueryListTitle = implementer.tokenizeText(queryList);

		HashMap<String, TreeMap<String, Double>> cosineScores = implementer.findScores(docs, updatedQueryList);
		List<String> keys = new ArrayList<>(cosineScores.keySet());
		Collections.sort(keys);
		System.out.println("Query\tDoc\tOrder\tScore");
		for (String query : keys) {
			AtomicInteger i = new AtomicInteger(1);
			cosineScores.get(query).forEach((docID, score) -> {
				System.out.println(query + "\t" + docID + "\t" + i.getAndIncrement() + "\t" + score);
			});
		}

		// query - title and description
		try {
			out = new PrintStream(new FileOutputStream("phase3_output_3_title_and_Description.txt"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		System.setOut(out);

		queryListTitleDescription = implementer.extractQueriesOnTitleAndDescription(queryFiles);

		updatedQueryListTitleDescription = implementer.tokenizeText(queryListTitleDescription);

		HashMap<String, TreeMap<String, Double>> cosineScores = implementer.findScores(docs,
				updatedQueryListTitleDescription);
		List<String> keys = new ArrayList<>(cosineScores.keySet());
		Collections.sort(keys);
		System.out.println("Query\tDoc\tOrder\tScore");
		for (String query : keys) {
			AtomicInteger i = new AtomicInteger(1);
			cosineScores.get(query).forEach((docID, score) -> {
				System.out.println(query + "\t" + docID + "\t" + i.getAndIncrement() + "\t" + score);
			});
		}

		// title - narration
		try {
			out = new PrintStream(new FileOutputStream("phase3_output_3_title_and_Description.txt"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		System.setOut(out);

		queryListTitleNarration = implementer.extractQueriesOnTitleAndNarration(queryFiles);

		updatedQueryListTitleNarration = implementer.tokenizeText(queryListTitleNarration);

		HashMap<String, TreeMap<String, Double>> cosineScores = implementer.findScores(docs,
				updatedQueryListTitleNarration);
		List<String> keys = new ArrayList<>(cosineScores.keySet());
		Collections.sort(keys);
		System.out.println("Query\tDoc\tOrder\tScore");
		for (String query : keys) {
			AtomicInteger i = new AtomicInteger(1);
			cosineScores.get(query).forEach((docID, score) -> {
				System.out.println(query + "\t" + docID + "\t" + i.getAndIncrement() + "\t" + score);
			});
		}

	}

}
