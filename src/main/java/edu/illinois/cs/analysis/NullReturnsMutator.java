package edu.illinois.cs.analysis;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class NullReturnsMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(MethodDeclaration n, Object arg) {
		super.visit(n, arg);
        if (!n.getType().isPrimitiveType()) {
            if (n.getBody().isPresent()) {
                BlockStmt block = n.getBody().get();
                for (ReturnStmt rtn : block.findAll(ReturnStmt.class)) {
                    rtn.setExpression(new NullLiteralExpr());
                }
            }
        }
	}
}
