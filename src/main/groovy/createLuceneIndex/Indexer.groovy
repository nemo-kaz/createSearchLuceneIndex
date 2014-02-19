package createLuceneIndex

import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version

/**
 * Created by yamir on 2/18/14.
 */
public class Indexer {

    private IndexWriter writer;

    public Indexer(String indexDir) throws IOException{
        Directory dir = FSDirectory.open(new File(indexDir))
        IndexWriterConfig iwc = new IndexWriterConfig(
                Version.LUCENE_46,
                new StandardAnalyzer(Version.LUCENE_46)
        )
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE)
        writer = new IndexWriter (dir,iwc);
    }


    public void close() throws IOException {
        writer.close();
    }

    public int index (String dataDir, FileFilter filter) throws Exception {
        File[] files = new File(dataDir).listFiles();

        for (File f: files) {
            if (!f.isDirectory() &&
                    !f.isHidden() &&
                    f.exists() &&
                    f.canRead() &&
                    (filter == null || filter.accept(f))) {
                indexFile(f);
            }
        }
        return writer.numDocs();
    }

    protected Document getDocument(File f) throws Exception {
        Document doc = new Document();
        doc.add(new Field("contents", new FileReader(f)));
        doc.add(new Field("filename", f.getName(),
                Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("fullpath", f.getCanonicalPath(),
                Field.Store.YES, Field.Index.NOT_ANALYZED));

        return doc;
    }

    private void indexFile(File f) throws Exception {
        System.out.println("Indexing " + f.getCanonicalPath());
        Document doc = getDocument(f);
        writer.addDocument(doc);
    }

}
