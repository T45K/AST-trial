package trial.ast.construction;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ASTConstructor implements IASTConstructor {

    /**
     * ASTを構築するメソッド
     * 1ファイルごとにCompilationUnitを生成する
     * @param   pathList javaファイルのパスのリスト
     * @return CompilationUnitのリスト
     */
    @Override
    public List<CompilationUnit> construct(final List<Path> pathList) {
        return pathList.stream()
                .map(this::constructUnitFromJavaFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private CompilationUnit constructUnitFromJavaFile(final Path path) {
        final byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        final char[] contents = new String(bytes).toCharArray();

        final ASTParser parser = AST.getParser();
        parser.setSource(contents);

        return (CompilationUnit)parser.createAST(new NullProgressMonitor());
    }
}
