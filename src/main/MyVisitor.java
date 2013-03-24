package main;

import java.util.Set;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class MyVisitor extends VoidVisitorAdapter {

	private Set<String> references;
	
	public MyVisitor(String className, Set<String> references){
		this.references= references;
	}
	
	@Override
	public void visit(ClassOrInterfaceType n, Object arg) {
		references.add(n.getName());
		super.visit(n, arg);
	}
	
	@Override
	public void visit(ImportDeclaration n, Object arg) {
		references.add(n.getName().getName());
		super.visit(n, arg);
	}	
	
	@Override
	public void visit(ExpressionStmt n, Object arg) {
		Expression exp= n.getExpression();
		parseExpression(exp);
		super.visit(n, arg);
	}
	
	@Override
	public void visit(MethodCallExpr n, Object arg) {
		if(n.getScope()!=null){
			Expression scope= n.getScope();
			parseExpression(scope);
			
		}
		if(n.getArgs()!=null){
			for(Expression expr : n.getArgs()){
				parseExpression(expr);
			}
		}
		super.visit(n, arg);
	}

	private void parseExpression(Expression exp){
		if(exp instanceof NameExpr){
			NameExpr nameExpr= (NameExpr)exp;
			references.add(nameExpr.getName());
		}
		if(exp instanceof FieldAccessExpr){
			FieldAccessExpr fieldAccessExpr= (FieldAccessExpr)exp;
			references.add(fieldAccessExpr.getScope().toString());
		}
		if(exp instanceof VariableDeclarationExpr){
			VariableDeclarationExpr variable= (VariableDeclarationExpr)exp;
			Type type= variable.getType();
			references.add(type.toString());
		}
	}
	
	@Override
	public void visit(ReturnStmt n, Object arg) {
		parseExpression(n.getExpr());
		super.visit(n, arg);
	}
	
}
