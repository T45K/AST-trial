package trial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public class MyVisitor extends ASTVisitor{

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		
		ArrayList<CompilationUnit> units = new ArrayList<>();
		
		String[] source = {"subject/T1.java"};
		String[] directory = {"subject"};
		
		FileASTRequestor requestor = new FileASTRequestor() {
			@Override
			public void acceptAST(String path,CompilationUnit unit) {
				units.add(unit);
			}
		};

		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		Map<String, String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);
		parser.setEnvironment(new String[0], directory, null, true);
		parser.createASTs(source, null, new String[] {}, requestor, new NullProgressMonitor());
		
		for(CompilationUnit unit : units) {
			MyVisitor visitor = new MyVisitor();
			unit.accept(visitor);
			System.out.println(unit);
		}
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		node.setExpression(node.getAST().newThisExpression());
		System.out.println(node.resolveMethodBinding().getDeclaringClass().getName());
		//System.out.println(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SimpleName node) {
		if(!(node.getParent() instanceof QualifiedName) && node.resolveBinding().getKind() == IBinding.TYPE && !node.getIdentifier().equals("T1")) {
			String q = node.resolveTypeBinding().getQualifiedName();
			if(q.contains("<")) {
				q = q.substring(0, q.indexOf("<") );
			}
			q = q.substring(0, q.lastIndexOf(".") );
			
			QualifiedName qualifiedName = node.getAST().newQualifiedName(node.getAST().newName(q), node.getAST().newSimpleName(node.getIdentifier()));
			
			replaceNode((Name)node, (Name)qualifiedName);
		}
		/*
		if(node.resolveTypeBinding() != null && node.resolveBinding().getKind() == IBinding.VARIABLE)
		System.out.println(node + " " + node.resolveTypeBinding().getName());
		*/
		return super.visit(node);
	}
	
	private void replaceNode(ASTNode target, ASTNode astNode) {
	    StructuralPropertyDescriptor locationInParent = target.getLocationInParent();

	    ASTNode copiedNode = ASTNode.copySubtree(target.getAST(), astNode);
	    
	    if (locationInParent.isChildListProperty()) {
	      List siblings = (List) target.getParent().getStructuralProperty(locationInParent);
	      int replaceIdx = siblings.indexOf(target);
	      siblings.set(replaceIdx, copiedNode);

	    } else if (locationInParent.isChildProperty()) {
	      target.getParent().setStructuralProperty(locationInParent, copiedNode);

	    } else {
	      throw new RuntimeException("can't replace node");
	    }
	}
}
