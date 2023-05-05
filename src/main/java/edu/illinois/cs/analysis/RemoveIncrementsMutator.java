package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class RemoveIncrementsMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(UnaryExpr n, Object arg) {
		super.visit(n, arg);
        if (n.getOperator() == UnaryExpr.Operator.POSTFIX_INCREMENT || n.getOperator() == UnaryExpr.Operator.PREFIX_INCREMENT) {
            // System.out.println(n.toString());
            n.replace(n.getExpression());
        }
	}
}
