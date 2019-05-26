package trial;

import org.eclipse.jdt.core.dom.CompilationUnit;
import trial.ast.ASTFactory;
import trial.ast.construction.IASTConstructor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(final String[] args) throws IOException {
        final Path directoryPath = Paths.get(args[0]);
        final List<Path> pathList = FileUtility.getAllJavaFilePath(directoryPath);
        final IASTConstructor astConstructor = ASTFactory.create(args[1]);
        final List<CompilationUnit> unitList = astConstructor.construct(pathList);

        for (final CompilationUnit unit : unitList) {
            final MyVisitor visitor = new MyVisitor();
            unit.accept(visitor);
        }
    }
}
