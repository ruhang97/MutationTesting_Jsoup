package edu.illinois.cs.analysis;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
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
	public void visit(ExpressionStmt n, Object arg) {
		super.visit(n, arg);
		if (n.getExpression().isAssignExpr()) {
			AssignExpr expr = n.getExpression().asAssignExpr();
			if (expr.getValue().isMethodCallExpr()) {
				try {
					expr.setValue(replace(expr.getValue().asMethodCallExpr()));
				} catch (Exception e) {

				}
			}
		} else if (n.getExpression().isVariableDeclarationExpr()) {
			VariableDeclarationExpr expr = n.getExpression().asVariableDeclarationExpr();
			for (VariableDeclarator vd : expr.getVariables()) {
				if (vd.getInitializer().isPresent() && vd.getInitializer().get().isMethodCallExpr()) {
					try {
						vd.setInitializer(replace(vd.getInitializer().get().asMethodCallExpr()));
					} catch (Exception e) {
	
					}
                }
			}
		}
		// System.out.println(n.getExpression());

	}

	private Expression replace(MethodCallExpr expr) {
		ResolvedType type = expr.resolve().getReturnType();
		if (type.isPrimitive()) {
			if (type.asPrimitive().isBoolean()) {
				return new BooleanLiteralExpr(false);
			} else if (type.asPrimitive() == ResolvedPrimitiveType.CHAR) {
				return new CharLiteralExpr('\u0000');
			} else if (type.asPrimitive() == ResolvedPrimitiveType.FLOAT || type.asPrimitive() == ResolvedPrimitiveType.DOUBLE) {
				return new DoubleLiteralExpr("0.0");
			} else if (type.asPrimitive() == ResolvedPrimitiveType.INT || type.asPrimitive() == ResolvedPrimitiveType.BYTE || 
				type.asPrimitive() == ResolvedPrimitiveType.SHORT || type.asPrimitive() == ResolvedPrimitiveType.LONG ) {
				return new IntegerLiteralExpr("0");
			}
		}
		return new NullLiteralExpr();
	}
}