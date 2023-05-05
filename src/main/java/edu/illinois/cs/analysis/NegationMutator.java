package edu.illinois.cs.analysis;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.NameExpr;

import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

public class NegationMutator extends VoidVisitorAdapter
{
	@Override
	public void visit(NameExpr n, Object arg) {
		super.visit(n, arg);
        // Expression operand = n.getExpression();
        // if (!(operand instanceof NameExpr)) {
        //     return;
        // }

        // NameExpr nameExpr = (NameExpr) operand;
        String type = n.resolve().getType().describe();
        if (type.equals("int") || type.equals("float")) {
            // n.setOperator(UnaryExpr.Operator.MINUS);
            System.out.print("**mutated**" + n.toString() + " -> ");
            n.replace(new UnaryExpr(n, UnaryExpr.Operator.MINUS));
            System.out.println(n.toString());
        }
	}
}

