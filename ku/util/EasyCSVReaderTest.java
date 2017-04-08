package ku.util;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EasyCSVReaderTest {


	private final String tempfilename = "testdata.csv";
	private File file;
	private InputStream instream;
	private OutputStream outstream;
	private PrintStream out;
	private char delimiter = ',';
	
	@Before
	public void setUp() throws Exception {
		file = new File(tempfilename);
		outstream = new FileOutputStream(file);
		out = new PrintStream(outstream, true);
	}

	@After
	public void tearDown() throws Exception {
		if (instream!=null) try { instream.close(); } catch(IOException e) {/*ignore*/}
		if (outstream!=null) try { outstream.close(); } catch(IOException e) {/*ignore*/}
		if ( file.exists() ) {
			System.out.println("CSV filename "+file.getAbsolutePath() );
			
			// Delete the old file to guarantee contents are overwritten for each test.
			// Don't delete file if you want to verify path or contents.
			//file.delete();
		}
		instream = null;
	}
	
	/**
	 * This is the example given in assignment sheet.
	 * If people test their code, it should definitely pass this test.
	 */
	@Test
	public void testEasyExample() {
		// put data in the file
		writeLine("FIRST,LAST,EMAIL,TELEPHONE");
		writeLine("\"Harry\",  \"Potter\"     , harry@wizards.com,086-9999999");
		writeLine("Magic, Owl,, 12345678 ,");
		CSVReader reader = new CSVReader( getInputStream() );
		// should have the three lines written above
		assertTrue( reader.hasNext() );
		String [] args = reader.next();
		String [] expected = {"FIRST","LAST","EMAIL","TELEPHONE"};
		assertArrayEquals(expected, args);
		// you should not be required to call hasNext() first
		args = reader.next();
		expected = new String[]{"Harry", "Potter", "harry@wizards.com", "086-9999999"};
		assertArrayEquals(expected, args);
		// you can call hasNext as many times as you like
		assertTrue("CSV 3rd line of data missing", reader.hasNext());
		assertTrue("hasNext consumed an extra line of data", reader.hasNext());
		args = reader.next();
		expected = new String[]{"Magic","Owl","","12345678",""};
		assertArrayEquals("Missing blank fields", expected, args);
		assertFalse("hasNext returned true but no data left", reader.hasNext());
	}
	
	/**
	 * CSVReader should ignore blank lines, even if they contain some space characters.
	 * In real CSV files, it could happen that ther is extra (unintended) blank line at end.
	 */
	@Test
	public void testIgnoreBlankLines() {
		// put data in the file
		writeLine("first");
		writeLine("");
		writeLine("");
		writeLine("the,end");
		writeLine("");  // in a real data file, this could easily happen.
		CSVReader reader = new CSVReader( getInputStream() );
		// should have the three lines written above
		assertTrue( reader.hasNext() );
		String [] args = reader.next();
		String [] expected = {"first"};
		assertArrayEquals(expected, args);
		
		assertTrue(reader.hasNext());
		expected = new String[]{"the","end"};
		args = reader.next();
		assertArrayEquals("CSVReader didn't skip blank lines", expected, args);
		assertFalse("CSVReader didn't skip blank line at end", reader.hasNext());
	}
	
	/**
	 * Lines beginning with # are comment lines and should be ignored.
	 */
	@Test
	public void testIgnoreCommentLines() {
		// put data in the file
		writeLine("# This is a comment");
		writeLine("first");
		writeLine("# Another comment");
		writeLine("the,end");
		CSVReader reader = new CSVReader( getInputStream() );
		// should have the three lines written above
		assertTrue( reader.hasNext() );
		String [] args = reader.next();
		String [] expected = {"first"};
		assertArrayEquals(expected, args);
		
		assertTrue(reader.hasNext());
		expected = new String[]{"the","end"};
		args = reader.next();
		assertArrayEquals(expected, args);
		assertFalse(reader.hasNext());
	}

	/** write a line of unquoted args in CSV format */
	private void writeArrayToLine(String[ ] args) {
		StringBuilder buf = new StringBuilder();
		if (args.length>0) buf.append(args[0]);
		for(int k=1; k < args.length; k++) buf.append(delimiter).append(args[k]);
		out.println(buf.toString());
	}
	
	
	/** write a line of unquoted args in CSV format */
	private void writeLine(String ... args) {
		StringBuilder buf = new StringBuilder();
		if (args.length>0) buf.append(args[0]);
		for(int k=1; k < args.length; k++) buf.append(delimiter).append(args[k]);
		out.println(buf.toString());
	}
	
	/** Get an InputStream to read from the csv test file. */
	private InputStream getInputStream() {
		if (instream == null) try {
			instream = new FileInputStream(file);
		} catch(IOException ioe) {
			throw new RuntimeException("Couldn't create instream for test", ioe);
		}
		return instream;
	}
	
	
	/** Run a test suite and describe the results. */
	public static void main(String[] args) {
		BigCSVReaderTest.runTests( EasyCSVReaderTest.class );
	}
}
