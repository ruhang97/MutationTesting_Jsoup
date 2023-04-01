package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
public class ArithmeticOperatorReplaceMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(BinaryExpr n, Object arg) {
		super.visit(n, arg);
        if (!(n.getLeft() instanceof DoubleLiteralExpr && 
                n.getRight() instanceof DoubleLiteralExpr) || 
            !(n.getLeft() instanceof IntegerLiteralExpr && 
                n.getRight() instanceof IntegerLiteralExpr)    ) {
            return;
        }
        // + -> -
		if (n.getOperator().equals(BinaryExpr.Operator.PLUS)) {		
			n.setOperator(BinaryExpr.Operator.MINUS);
			return;
		}
        // - -> +
		if (n.getOperator().equals(BinaryExpr.Operator.MINUS)) {		
			n.setOperator(BinaryExpr.Operator.PLUS);
			return;
		}
        // * -> /
		if (n.getOperator().equals(BinaryExpr.Operator.MULTIPLY)) {		
			n.setOperator(BinaryExpr.Operator.DIVIDE);
			return;
		}
        // / -> *
		if (n.getOperator().equals(BinaryExpr.Operator.DIVIDE)) {		
			n.setOperator(BinaryExpr.Operator.MULTIPLY);
			return;
		}
	}
}