package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ConstructorCalls extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(ObjectCreationExpr n, Object arg) {
		super.visit(n, arg);
		n.replace(new NullLiteralExpr());
	}
}
