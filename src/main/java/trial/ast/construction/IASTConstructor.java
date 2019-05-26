package trial.ast.construction;

import org.eclipse.jdt.core.dom.CompilationUnit;

import java.nio.file.Path;
import java.util.List;

public interface IASTConstructor {
    /**
     * 与えられたJavaファイルのパスから，そのファイルの内容に即したCompilationUnitを構築する
     * @param pathList Javaファイルのパスのリスト
     * @return CompilationUnitのリスト
     */
    public List<CompilationUnit> construct(List<Path> pathList);
}
