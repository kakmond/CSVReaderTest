# JUnit Tests for CSVReader

This repository contains JUnit tests for the CSVReader assignment.

* `EasyCSVReaderTest.java` - example CSV data from the assignment sheet, and two tests of whether CSVReader skips blank lines and comment lines.

* `BigCSVReaderTest.java` - more detailed tests of CSVReader.

These tests require:

1. You have a `CSVReader.java` class in the correct package, and it has the constructor and methods specified in the assignment.
2. You have JUnit 4 on your project classpath.

Eclipse and Netbeans come with the JUnit 4 library. If you add these JUnit test files to your project (see below), Eclipse/Netbeans will give an error message about missing classes.  Then they will offer to "fix project setup" by adding JUnit 4 to your project.

In Eclipse, if your project contains JUnit tests but doesn't have the JUnit library, Eclipse will flag errors because the project doesn't have the JUnit library.  Open one of the JUnit test sources and put the cursor on a line that has an error (like `import org.junit.*`). Press CTRL-1 which means "quick fix".  Eclipse will show a dialog of fixes.  Choose "Fix project setup".

### What You Need to Know

1. How to add JUnit to an Eclipse or Netbeans project.
    * Eclipse and Netbeans include JUnit, so simply add the JUnit 4 library to your project configuration.
2. How to configure a project to have 2 "source" directories: the main source code (in the `src` directory) and test source code (a `test` directory).
    * First, add the test code to a folder named `test`. The test folder should have the same directory structure as your main `src` folder. (See below)
    * In Eclipse, open the dialog for Project -> Properties and choose "Java Build Path".  Select the "Source" tab.  Click "Add Folder..." and add the test folder.  Exit the dialog.
    * Now, Eclipse will compile and run files in both the `src` and `test` folders.

### Add these Tests to your Project

**Please don't "commit" my JUnit test files as part of your project code!**

#### 1. Using git submodule (the preferred way)

The Git repository for CSVReaderTest is structured as a Git submodule.
Git submodules let you add a Git repository inside another Git repository, but keep the two sets of files separate.  Here are the steps:

```
# 1. change directory to your CSVReader project
> cd /workspace/csvreader

# 2. add the CSVReaderTest as a submodule, and put the files in a 
#    subdirectory named "test".  
#    IMPORTANT: don't forget the "test" parameter on command line
> git submodule add https://github.com/OOP2017/CSVReaderTest.git test

# This should add files to the "test" directory.
# You should have files
# test/ku/util/BigCSVReaderTest.java and test/ku/util/EasyCSVReaderTest.java

# 3. If you _don't_ have any files in the test directory then do this:
> git submodule init
> git submodule update
```

#### 2. Just add the files to project

If you can't use `git submodule` then create a ZIP file of the CSVReaderTest repo, and unzip the files into your CSVReader project directory. It will
create files and folders like this:
```
test/
test/README.md
test/ku/util
test/ku/util/BigCSVReaderTest.java
test/ku/util/EasyCSVReaderTest.java
```

### Running the Tests

* Run as JUnit test in Eclipse:

    Right click on one of the files and select "Run as..." > "JUnit test case". This is the best way, since you can see the JUnit failure messages. If you click on a failure message, it will show the JUnit test code that caused the failure.

* Run as ordinary Java program in Eclipse:

    Right click on a file and select "Run as..." > "Java Application".  This runs the same tests as using JUnit, but uses my test runner which prints extra output.

