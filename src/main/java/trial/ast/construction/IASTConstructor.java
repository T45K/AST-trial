package trial.ast.construction;

import org.eclipse.jdt.core.dom.CompilationUnit;

import java.nio.file.Path;
import java.util.List;

public interface IASTConstructor {
    public List<CompilationUnit> construct(List<Path> pathList);
}
