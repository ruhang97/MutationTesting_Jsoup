package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.NameExpr;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;

public class NegationMutator extends VoidVisitorAdapter
{
    @Override
	public void visit(NameExpr n, Object arg) {
		super.visit(n, arg);
        try {
            ResolvedType type = n.resolve().getType();
            if (type.isPrimitive()) {
                for (ResolvedType t : ResolvedPrimitiveType.getNumericPrimitiveTypes()) {
                    if (type.asPrimitive() == t) {
                        if (n.getParentNode().isPresent()) {
                            n.getParentNode().get().replace(n, new UnaryExpr(n, UnaryExpr.Operator.MINUS));
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }
	}

    @Override
    public void visit(FieldAccessExpr n, Object arg) {
        super.visit(n, arg);

        try {
            ResolvedType type = n.resolve().getType();
            if (type.isPrimitive()) {
                for (ResolvedType t : ResolvedPrimitiveType.getNumericPrimitiveTypes()) {
                    if (type.asPrimitive() == t) {
                        if (n.getParentNode().isPresent()) {
                            n.getParentNode().get().replace(n, new UnaryExpr(n, UnaryExpr.Operator.MINUS));
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void visit(ArrayAccessExpr n, Object arg) {
        super.visit(n, arg);

        try {
            ResolvedType type = n.calculateResolvedType();
            if (type.isPrimitive()) {
                for (ResolvedType t : ResolvedPrimitiveType.getNumericPrimitiveTypes()) {
                    if (type.asPrimitive() == t) {
                        if (n.getParentNode().isPresent()) {
                            n.getParentNode().get().replace(n, new UnaryExpr(n, UnaryExpr.Operator.MINUS));
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}

