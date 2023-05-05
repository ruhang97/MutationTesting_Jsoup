package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.NameExpr;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

public class UOI1 extends VoidVisitorAdapter
{
	@Override
	public void visit(NameExpr n, Object arg) {
		super.visit(n, arg);

        // ResolvedValueDeclaration resolvedValueDeclaration = n.resolve();
        // String type = resolvedValueDeclaration.getType().describe();
        // if (type.equals("int") || type.equals("float")) {
        // if (true) {
        //     System.out.print("**mutated** Unary Op Insertion: " +
        //         n.toString() + " -> ");
        //     UnaryExpr unaryExpr = new UnaryExpr(n, UnaryExpr.Operator.MINUS);   // UOI3
        //     // UnaryExpr unaryExpr = new UnaryExpr(n, UnaryExpr.Operator.PREFIX_INCREMENT);     // UOI2
        //     // UnaryExpr unaryExpr = new UnaryExpr(n, UnaryExpr.Operator.POSTFIX_INCREMENT);    //UOI1
        //     n.replace(unaryExpr);
        //     // System.out.println(n.toString());
        // }

        try {
            ResolvedType type = n.resolve().getType();
            if (type.isPrimitive()) {
                for (ResolvedType t : ResolvedPrimitiveType.getNumericPrimitiveTypes()) {
                    if (type.asPrimitive() == t) {
                        if (n.getParentNode().isPresent()) {
                            n.getParentNode().get().replace(n, 
                                new UnaryExpr(n, UnaryExpr.Operator.POSTFIX_INCREMENT)); // UOI1
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {

        }
	}
}

