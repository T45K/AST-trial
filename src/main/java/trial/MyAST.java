package trial;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;;

public class MyAST extends ASTVisitor {
	public static ASTParser createASTParser(MyVisitor path) {
		FileASTRequestor myRequestor = new FileASTRequestor() {
			@Override
			public void acceptAST(String sourceFilePath, CompilationUnit ast) {
				// TODO 自動生成されたメソッド・スタブ
				path.get();
				super.acceptAST(sourceFilePath, ast);
			}
		};
		return ASTParser.newParser(AST.JLS10);
	}
}
