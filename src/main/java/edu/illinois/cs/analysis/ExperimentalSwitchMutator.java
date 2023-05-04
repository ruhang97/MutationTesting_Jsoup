package edu.illinois.cs.analysis;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ExperimentalSwitchMutator extends VoidVisitorAdapter
{

	/**
	 * The switch mutator finds the first label within a switch statement that differs from 
     * the default label. 
     * It mutates the switch statement by replacing the default label (wherever it is used) with this label. 
     * All the other labels are replaced by the default one.
	 */
	@Override
	public void visit(SwitchStmt n, Object arg) {
		super.visit(n, arg);
        NodeList<SwitchEntry> entries = n.getEntries();
        NodeList<Expression> firstLabels = null;
        // find first label
        // for (SwitchEntry entry : entries) {
        //     if (!entry.getLabels().isEmpty()) {
        //         System.out.println("**mutated** Switch: first label: " + entry.getLabels().get(0).toString());
        //         firstLabels = entry.getLabels();
        //         break;
        //     }
        // }

        for (SwitchEntry entry : entries) {
            if (!entry.getLabels().isEmpty()) {
                // all not default -> default 
                System.out.println("**mutated** Switch: " + entry.getLabels().get(0).toString());
                entry.getLabels().clear();
            } else {
                // replace default with a label
                continue;
            }
        }
        System.out.println(entries.toString());
	}
}
