package edu.illinois.cs.analysis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class RemoveConditionalsMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(BinaryExpr n, Object arg) {
		super.visit(n, arg);
        if (n.getOperator() == BinaryExpr.Operator.EQUALS) {
            // a == b -> a == a (always true)
            System.out.print("\t**Mutated** Remove Conditionals: \"" + n.toString() + "\" -> \"");
            n.setRight(n.getLeft());
            // System.out.println(n.toString() + "\"");
        }
            
	}
}
