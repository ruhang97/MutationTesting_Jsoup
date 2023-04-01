package edu.illinois.cs.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MutationTest
{
	@Test
	public void testJsoup() {
		// Instantiate the CodeModifier class that you will implement to perform
		// the actual task. This is a visitor class according to the visitor
		// pattern (one of the most important design patterns).
		VoidVisitorAdapter codeModifier = new FalseReturnsMutator();
		boolean killed = modifyAndTryToKill(codeModifier, "nodes", "Document.java");
		assertTrue(killed);
	}

	/*
	 * Use the modifier to create mutant, run tests on mutant
	 * Input: 
	 * 		module = "nodes" or "parser";		
	 * 		targetFile = "Document.java"
	 * Return:
	 * 		true if mutant killed
	 * 		false if not killed
	 */
	private boolean modifyAndTryToKill(VoidVisitorAdapter codeModifier, String module, String targetFile) {
		// Initialize the source root as the "target/test-classes" dir, which
		// includes the test resource information (i.e., the source code info
		// for Jsoup for this assignment) copied from src/test/resources
		// during test execution
		SourceRoot sourceRoot = new SourceRoot(
				CodeGenerationUtils.mavenModuleRoot(CodeParserTest.class)
					.resolve("target/test-classes"));

		// Get the code representation for the specific java file using its
		// package and name info
		// CompilationUnit cu = sourceRoot.parse("org.jsoup.nodes",
		CompilationUnit cu = sourceRoot.parse("org.jsoup." + module,
				targetFile);

		// Apply our visitor class on the code representation for the given
		// Java file. In this way, our visit function(s) can be automatically
		// applied to all possible elements of the specified type(s).
		codeModifier.visit(cu, null);
		
		SourceRoot targetRoot = new SourceRoot(
			CodeGenerationUtils.mavenModuleRoot(CodeParserTest.class)
			// .resolve("jsoup/src/main/java"));
			.resolve("jsoup/src/main/java/org/jsoup"));		// add module
			

		// String targetPath = targetRoot.getRoot() + "/org/jsoup/nodes/" + targetFile;
		String targetPath = targetRoot.getRoot() + "/" + module + "/" + targetFile;	// add module
		saveMutantToFile(targetPath, cu);

		boolean killed = mutantKilled();
		System.out.println("Mutant Killed: " + Boolean.toString(killed));
		
		sourceRoot = new SourceRoot(
				CodeGenerationUtils.mavenModuleRoot(CodeParserTest.class)
					.resolve("target/test-classes"));


		// Approach 1: Overwrite modified file
		// cu = sourceRoot.parse("org.jsoup.nodes",
		cu = sourceRoot.parse("org.jsoup." + module,
				targetFile);

		saveMutantToFile(targetPath, cu);

		// Approach 2: Copy from back code
		try {
			Runtime rt = Runtime.getRuntime();
			String[] commands = {"/bin/sh", "-c", "echo 1"};	// TODO: copy from backup
			Process proc = rt.exec(commands);
		} catch (IOException e) {
			System.out.println("[Error] file copy failed");
			System.out.println(e);
		}
		return killed;
	}

	/*
     * Save the generated to the given path
     * Return:
     *      true if saved successfully
     *      false if failed to save
     */
	private boolean saveMutantToFile(String path, CompilationUnit cu) {
		try {
			FileWriter writer = new FileWriter(path);
            writer.write(cu.toString());
			writer.close();
			return true;
        } catch (IOException e) {
			System.out.println("IOException");
			return false;
		}
	}

	private boolean mutantKilled() {
		Runtime rt = Runtime.getRuntime();
		String[] commands = {"/bin/sh", "-c", "cd jsoup && mvn test"};
		try {
			Process proc = rt.exec(commands);
			
			BufferedReader stdInput = new BufferedReader(new 
			InputStreamReader(proc.getInputStream()));
			
			BufferedReader stdError = new BufferedReader(new 
			InputStreamReader(proc.getErrorStream()));
			
			// Read the output from the command
			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				if (findFailNum(s) > 0) {
					System.out.println("********** Failure Found **********");
					return true;
				}
				if (findErrorNum(s) > 0) {
					System.out.println("********** Error Found **********");
					return true;
				}
			}
			
			// Read any errors from the attempted command
			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		}
		catch (IOException e) {
			System.out.println("Exception executing command");
			return true;
		}
		return false;
	}

	/*
     * Find number of failure in a message
     * Return:
     *      >= 0 if found Failure info
     *      == -1 if no Failure info
     * e.g. msg = "Failures: 4 success: 8"; return 4
     * e.g. msg = "success: 8"; return -1
     */
	private static int findFailNum(String msg) {
        Pattern pattern = Pattern.compile("Failures: (\\d+)");
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            String numStr = matcher.group().split("\\s+")[1];
            Integer failNum = Integer.parseInt(numStr);
            // System.out.println(matcher.group());
            // System.out.println(failNum);
            return failNum;
        }
        return -1;
    }

	/*
     * Find number of error in a message
     * Return:
     *      >= 0 if found Error info
     *      == -1 if no Error info
     * e.g. msg = "Errors: 4 success: 8"; return 4
     * e.g. msg = "success: 8"; return -1
     */
	private static int findErrorNum(String msg) {
        Pattern pattern = Pattern.compile("Errors: (\\d+)");
        Matcher matcher = pattern.matcher(msg);
        while (matcher.find()) {
            String numStr = matcher.group().split("\\s+")[1];
            Integer errNum = Integer.parseInt(numStr);
            // System.out.println(matcher.group());
            // System.out.println(failNum);
            return errNum;
        }
        return -1;
    }

}
