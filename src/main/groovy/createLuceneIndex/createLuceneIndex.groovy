package createLuceneIndex

class TextFilesFilter implements FileFilter {
    public boolean accept(File path) {
        return path.getName().toLowerCase()
                .endsWith(".txt");
    }
}

def createIndex(indexDir, dataDir){
    long start = System.currentTimeMillis();
    Indexer indexer = new Indexer(indexDir);
    int numIndexed;
    try {
        numIndexed = indexer.index(dataDir, new TextFilesFilter());
    } finally {
        indexer.close();
    }
    long end = System.currentTimeMillis();
    System.out.println("Indexing " + numIndexed + " files took "
            + (end - start) + " milliseconds");
}

if (args.length != 2) {
    throw new IllegalArgumentException("Usage: groovy createLuceneindex "
            + " <index dir> <data dir>");
}
String indexDir = args[0];
String dataDir = args[1];

createIndex(indexDir, dataDir);