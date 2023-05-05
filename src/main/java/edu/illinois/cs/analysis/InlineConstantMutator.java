package edu.illinois.cs.analysis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class InlineConstantMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(VariableDeclarationExpr n, Object arg) {
		super.visit(n, arg);
		if (n.getModifiers().contains(Modifier.finalModifier())) {
			return;
		}
		for (VariableDeclarator vd : n.getVariables()) {
			if (vd.getType().equals(PrimitiveType.booleanType())) {
				if (vd.getInitializer().isPresent() && vd.getInitializer().get().isBooleanLiteralExpr()) {
					boolean flipped = vd.getInitializer().get().equals(new BooleanLiteralExpr(false));
					vd.setInitializer(new BooleanLiteralExpr(flipped));
                    
                }
			}

			if (vd.getType().equals(PrimitiveType.intType())) {
				if (vd.getInitializer().isPresent() && vd.getInitializer().get().isIntegerLiteralExpr()){
					int value = vd.getInitializer().get().asIntegerLiteralExpr().asNumber().intValue();
					if (value == 1) {
						value = 0;
					} else if (value == -1) {
						value = 1;
					} else if (value == 5) {
						value = -1;
					} else {
						value++;
					}
					vd.setInitializer(new IntegerLiteralExpr(String.valueOf(value)));
				}
			}

			if (vd.getType().equals(PrimitiveType.longType())) {
				if (vd.getInitializer().isPresent() && vd.getInitializer().get().isLongLiteralExpr()){
					long value = vd.getInitializer().get().asLongLiteralExpr().asNumber().longValue();
					if (value == 1) {
						value = 0;
					} else {
						value++;
					}
					vd.setInitializer(new LongLiteralExpr(String.valueOf(value)));
				}
			}
		}

	}    
}
