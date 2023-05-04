package edu.illinois.cs.analysis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class IncrementsMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(UnaryExpr n, Object arg) {
		super.visit(n, arg);
        if (n.getOperator() == UnaryExpr.Operator.POSTFIX_INCREMENT) { 
            // i++ -> i--
            n.setOperator(UnaryExpr.Operator.POSTFIX_DECREMENT);
        } else if (n.getOperator() == UnaryExpr.Operator.PREFIX_INCREMENT) {
            // ++i -> --i
            n.setOperator(UnaryExpr.Operator.PREFIX_DECREMENT);
        } else if (n.getOperator() == UnaryExpr.Operator.POSTFIX_DECREMENT) {
            // i-- -> i++
            n.setOperator(UnaryExpr.Operator.POSTFIX_INCREMENT);
        } else if (n.getOperator() == UnaryExpr.Operator.PREFIX_DECREMENT) {
            // --i -> ++i
            n.setOperator(UnaryExpr.Operator.PREFIX_INCREMENT);
        }
	}    
}
