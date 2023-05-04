package edu.illinois.cs.analysis;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.function.UnaryOperator;

import com.github.javaparser.ast.body.MethodDeclaration;

import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.NameExpr;

import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.PrimitiveType;

import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;



public class InvertNegativesMutator extends VoidVisitorAdapter
{
	@Override
	// public void visit(UnaryExpr n, Object arg) {
	// 	super.visit(n, arg);
    //     Expression operand = n.getExpression();
    //     if (!(operand instanceof NameExpr)) {
    //         return;
    //     }
    //     NameExpr nameExpr = (NameExpr) operand;
    //     ResolvedValueDeclaration resolvedValueDeclaration = nameExpr.resolve();
    //     String type = resolvedValueDeclaration.getType().describe();
    //     if (type.equals("int") || type.equals("float")) {
    //         n.setOperator(UnaryExpr.Operator.PLUS);
    //         System.out.println("invert negtatives applied");
    //     }

	// }
    public void visit(MethodDeclaration n, Object arg) {
		super.visit(n, arg);
		
		// satisfying the listed constriants rather than all possible methods
        Type returnType = n.getType();
        if (!(returnType instanceof PrimitiveType)) {
            return;
        }
        PrimitiveType primitiveType = (PrimitiveType) returnType;
        if (primitiveType.getType() != PrimitiveType.Primitive.INT) {
            return;
        }

        if (!n.getBody().isPresent()){
            return;
        }
        BlockStmt body = n.getBody().get();
        for (Statement stmt : body.getStatements()) {
            if (!(stmt instanceof ReturnStmt)) {
                continue;
            }
            ReturnStmt returnStmt = (ReturnStmt) stmt;
            Expression expr = returnStmt.getExpression().orElse(new NullLiteralExpr());
            if (expr instanceof UnaryExpr) {
                UnaryExpr uExpr = (UnaryExpr) expr;
                if (uExpr.getOperator() == UnaryExpr.Operator.MINUS) {
                    uExpr.setOperator(UnaryExpr.Operator.MINUS);
                    ReturnStmt newReturnStmt = new ReturnStmt(uExpr);
                    int index = body.getStatements().indexOf(returnStmt);
                    body.getStatements().set(index, returnStmt);
                    System.out.println("inverted negatives");
                }
            }
        }
	}
}
