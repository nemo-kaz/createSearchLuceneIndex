package searchLuceneIndex

import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.TopDocs
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.queryparser.classic.QueryParser

def searchIndex(String indexLocation, String query) {

    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
    // Now search the index
    IndexSearcher searcher=new IndexSearcher(reader);
    // Parse a simple query that searches for "text":
    QueryParser parser = new QueryParser(Version.LUCENE_46, "contents", new SimpleAnalyzer(Version.LUCENE_46));
    Query q = parser.parse(query);
    TopDocs docs = searcher.search(q, 10);
    System.out.println("Total hits: " +docs.totalHits);
    Document doc=searcher.doc(docs.scoreDocs[0].doc);
    System.out.println(doc.get("filename"));
    reader.close();

}

if (args.length < 1) {
    throw new IllegalArgumentException("Usage: groovy searchLuceneIndex "
            + " <indexdir> <query>");
}


searchIndex(args[0], args[1]);
