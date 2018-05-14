package app.test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class LuceneWriteIndexExample {

	private static final String INDEX_DIR = "d:/lucene/lucene6index";

	public static void main(String[] args) throws Exception {
		System.out.println("create lucene index start");
		IndexWriter writer = createWriter();
		List<Document> documents = new ArrayList<>();

		Document document1 = createDocument(1, "Lokesh", "中華民國", "howtodoinjava.com");
		documents.add(document1);

		Document document2 = createDocument(2, "Brian", "Schultz", "example.com");
		documents.add(document2);

		// Let's clean everything first
		writer.deleteAll();

		writer.addDocuments(documents);
		writer.commit();
		writer.close();
		System.out.println("create lucene index end");
	}

	private static Document createDocument(Integer id, String firstName, String lastName, String website) {
		Document document = new Document();
		document.add(new StringField("id", id.toString(), Field.Store.YES));
		document.add(new TextField("firstName", firstName, Field.Store.YES));
		document.add(new TextField("lastName", lastName, Field.Store.YES));
		document.add(new TextField("website", website, Field.Store.YES));
		String[] ws = website.split("\\.");
		for (String w : ws) {
			System.out.println(w);
			document.add(new TextField("index", w, Field.Store.NO));
		}
		return document;
	}

	private static IndexWriter createWriter() throws IOException {
		FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(dir, config);
		return writer;
	}
}
