package trial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class MyVisitor extends ASTVisitor{
	
	//all clear modifier!!!

	public static void main(String[] args) {
		//ArrayList<CompilationUnit> units = new ArrayList<>();
		
		//String[] source = {"subject"};
		//String[] directory = {"subject"};
		File file = new File("subject");
		ArrayList<String> allJavaFile = new ArrayList<>();
		
		getAllJavaFile(file,allJavaFile);
		
		String[] source = allJavaFile.toArray(new String[allJavaFile.size()]);		
		
		
		FileASTRequestor requestor = new FileASTRequestor() {
			@Override
			public void acceptAST(String path,CompilationUnit unit) {
				unit.recordModifications();
			}
		};
		

		
		/*
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		Map<String, String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);
		parser.setEnvironment(new String[0], directory, null, true);
		*/
		//parser.createASTs(source, null, new String[] {}, requestor, new NullProgressMonitor());
		
		//units.forEach(s -> s.accept(new MyVisitor()));
		
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
			
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			parser.setStatementsRecovery(true);
			
			parser.setUnitName("aaa");
			String[] aStrings = {"subject"};
			parser.setEnvironment(aStrings,aStrings, null, false);
			CompilationUnit unit = (CompilationUnit)parser.createAST(new NullProgressMonitor());
			unit.recordModifications();
			unit.accept(new MyVisitor());
			/*
			
			IDocument document = new Document(code.toString());
			TextEdit edit = unit.rewrite(document, null);
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
			*/
		}
	}
	
	public void get() {

		MyAST.createASTParser(this);
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
	public boolean visit(SimpleName node) {
		if(node.resolveBinding() != null
		&& node.resolveBinding().getKind() == IBinding.VARIABLE) {
			System.out.println(node);
			IVariableBinding var = (IVariableBinding) node.resolveBinding();
			System.out.println(var.getDeclaringClass().getQualifiedName());
		}
		return super.visit(node);
	}
	
	/*
	@Override
	public boolean visit(MethodDeclaration node) {
		System.out.println(node.resolveBinding().getDeclaringClass().getQualifiedName());
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		System.out.println(node.resolveMethodBinding().getDeclaringClass().getQualifiedName());
		return super.visit(node);
	}
	*/
	/*
	@Override
	public boolean visit(TypeDeclaration node) {
		Iterator<Object> list = node.modifiers().iterator();
		while(list.hasNext()) {
			Object object = list.next();
			if(object instanceof Modifier) {
				Modifier modifier = (Modifier) object;
				if(modifier.isPrivate() || modifier.isProtected()) {
					list.remove();
				}
				if(modifier.isPublic()) {
					return super.visit(node);
				}
			}
		}
		node.modifiers().add(node.getAST().newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		return super.visit(node);
	}
	*/
	
	/*
	
	@Override
	public boolean visit(MethodDeclaration node) {
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
	
	@Override
	public boolean visit(FieldDeclaration node) {
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
	*/
	
	/*
	@Override
	public boolean visit(EnumDeclaration node) {
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
	*/
	
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
