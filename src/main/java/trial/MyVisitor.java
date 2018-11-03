package trial;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MyVisitor extends ASTVisitor{

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		
		ArrayList<CompilationUnit> units = new ArrayList<>();
		
		String[] source = {"subject/T1.java","subject/T2.java"};
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
		}
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println(node.getName());
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SimpleName node) {
		// TODO 自動生成されたメソッド・スタブ
		try{
			System.out.println(node + " " + node.resolveTypeBinding().getName());
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println(node + " " + "null");
		}
		return super.visit(node);
	}
}
