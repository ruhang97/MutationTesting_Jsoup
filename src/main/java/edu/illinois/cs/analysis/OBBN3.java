package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;

public class OBBN3 extends VoidVisitorAdapter
{
	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(BinaryExpr n, Object arg) {
		super.visit(n, arg);
        if (n.getOperator().equals(BinaryExpr.Operator.AND) ||
            n.getOperator().equals(BinaryExpr.Operator.OR)) {
            n.setLeft(n.getRight());
			return;
        }
	}
}