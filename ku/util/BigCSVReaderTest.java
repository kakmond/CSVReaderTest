package ku.util;
import org.junit.*;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Random;
import java.util.List;

import ku.util.CSVReader;
/**
 * JUnit 4 tests for the CSVReader assignment.
 * Add this file to your project to run the tests.
 * In Eclipse or NetBeans it will need to add
 * the JUnit 4 library to your project.
 * Eclipse and Netbeans will do this if you click
 * on one of the error for one of the "import org.junit.*"
 * lines, and let Eclipse or Netbeans suggest a fix.
 * In Eclipse the fix is "Fix Project settings".
 *
 * You can run this file as a JUnit Test suite or
 * run it as a Java application (which will invoke
 * the main method and my test runner code).
 * You can also run these tests on command line
 * if you have junit.jar on your classpath:
 * java -cp /path-to-junit/junit.jar;. BigCSVReaderTest
 *
 * @author Jim Brucker
 *
 */
public class BigCSVReaderTest {
	// A temporary file used to write data for tests.
	// Set this to whatever is appropriate for your system.
	private final String tempfilename = "testdata.csv";
	private File file;
	private InputStream instream;
	private OutputStream outstream;
	private PrintStream out;
	private char delim;
	
	@Before
	public void setUp() throws Exception {
		file = new File(tempfilename);
		outstream = new FileOutputStream(file);
		out = new PrintStream(outstream, true);
		delim = ',';
	}

	@After
	public void tearDown() throws Exception {
		if (instream!=null) try { instream.close(); } catch(IOException e) {/*ignore*/}
		if (outstream!=null) try { outstream.close(); } catch(IOException e) {/*ignore*/}
		if ( file.exists() ) {
			// Delete the old file to guarantee contents are overwritten each test.
			// This shouldn't really be necessary.
			//file.delete();
			// Or, don't delete file if you want to verify path or contents.
			// Or, add some code here to print the file so you can see test data.
		}
		instream = null;
	}

	/** Get an InputStream for reading from the file of CSV data. */
	private InputStream getInstream() {
		if (instream == null) try {
			instream = new FileInputStream(file);
		} catch(IOException ioe) {
			throw new RuntimeException("Couldn't create instream for test", ioe);
		}
		return instream;
	}
	
	/** 
	 * Test that CSVReader implements Iterator for array of String.
	 */
	@Test(timeout=100)
	public void testImplementsIterator() {
		CSVReader csv = new CSVReader( getInstream() );
		// instanceof can't test for type parameter values
		assertTrue( csv instanceof java.util.Iterator );	
	}
	
	/** 
	 * Test basic use of hasNext - next using unquoted strings
	 *  and different number of fields on each line.
	 */
	@Test(timeout=100)
	public void testUnquoted() {
		String [] r1 = {"aaa","bbb","ccc"};
		String [] r2 = {"A","Bb","C","Dd","5555","six","7-11","8","Nines","10!!!"};
		String [] r3 = {"a"};
		writeArrayToLine(r1);
		writeArrayToLine(r2);
		writeArrayToLine(r3);
		out.close();
		
		CSVReader csv = new CSVReader( getInstream() );
		assertTrue( csv.hasNext() );
		String [] next = csv.next();
		assertEquals( r1.length, next.length );
		assertArrayEquals( r1, next );
		assertTrue( csv.hasNext() );
		assertArrayEquals( r2, csv.next() );
		assertTrue( csv.hasNext() );
		assertArrayEquals( r3, csv.next() );
		assertFalse( csv.hasNext() );
	}

	/** Input with many fields on one line. */
	@Test(timeout=100)
	public void testLongLines() {
		String message = "Write-readable-software";
		int length =  message.length();
		Random rand = new Random();
		int count = 100;
		String [] r1 = new String[count];
		for(int k=0; k<count; k++) r1[k] = message.substring(rand.nextInt(length)+1);
		count = 200;
		String [] r2 = new String[count];
		message = "abcdefghij";
		r2[0] = Character.toString(message.charAt(0));
		for(int k=1; k<count; k++) r2[k] = r2[k-1]+message.charAt(k%10);
		writeArrayToLine(r1);
		writeArrayToLine(r2);
		out.close();
		
		CSVReader csv = new CSVReader(getInstream());
		assertTrue( csv.hasNext() );
		String [] next = csv.next();
		assertEquals( r1.length, next.length );
		assertArrayEquals( r1, next );
		assertTrue( csv.hasNext() );
		assertArrayEquals( r2, csv.next() );

		assertFalse( csv.hasNext() );
	}
	
	
	/** 
	 * CSV reader should skip empty lines.
	 */
	@Test(timeout=100)
	public void testSkipEmptyLines() {
		String [] r1 = {"aaa","bbb","ccc"};
		String [] r2 = {"AAA","BBB"};
		String [] r3 = {"the-end"};
		writeArrayToLine(r1);
		writeline("");  // empty line
		writeline("");   // another empty line
		writeArrayToLine(r2);
		writeline("");
		writeArrayToLine(r3);
		out.close();
		
		CSVReader csv = new CSVReader(getInstream());
		assertTrue( csv.hasNext() );
		String [] next = csv.next();
		assertEquals( r1.length, next.length );
		assertArrayEquals( r1, next );
		assertTrue( csv.hasNext() );
		assertArrayEquals( r2, csv.next() );
		assertTrue( csv.hasNext() );
		assertArrayEquals( r3, csv.next() );
		assertFalse( csv.hasNext() );
	}
	
	/** Test of next using data with spaces in the fields. 
	 *  CSVReader should remove surrounding space but not internal space.
	 */
	@Test(timeout=100)
	public void testUnquotedSpace() {
		String [] r1in  = {"aaa  ","  bbb","  ccc "};
		String [] r1out = {"aaa","bbb","ccc"};
		String [] r2 = {"AA   AA","","C C","DD DD"};
		writeArrayToLine(r1in);
		writeArrayToLine(r2);
		out.close();
		
		CSVReader csv = new CSVReader(getInstream());
		assertArrayEquals( r1out, csv.next() );
		assertArrayEquals( r2, csv.next() );
	}
	
	
	/** Test handling of empty fields (CSVReader should not remove them).
	 *  How to handle trailing empty fields wasn't explicitly 
	 *  specified, so don't test for that.
	 */
	@Test(timeout=100)
	public void testEmptyFields() {
		String [] r1  = {"a","","c"}; // 3 fields
		String [] r2 = {"","","c"};   // 3 fields
		String [] r3 = {"","b"};      // 2 fields
		String [] r4 = {"a","","","","","d"}; // many fields
		writeArrayToLine(r1);
		writeArrayToLine(r2);
		writeArrayToLine(r3);
		writeArrayToLine(r4);
		out.close();
		
		CSVReader csv = new CSVReader(getInstream());
		assertTrue( csv.hasNext() );
		assertArrayEquals( r1, csv.next() );
		assertArrayEquals( r2, csv.next() );
		assertArrayEquals( r3, csv.next() );
		assertArrayEquals( r4, csv.next() );
	}
	
	/** Test quoted fields. CSVReader should remove quotes at start and end. 
	 *  For example:  "a","b","cccc" (input data has quotes around fields)
	 */
	@Test(timeout=100)
	public void testQuotedFields() {
		String [] r1 = {"An Apple", "or Orange"};
		// should not remove space inside of quotes
		String [] r2 = {"","a b"," has space "};
		quoteline(r1);
		quoteline(r2);
		out.close();
	
		CSVReader csv = new CSVReader(getInstream());
		assertTrue( csv.hasNext() );
		assertArrayEquals( r1, csv.next() );
		assertArrayEquals( r2, csv.next() );
	}
	
	/** 
	 * next() should throw NoSuchElementException if no more data.
	 */
	@Test(timeout=100,expected=java.util.NoSuchElementException.class)
	public void testNextThrowsException() {
		String [] r1 = {"Crash","and","burn"};
		writeArrayToLine(r1);
		out.close();
		
		CSVReader csv = new CSVReader(getInstream());
		assertTrue( csv.hasNext() );
		String [] next = csv.next();
		assertEquals( r1.length, next.length );
		csv.next();
		fail("next should throw NoSuchElementException if no more data");
	}
	
	/** Test that we can change the delimiter character. */
	@Test(timeout=100)
	public void testSetDelimiter() {
		String [] r1 = {"NAME", "ADDRESS","EMAIL"};
		// should not remove space inside of quotes
		String [] r2 = {"Santa Claus","North Pole","santa@snowy.no"};
		String [] r3 = {"Obama","1 Pennsylvania Ave","pres@whitehouse.gov"};
		// default delimiter
		writeArrayToLine(r1);
		// change delimiter
		delim  =  ':';
		writeArrayToLine(r2);
		writeArrayToLine(r3);
		out.close();
		
		CSVReader csv = new CSVReader(getInstream());
		assertArrayEquals( r1, csv.next() );
		csv.setDelimiter(delim);
		assertArrayEquals( r2, csv.next() );
		assertArrayEquals( r3, csv.next() );
	}
	
	/** Constructor accepts name of file. */
	@Test(timeout=100)
	public void testConstructorWithFilename() throws IOException {
		String [] r1 = {"NAME", "ADDRESS","EMAIL"};
		// should not remove space inside of quotes
		String [] r2 = {"Santa Claus","North Pole","santa@snowy.no"};
		writeArrayToLine(r1);
		writeArrayToLine(r2);
		out.close();
		
		String filename = file.getAbsolutePath();
		//System.out.println("test: new CSVReader("+filename+")");
		CSVReader csv = new CSVReader(filename);
		assertTrue("Open with filename "+filename, csv.hasNext());
		assertArrayEquals( r1, csv.next() );
		assertTrue(csv.hasNext());
		assertArrayEquals( r2, csv.next() );
		assertFalse(csv.hasNext());
	}
	
	
	/** Test Constructor accepts URL. */
	@Test(timeout=100)
	public void testConstructorWithURL() throws Exception {
		String [] r1 = {"NAME", "ADDRESS","EMAIL"};
		// should not remove space inside of quotes
		String [] r2 = {"Santa","North Pole","santa@snowy.no"};
		writeArrayToLine(r1);
		writeArrayToLine(r2);
		out.close();
		String url = file.toURI().toString();
		// add gratuitous "//" after protocol to match pattern in assignment
		if (url.startsWith("file:")) url = "file://" +url.substring(5);
		// System.out.println("test: new CSVReader("+url+")");
		CSVReader csv = new CSVReader(url);
		assertTrue("Open with url "+url, csv.hasNext());
		assertArrayEquals( r1, csv.next() );
	}
	
	/** Constructor accepts name of file. */
	@Test(timeout=100, expected=java.io.FileNotFoundException.class)
	public void testConstructorThrowsRuntimeException() throws Exception {
		String filename = "ANonExistentFile.hahaha";
		CSVReader csv = new CSVReader(filename);
		fail( "Should throw FileNotFoundException for non-existent file "+filename );
	}
	
	/** write a line of unquoted args in CSV format */
	private void writeArrayToLine(String[ ] args) {
		StringBuilder buf = new StringBuilder();
		if (args.length>0) buf.append(args[0]);
		int k = 1;
		while(k < args.length) buf.append(delim).append(args[k++]);
		out.println(buf.toString());
	}
	
	
	/** write a line of unquoted args in CSV format */
	private void writeline(String ... args) {
		writeArrayToLine(args);
	}
	
	/** write a line of quoted args in CSV format. Quotes are added. */
	private void quoteline(String[] args) {
		StringBuilder buf = new StringBuilder();
		if (args.length>0) buf.append(q(args[0]));
		int k = 1;
		while(k < args.length) buf.append(delim).append(q(args[k++]));
		out.println(buf.toString());
	}
	
	/** surround a string with double quote marks */
	private String q(String s) {
		return '"' + s + '"';
	}
	
	/** 
	 * My test runner. It displays more information about
	 * the results than JUnit's TestRunner.
	 * Run a test suite and describe the results. 
	 * @param testclass name of class containing JUnit tests
	 */
	public static void runTests( Class<?> testclass) {
		Result result = org.junit.runner.JUnitCore.runClasses( testclass );
		int count = result.getRunCount();
		int failed = result.getFailureCount();
		System.out.printf("%s: Running %d tests\n", testclass.getSimpleName(), count);
		List<Failure> failures = result.getFailures();
		// this doesn't seem to return all the failures
		for(Failure f: failures) {
			Description d = f.getDescription();
			System.out.println( f.getTestHeader() +": "+ f.getMessage() );
			System.out.println( d.getDisplayName() );
		}
		System.out.printf("%s: Success %d Failure %d\n",
				testclass.getSimpleName(), count-failed, failed);
	}
	
	/** Run a test suite and describe the results. */
	public static void main(String[] args) {
		runTests( BigCSVReaderTest.class );
	}
}
