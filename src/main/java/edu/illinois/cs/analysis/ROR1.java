package edu.illinois.cs.analysis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ROR1 extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(BinaryExpr n, Object arg) {
		super.visit(n, arg);
		if (n.getOperator().equals(BinaryExpr.Operator.LESS)) {		
			n.setOperator(BinaryExpr.Operator.LESS_EQUALS);
			return;
		}
		if (n.getOperator().equals(BinaryExpr.Operator.LESS_EQUALS)) {		
			n.setOperator(BinaryExpr.Operator.LESS);
			return;
		}
		if (n.getOperator().equals(BinaryExpr.Operator.GREATER)) {		
			n.setOperator(BinaryExpr.Operator.LESS);
			return;
		}
		if (n.getOperator().equals(BinaryExpr.Operator.GREATER_EQUALS)) {		
			n.setOperator(BinaryExpr.Operator.LESS);
			return;
		}
        if (n.getOperator().equals(BinaryExpr.Operator.EQUALS)) {		
			n.setOperator(BinaryExpr.Operator.LESS);
			return;
		}
		if (n.getOperator().equals(BinaryExpr.Operator.NOT_EQUALS)) {		
			n.setOperator(BinaryExpr.Operator.LESS);
			return;
		}
	}
}
