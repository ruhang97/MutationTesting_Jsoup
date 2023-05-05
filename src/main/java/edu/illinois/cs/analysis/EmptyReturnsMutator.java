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
import com.github.javaparser.ast.expr.StringLiteralExpr;

public class EmptyReturnsMutator extends VoidVisitorAdapter
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
        // System.out.println("[DEBUG]" + returnType.asString());
        if (returnType.asString().equals("String")) {
            // System.out.println("string altered");
            n.getBody().get().getStatements().clear();
            n.getBody().get().addStatement(new ReturnStmt(new StringLiteralExpr("")));
            
        } else if (returnType.asString().equals("int") || 
                    returnType.asString().equals("short") ||
                    returnType.asString().equals("long")) {
            // System.out.println("int altered");
            n.getBody().get().getStatements().clear();
            n.getBody().get().addStatement(new ReturnStmt("0"));

        } else if (returnType.asString().equals("double") || 
                    returnType.asString().equals("float")) {
            // System.out.println("double altered");
            n.getBody().get().getStatements().clear();
            n.getBody().get().addStatement(new ReturnStmt("0.0"));
        }
	}
}