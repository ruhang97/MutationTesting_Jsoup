package edu.illinois.cs.analysis;

// import com.github.javaparser.StaticJavaParser;
// import com.github.javaparser.ast.CompilationUnit;
// import com.github.javaparser.ast.comments.BlockComment;
// import com.github.javaparser.ast.comments.JavadocComment;
// import com.github.javaparser.ast.comments.LineComment;
// import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
// import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;


public class VoidMethodCallsMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(MethodDeclaration n, Object arg) {
		super.visit(n, arg);
        for (Statement stmt : n.getBody().get().getStatements()) {
            if (!stmt.isReturnStmt() && stmt.getChildNodes().size() == 1 &&
                stmt.getChildNodes().get(0).getClass().equals(MethodCallExpr.class)) {
                    stmt.replace(new EmptyStmt());
            }
        }
	}
}