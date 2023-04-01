package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.AssignExpr;
// import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.expr.StringLiteralExpr;

public class ArithmeticOperatorDeletion extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(AssignExpr n, Object arg) {
		super.visit(n, arg);
		if (n.getValue() instanceof BinaryExpr) {
            System.out.println("changing" + n.toString());
            BinaryExpr be = (BinaryExpr) n.getValue();
            n.setValue(be.getLeft());
            System.out.println("to" + n.toString());
        }
	}
}