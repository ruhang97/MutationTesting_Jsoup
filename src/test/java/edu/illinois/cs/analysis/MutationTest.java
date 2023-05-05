package edu.illinois.cs.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Path;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MutationTest
{
	public enum Mutator {
		// CONDITIONALS_BOUNDARY(new ConditionalsBoundaryMutator()),
		// CONSTANT_REPLACE1(new CRCR1());
		// CONSTANT_REPLACE2(new CRCR2());
		// CONSTANT_REPLACE3(new CRCR3());
		// CONSTANT_REPLACE4(new CRCR4());
		// CONSTANT_REPLACE5(new CRCR5());
		// CONSTANT_REPLACE6(new CRCR6());
		// CONSTRUCTOR_CALL(new ConstructorCalls());
		// INCREMENTS(new IncrementsMutator()),
		// INVERT_NEGATIVES(new InvertNegativesMutator()),
		// INLINE_CONSTANT(new InlineConstantMutator()),
		// MATH(new MathMutator()),
		// NEGATE_COND(new NegateConditionalsMutator()),
		// VOID_METHOD(new VoidMethodCallsMutator()),
		// NON_VOID_METHOD(new NonVoidMethodCallsMutator()),
		// EMPTY_RETURNS(new EmptyReturnsMutator()),
		// FALSE_RETURNS(new FalseReturnsMutator()),
		// REMOVE_INCREMENT(new RemoveIncrementsMutator()),
		// TRUE_RETURNS(new TrueReturnsMutator()),
		// NULL_RETURNS(new NullReturnsMutator()),
		// PRIMITIVE_RETURNS(new PrimitiveReturnsMutator()),
		// REMOVE_CONDITIONALS(new RemoveConditionalsMutator()),
		// SWITCH(new ExperimentalSwitchMutator()),
		NEGATION(new NegationMutator());
		// ARITH_OP_REPLACE(new ArithmeticOperatorReplaceMutator()),
		// ARITH_OP_DELETION(new ArithmeticOperatorDeletion()),
		// BITWISE(new BitwiseMutator()),
		// BITWISE2(new OBBN2()),
		// BITWISE3(new OBBN3()),
		// UNARY_OP_INSERTION(new UnaryOpInsertionMutator());

		VoidVisitorAdapter modifier;

		private Mutator(VoidVisitorAdapter modifier) {
			this.modifier = modifier;
		}
		public boolean exec(String module, String targetFile) {
			return modifyAndTryToKill(this.modifier, module, targetFile);
		}

	}

	@Test
	public void testJsoup() {
		int count = 0;
		int countKill = 0;
		String directoryPath = "./jsoup/src/main/java/org/jsoup";
		boolean allFiles = false;

		if (!allFiles) {
			// Mutate only one file
			for (Mutator mutator : Mutator.values()) {
				System.out.println("Running mutator " + mutator.toString());
				boolean killed = mutator.exec("nodes", "Entities.java");
				if (killed) countKill++;
				count++;
			}

		} else {
			// All Files
			for (Mutator mutator : Mutator.values()) {
				System.out.println("----------------");
				System.out.println("Running mutator " + mutator.toString());
				for (String file : getAllFiles(directoryPath)) {
					System.out.println("Mutating " + file);
					boolean killed = mutator.exec("", file);
					if (killed) countKill++;
					count++;
				}
				
				for (String module : getAllDir(directoryPath)) {
					System.out.println("Checking module " + module);

					for (String file : getAllFiles(directoryPath + "/" + module)) {
						System.out.println("Mutating " + file);
						boolean killed = mutator.exec(module, file);
						if (killed) countKill++;
						count++;
					}
				}
			}
		}
		double score = countKill * 100.0 / count;
		System.out.println("\n****************** mutation score = " + 
							score + "% ******************");
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
	private static boolean modifyAndTryToKill(VoidVisitorAdapter codeModifier, String module, String targetFile) {
		// Initialize the source root as the "target/test-classes" dir, which
		// includes the test resource information (i.e., the source code info
		// for Jsoup for this assignment) copied from src/test/resources
		// during test execution
		SourceRoot sourceRootTmp = new SourceRoot(
				CodeGenerationUtils.mavenModuleRoot(CodeParserTest.class)
					.resolve("target/test-classes"));
		SourceRoot targetRoot = new SourceRoot(
			CodeGenerationUtils.mavenModuleRoot(CodeParserTest.class)
			// .resolve("jsoup/src/main/java"));
			.resolve("jsoup/src/main/java/org/jsoup"));		// add module
		String targetPath = targetRoot.getRoot() + "/" + module + "/" + targetFile;	// add module
		String targetDir = targetRoot.getRoot() + "/" + module;	// add module
		
		CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
		
		combinedTypeSolver.add(new JavaParserTypeSolver(CodeGenerationUtils.mavenModuleRoot(CodeParserTest.class)
		.resolve("target/test-classes")));

		Path sourceCodePath = new File(targetDir).toPath();
		combinedTypeSolver.add(new JavaParserTypeSolver(sourceCodePath));

		// Path jarPath = new File(sourceRootTmp.getRoot()).toPath();
		combinedTypeSolver.add(new JarTypeSolver(sourceRootTmp.getRoot()));


        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setSymbolResolver(symbolSolver);
		// Initialize the source root as the "target/test-classes" dir, which
		// includes the test resource information (i.e., the source code info
		// for Jsoup for this assignment) copied from src/test/resources
		// during test execution
		SourceRoot sourceRoot = new SourceRoot(
				CodeGenerationUtils.mavenModuleRoot(CodeParserTest.class)
					.resolve("target/test-classes"), parserConfiguration);

		// Get the code representation for the specific java file using its
		// package and name info
		// CompilationUnit cu = sourceRoot.parse("org.jsoup.nodes",
		CompilationUnit cu = sourceRoot.parse("org.jsoup." + module,
				targetFile);

		String originalContent = cu.toString();
		// Apply our visitor class on the code representation for the given
		// Java file. In this way, our visit function(s) can be automatically
		// applied to all possible elements of the specified type(s).
		codeModifier.visit(cu, null);

		// String targetPath = targetRoot.getRoot() + "/org/jsoup/nodes/" + targetFile;
		saveMutantToFile(targetPath, cu);
		System.out.println("DEBUG " + targetPath);

		boolean killed = mutantKilled();
		System.out.println("Mutant Killed: " + Boolean.toString(killed) + "\n");
		
		sourceRoot = new SourceRoot(
				CodeGenerationUtils.mavenModuleRoot(CodeParserTest.class)
					.resolve("target/test-classes"));

		restoreFile(targetPath, originalContent);

		return killed;
	}

	/*
     * Save the generated to the given path
     * Return:
     *      true if saved successfully
     *      false if failed to save
     */
	private static boolean saveMutantToFile(String path, CompilationUnit cu) {
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

	private static boolean restoreFile(String path, String content) {
		try {
			FileWriter writer = new FileWriter(path);
            writer.write(content);
			writer.close();
			return true;
        } catch (IOException e) {
			System.out.println("IOException");
			return false;
		}
	}

	private static boolean mutantKilled() {
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
			String prev = null;
			while ((s = stdInput.readLine()) != null) {
				if (findFailNum(s) > 0) {
					if (prev != null) System.out.println(prev);
					System.out.println(s);
					System.out.println("********** Failure Found **********");
					return true;
				}
				if (findErrorNum(s) > 0) {
					if (prev != null) System.out.println(prev);
					System.out.println(s);
					System.out.println("********** Error Found **********");
					return true;
				}
				prev = s;
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

	/*
     * Return a list of all .java files in a directory
     */
	public List<String> getAllFiles(String directoryPath) {
		File folder = new File(directoryPath);
		File[] listOfFiles = folder.listFiles();
		LinkedList<String> list = new LinkedList<>();
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.matches(".+\\.java$")) {
					list.add(fileName);
				}
			}
		}
		return list;
	}

	/*
     * Return a list of all directories in a directory
     */
	public List<String> getAllDir(String directoryPath) {
		File folder = new File(directoryPath);
		File[] listOfFiles = folder.listFiles();
		LinkedList<String> list = new LinkedList<>();
		
		for (File file : listOfFiles) {
			if (file.isDirectory()) {
				String fileName = file.getName();
				list.add(fileName);
			}
		}
		return list;
	}
}
