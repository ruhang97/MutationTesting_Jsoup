package edu.illinois.cs.analysis;

// import com.github.javaparser.StaticJavaParser;
// import com.github.javaparser.ast.CompilationUnit;
// import com.github.javaparser.ast.comments.BlockComment;
// import com.github.javaparser.ast.comments.JavadocComment;
// import com.github.javaparser.ast.comments.LineComment;
// import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
// import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;


public class TrueReturnsMutator extends VoidVisitorAdapter
{

	/**
	 * This visit function will be automatically applied to all binary
	 * expressions in the given Java file
	 */
	@Override
	public void visit(MethodDeclaration n, Object arg) {
		super.visit(n, arg);
		
		if (n.getBody().isEmpty()) {
            return;
        }
		// satisfying the listed constriants rather than all possible methods
        Type returnType = n.getType();
        if (returnType instanceof PrimitiveType) {
            PrimitiveType primitiveType = (PrimitiveType) returnType;
            if (primitiveType.getType() == PrimitiveType.Primitive.BOOLEAN) {
                n.getBody().get().getStatements().clear();
                n.getBody().get().addStatement(new ReturnStmt(new BooleanLiteralExpr(true)));
            }
        }
	}
}