package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.ast.type.Type;


public class NonVoidMethodCallsMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(MethodCallExpr n, Object arg) {
		super.visit(n, arg);
		System.out.println(n.toString());
		ResolvedType type = n.resolve().getReturnType();

		if (type.isVoid()){
			return;
		}

		if (type.isPrimitive()) {
			if (type.asPrimitive().isBoolean()) {
				n.replace(new BooleanLiteralExpr(false));
			} else if (type.asPrimitive() == ResolvedPrimitiveType.CHAR) {
				n.replace(new CharLiteralExpr('\u0000'));
			} else if (type.asPrimitive() == ResolvedPrimitiveType.FLOAT || type.asPrimitive() == ResolvedPrimitiveType.DOUBLE) {
				n.replace(new DoubleLiteralExpr("0.0"));
			} else if (type.asPrimitive() == ResolvedPrimitiveType.INT || type.asPrimitive() == ResolvedPrimitiveType.BYTE || 
				type.asPrimitive() == ResolvedPrimitiveType.SHORT || type.asPrimitive() == ResolvedPrimitiveType.LONG ) {
				n.replace(new IntegerLiteralExpr("0"));
			}
		} else {
			n.replace(new NullLiteralExpr());
		}
	}
}