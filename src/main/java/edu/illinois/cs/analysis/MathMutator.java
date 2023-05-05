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
public class MathMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(BinaryExpr n, Object arg) {
		super.visit(n, arg);
        if (n.getLeft() instanceof DoubleLiteralExpr ||
            n.getRight() instanceof DoubleLiteralExpr ||
            n.getLeft() instanceof IntegerLiteralExpr ||
            n.getLeft() instanceof IntegerLiteralExpr) {
                
            if (n.getOperator().equals(BinaryExpr.Operator.PLUS)) {		
                // + -> -
                n.setOperator(BinaryExpr.Operator.MINUS);
            } else if (n.getOperator().equals(BinaryExpr.Operator.MINUS)) {		
                // - -> +
                n.setOperator(BinaryExpr.Operator.PLUS);
            } else if (n.getOperator().equals(BinaryExpr.Operator.MULTIPLY)) {		
                // * -> /
                n.setOperator(BinaryExpr.Operator.DIVIDE);
            } else if (n.getOperator().equals(BinaryExpr.Operator.DIVIDE)) {		
                // / -> *
                n.setOperator(BinaryExpr.Operator.MULTIPLY);
            } else if (n.getOperator().equals(BinaryExpr.Operator.REMAINDER)) {
                // % -> *
                n.setOperator(BinaryExpr.Operator.MULTIPLY);
            } else if (n.getOperator().equals(BinaryExpr.Operator.BINARY_AND)) {
                // & -> |
                n.setOperator(BinaryExpr.Operator.BINARY_OR);
            } else if (n.getOperator().equals(BinaryExpr.Operator.BINARY_OR)) {
                // | -> &
                n.setOperator(BinaryExpr.Operator.BINARY_AND);
            } else if (n.getOperator().equals(BinaryExpr.Operator.XOR)) {
                // ^ -> &
                n.setOperator(BinaryExpr.Operator.BINARY_AND);
            } else if (n.getOperator().equals(BinaryExpr.Operator.LEFT_SHIFT)) {
                // << -> >>
                n.setOperator(BinaryExpr.Operator.SIGNED_RIGHT_SHIFT);
            } else if (n.getOperator().equals(BinaryExpr.Operator.SIGNED_RIGHT_SHIFT)) {
                // >> -> <<
                n.setOperator(BinaryExpr.Operator.LEFT_SHIFT);
            } else if (n.getOperator().equals(BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT)) {
                // >>> -> <<
                n.setOperator(BinaryExpr.Operator.LEFT_SHIFT);
            } 
            
        }
	}
}