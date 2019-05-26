package trial;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import java.util.List;

/**
 * ASTVisitorを継承したクラス
 * 継承したvisitメソッドに追記することで，各ノードに対していろいろできる
 */
public class MyVisitor extends ASTVisitor {

    @Override
    public boolean visit(final MethodDeclaration node) {
        System.out.println(node.getName());
        return super.visit(node);
    }

    @Override
    public boolean visit(final IfStatement node) {
        System.out.println(node);
        return super.visit(node);
    }

    /**
     * 走査中のASTNodeに存在するASTNodeを入れ替える
     * @param oldNode 古いノード
     * @param newNode 新しいノード
     */
    private void replaceNode(final ASTNode oldNode, final ASTNode newNode) {
        final StructuralPropertyDescriptor locationInParent = oldNode.getLocationInParent();
        final ASTNode copiedNode = ASTNode.copySubtree(oldNode.getAST(), newNode);

        if (locationInParent.isChildListProperty()) {
            final List siblings = (List) oldNode.getParent().getStructuralProperty(locationInParent);
            int replaceIndex = siblings.indexOf(oldNode);
            siblings.set(replaceIndex, copiedNode);
        } else if (locationInParent.isChildProperty()) {
            oldNode.getParent().setStructuralProperty(locationInParent, copiedNode);
        } else {
            throw new RuntimeException("can't replace node");
        }
    }
}
