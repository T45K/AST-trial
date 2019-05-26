package trial.ast.construction;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;

import java.util.Map;

class ASTUtility {
    static ASTParser getParser() {
        final ASTParser parser = ASTParser.newParser(AST.JLS10);

        final Map<String, String> options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_10);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_10);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_10);

        parser.setCompilerOptions(options);

        return parser;
    }
}
