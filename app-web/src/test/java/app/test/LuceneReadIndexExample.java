package app.test;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneReadIndexExample {

	private static final String INDEX_DIR = "d:/lucene/lucene6index";

	public static void main(String[] args) throws Exception {
		IndexSearcher searcher = createSearcher();

		// Search by ID
		TopDocs foundDocs = searchById(1, searcher);

		System.out.println("Total Results :: " + foundDocs.totalHits);

		for (ScoreDoc sd : foundDocs.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("firstName")));
		}

		// Search by firstName
		TopDocs foundDocs2 = searchByFirstName("Brian", searcher);

		System.out.println("Total Results :: " + foundDocs2.totalHits);

		for (ScoreDoc sd : foundDocs2.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("id")));
		}
		
		// Search by listName
		TopDocs foundDocs22 = searchByListName("中華民國", searcher);

		System.out.println("中華民國 Total Results :: " + foundDocs22.totalHits);

		for (ScoreDoc sd : foundDocs22.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("lastName")));
		}

		// Search by Index
		TopDocs foundDocs3 = searchByIndex("com", searcher);

		System.out.println("Total Results :: " + foundDocs3.totalHits);

		for (ScoreDoc sd : foundDocs3.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("website")));
		}

		// Search by Index
		TopDocs foundDocs4 = searchByIndex("example", searcher);

		System.out.println("Total Results :: " + foundDocs4.totalHits);

		for (ScoreDoc sd : foundDocs4.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("website")));
		}

		// Search by Index
		TopDocs foundDocs5 = searchByIndex("example com", searcher);

		System.out.println("Total Results :: " + foundDocs5.totalHits);

		for (ScoreDoc sd : foundDocs5.scoreDocs) {
			Document d = searcher.doc(sd.doc);
			System.out.println(String.format(d.get("website")));
		}
	}

	private static TopDocs searchByFirstName(String firstName, IndexSearcher searcher) throws Exception {
		QueryParser qp = new QueryParser("firstName", new StandardAnalyzer());
		Query firstNameQuery = qp.parse(firstName);
		TopDocs hits = searcher.search(firstNameQuery, 10);
		return hits;
	}
	
	private static TopDocs searchByListName(String listName, IndexSearcher searcher) throws Exception {
		QueryParser qp = new QueryParser("lastName", new StandardAnalyzer());
		Query listNameQuery = qp.parse(listName);
		TopDocs hits = searcher.search(listNameQuery, 10);
		return hits;
	}

	private static TopDocs searchById(Integer id, IndexSearcher searcher) throws Exception {
		QueryParser qp = new QueryParser("id", new StandardAnalyzer());
		Query idQuery = qp.parse(id.toString());
		TopDocs hits = searcher.search(idQuery, 10);
		return hits;
	}

	private static TopDocs searchByWebsite(String website, IndexSearcher searcher) throws Exception {
		QueryParser qp = new QueryParser("website", new StandardAnalyzer());
		Query websiteQuery = qp.parse(website);
		TopDocs hits = searcher.search(websiteQuery, 10);
		return hits;
	}

	private static TopDocs searchByIndex(String index, IndexSearcher searcher) throws Exception {
		QueryParser qp = new QueryParser("index", new StandardAnalyzer());
		Query websiteQuery = qp.parse(index);
		TopDocs hits = searcher.search(websiteQuery, 10);
		return hits;
	}

	private static IndexSearcher createSearcher() throws IOException {
		Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
}
