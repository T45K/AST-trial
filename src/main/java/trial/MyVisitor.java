package trial;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import java.util.List;

public class MyVisitor extends ASTVisitor {

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
