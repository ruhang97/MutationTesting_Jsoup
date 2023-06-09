/*
 * This mutator has other implementations: OBBN2.java, OBBN3.java
 */
package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;
public class BitwiseMutator extends VoidVisitorAdapter
{
	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(BinaryExpr n, Object arg) {
		super.visit(n, arg);
        if (n.getOperator().equals(BinaryExpr.Operator.AND)) {
            n.setOperator(BinaryExpr.Operator.OR);
			return;
        }
        if (n.getOperator().equals(BinaryExpr.Operator.OR)) {
            n.setOperator(BinaryExpr.Operator.AND);
			return;
        }
	}
}