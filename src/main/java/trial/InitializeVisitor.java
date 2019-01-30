package trial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class InitializeVisitor extends ASTVisitor {
	private boolean modifier;
	private boolean initialize;
	
	public static void main(String[] args) {
		new InitializeVisitor().run();
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		modifier = true;
		initialize = true;
		
		File file = new File("C:\\ex\\row-data\\forEx\\ex\\org");
		ArrayList<String> allJavaFile = new ArrayList<>();
		
		getAllJavaFile(file,allJavaFile);
		System.out.println(allJavaFile.size());
		
		String[] source = allJavaFile.toArray(new String[allJavaFile.size()]);
		
		for(int i = 0;i<source.length;i++) {
			StringBuilder code = new StringBuilder();
			try {
				BufferedReader buf = Files.newBufferedReader(Paths.get(source[i]));
				int c;
				while((c = buf.read()) != -1)
					code.append((char) c);
				buf.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			ASTParser parser = ASTParser.newParser(AST.JLS10);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final Map<String, String> options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_10);
		    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_10);
		    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_10);
			parser.setCompilerOptions(options);
			parser.setSource(code.toString().toCharArray());
			CompilationUnit unit = (CompilationUnit)parser.createAST(new NullProgressMonitor());
			unit.recordModifications();
			unit.accept(new InitializeVisitor());
			
			IDocument document = new Document(code.toString());
			final Map<String,String> rewriteOption = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
			TextEdit edit = unit.rewrite(document, rewriteOption);
			try {
				edit.apply(document);
			} catch (MalformedTreeException | BadLocationException e) {
				e.printStackTrace();
			}
			try {
				BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(source[i]));
				bufferedWriter.write(document.get());
				bufferedWriter.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void getAllJavaFile(File directory,ArrayList<String> list) {
		File[] files = directory.listFiles();
		if(files == null) return;
		for(File file : files) {
			if(file == null) {
				continue;
			}else if(file.isDirectory()) {
				getAllJavaFile(file, list);
			}else if(file.toString().contains(".java")) {
				list.add(file.toString());
			}
		}
	}
	
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		if(node.getInitializer() == null) {
			VariableDeclarationStatement variableDeclarationStatement = null;
			try{
				variableDeclarationStatement = (VariableDeclarationStatement)node.getParent();
			}catch(ClassCastException e) {
				return true;
			}
			if((variableDeclarationStatement.getModifiers() & Modifier.FINAL) != 0) {
				return true;
			}
			Type type = variableDeclarationStatement.getType();
			if(type instanceof PrimitiveType) {
				if(type.toString().equals("boolean"))
					node.setInitializer(node.getAST().newBooleanLiteral(false));
				else
					node.setInitializer(node.getAST().newNumberLiteral("0"));
			}else {
				node.setInitializer(node.getAST().newNullLiteral());
			}
		}
		return super.visit(node);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(MethodDeclaration node) {
		if(!modifier) {
			return true;
		}
		
		if(node.isConstructor() && node.getParent() instanceof EnumDeclaration) {
			return false;
		}
		Iterator<Object> list = node.modifiers().iterator();
		while(list.hasNext()) {
			Object object = list.next();
			if(object instanceof Modifier) {
				Modifier modifier = (Modifier) object;
				if(modifier.isPrivate() || modifier.isProtected()) {
					list.remove();
				}
				if(modifier.isPublic()) {
					return false;
				}
			}
		}
		node.modifiers().add(node.getAST().newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		return false;
	}
}
