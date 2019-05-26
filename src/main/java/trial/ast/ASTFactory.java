package trial.ast;

import trial.ast.construction.ASTConstructor;
import trial.ast.construction.ASTSConstructor;
import trial.ast.construction.IASTConstructor;

public class ASTFactory {
    private static final String CREATE_AST_MODE = "ast";
    private static final String CREATE_ASTS_MODE = "asts";

    /**
     * ファクトリーメソッド
     * 2種類のAST構築方法のどちらかのインスタンスを返す
     * @param mode AST構築の仕方 "ast" or "asts"
     * @return AST構築メソッドを実装したオブジェクト
     */
    public static IASTConstructor create(final String mode) {
        switch (mode) {
            case CREATE_AST_MODE:
                return new ASTConstructor();
            case CREATE_ASTS_MODE:
                return new ASTSConstructor();
            default:
                throw new IllegalArgumentException("illegal mode selection");
        }
    }
}
