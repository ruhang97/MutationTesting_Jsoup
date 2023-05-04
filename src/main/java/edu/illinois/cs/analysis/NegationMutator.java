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
	public void visit(UnaryExpr n, Object arg) {
		super.visit(n, arg);
        Expression operand = n.getExpression();
        if (!(operand instanceof NameExpr)) {
            return;
        }

        NameExpr nameExpr = (NameExpr) operand;
        ResolvedValueDeclaration resolvedValueDeclaration = nameExpr.resolve();
        String type = resolvedValueDeclaration.getType().describe();
        if (type.equals("int") || type.equals("float")) {
            n.setOperator(UnaryExpr.Operator.MINUS);
            System.out.println("negation applied");
        }
	}
}

