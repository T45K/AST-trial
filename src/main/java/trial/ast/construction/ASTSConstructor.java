package trial.ast.construction;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ASTSConstructor implements IASTConstructor {

    /**
     * ASTを構築するメソッド
     * 複数のファイルからまとめてCompilationUnitを生成する
     * @param pathList Javaファイルのパスのリスト
     * @return CompilationUnitのリスト
     */
    @Override
    public List<CompilationUnit> construct(final List<Path> pathList) {
        final String[] pathStringArray = pathList.stream()
                .map(Path::toString)
                .toArray(String[]::new);

        final List<CompilationUnit> unitList = new ArrayList<>();

        final FileASTRequestor myFileASTRequestor = new FileASTRequestor() {
            @Override
            public void acceptAST(final String sourceFilePath, final CompilationUnit ast) {
                unitList.add(ast);
            }
        };

        final ASTParser parser = AST.getParser();
        parser.createASTs(pathStringArray,null,null,myFileASTRequestor,new NullProgressMonitor());

        return unitList;
    }
}
