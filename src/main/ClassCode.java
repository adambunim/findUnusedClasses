package main;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.Type;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassCode {
	
	private String code;
	private String simpleName;
	private String className;
	private Set<String> references;
	private Set<String> referencesInWorkspace;
	private String group;
	private CompilationUnit cu;
	private TypeDeclaration typeDeclaration;
	
	public ClassCode(File file) throws Exception{	
		references= new HashSet<>();
		referencesInWorkspace= new HashSet<>();
		cu = JavaParser.parse(file);
		code= cu.toString();
		typeDeclaration= cu.getTypes().get(0);
		PackageDeclaration packageDeclaration= cu.getPackage();
		simpleName= typeDeclaration.getName();
		className= packageDeclaration.getName()+"."+simpleName;		
	}
	
	public void fillReferences(){
        List<BodyDeclaration> members = typeDeclaration.getMembers();
        if(members!=null){
	        for (BodyDeclaration member : members) {
	        	if (member instanceof FieldDeclaration) {
	        		FieldDeclaration field= (FieldDeclaration)member;
	        		Type type= field.getType();
	        		references.add(type.toString());
	            }
	        }
        }
        
		MyVisitor methodVisitor= new MyVisitor(className, references);
		methodVisitor.visit(cu, null);
	}
	
	public String getSimpleName(){
		return simpleName;	
	}	
	
	public String getClassName(){
		return className;	
	}	
	
	public String getCode(){
		return code;
	}
	
	public Set<String> getReferences(){
		return references;
	}
	
	public Set<String> getReferencesInWorspace(){
		return referencesInWorkspace;
	}
	
	@Override
	public String toString() {
		return className;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
}
