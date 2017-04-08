# JUnit Tests for CSVReader

This repository contains some JUnit tests for the CSVReader assignment.

* `EasyCSVReaderTest.java` - the example CSV from the assignment sheet, and two tests of whether CSVReader skips blank lines and comment lines.

* `BigCSVReaderTest.java` - more detailed tests of CSVReader

These tests require:

1. You have a correct `CSVReader.java` in the correct package.
2. You have JUnit 4 on the classpath.  

Eclipse and Netbeans come with the JUnit 4 library. If you add these test files to your project, Eclipse/Netbeans will give an error message about missing classes (the JUnit classes).  Then they will both offer to "fix project setup" by adding JUnit 4 to your project.

In Eclipse, if you add some JUnit tests to a project Eclipse will flag errors because your project doesn't have the JUnit library.  Open one of these files and put the cursor on a line that has an error (like `import org.junit.*`). Press CTRL-1 which means "quick fix".  Eclipse will show a dialog of fixes.  Choose "Fix project setup".

### What You Need to Know

1. How to add JUnit to an Eclipse or Netbeans project.
    * Eclipse and Netbeans include JUnit, so simply add the JUnit 4 library to your project configuration.
2. How to configure a project to have 2 "source" directories: the main source code (in the `src` directory) and test source code (a `test` directory).
    * First, add the test code to a folder named `test`. The test folder should have the same directory structure as your main `src` folder. (See below)
    * In Eclipse, open the dialog for Project -> Properties and choose "Java Build Path".  Select the "Source" tab.  Click "Add Folder..." and add the test folder.  Exit the dialog.
    * Now, Eclipse will compile and run files in both the `src` and `test` folders.

### Using the tests

The Git repository for CSVReaderTest is structured as a Git submodule.
Read below for how to add it to an existing Git project.

**please don't "commit" my JUnit test files as part of your project!**

The tests are in a directory named `test` to avoid mixing the test code in with production source code.  Copy the `test` directory to your In Eclipse and Netbeans you can have many "src" directories in a project.  Discover how to do it yourself, or ask a TA.  

Or, you can just copy the java files to your project's `src/ku/util` directory.  But please don't "commit" the Junit test files as part of your project.

### Running the Tests:

Run as JUnit test in Eclipse:

* Right click on one of the files and select "Run as..." > "JUnit test case". This is the best way, since you can see the JUnit failure messages. If you click on a failure message, it will show the JUnit test code that caused the failure.

Run as ordinary Java program in Eclipse:

* Right click on a file and select "Run as..." > "Java Application".  This runs the same tests as using JUnit, but uses my test runner which prints extra output.

