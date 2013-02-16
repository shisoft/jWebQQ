package iqq.comm;

import java.awt.Component;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import iqq.model.Member;
import iqq.service.MemberService;
import iqq.util.ThreadUtil;

/**
 * 自定义树描述类,将树的每个节点设置成不同的图标
 *
 * @author ChenZhiHui
 *
 */
public class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;
    private Member m = null;

    /**
     * 重写父类DefaultTreeCellRenderer的方法
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {

        //执行父类原型操作
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);

        setText(value.toString());

        if (sel) {
            setForeground(getTextSelectionColor());
        } else {
            setForeground(getTextNonSelectionColor());
        }

        //得到每个节点的TreeNode
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
        //得到每个节点的value
        if (value instanceof IconTreeNode) {
            Font f = new Font("宋体", Font.PLAIN, 14);
            this.setFont(f);
            setText(stringValue);
            this.setIconTextGap(10);

            IconTreeNode iconTreeNode = (IconTreeNode) value;

            if (iconTreeNode.getMember() != null) {
                m = iconTreeNode.getMember();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            m = MemberService.getInstance().getMemberAccount(m);
                            m.setFace(MemberService.getInstance().downloadFace(m));
                        } catch (Exception ex) {
                            Logger.getLogger(MyDefaultTreeCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                if (m != null && !m.isIsDownloadInfo()) {
                    ThreadUtil.submit(r);
                }

                try {
                    setIcon(m.getFace());
                } catch (Exception ex) {
                    Logger.getLogger(MyDefaultTreeCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                }
                //重复判断头像是否已经成功加载
                if (getIcon() == null) {
                    if (m != null) {
                        ThreadUtil.submit(r);
                    }
                }
            } else if (iconTreeNode.getGroup() != null) {
                setIcon(iconTreeNode.getGroup().getFace());
            }

        } else {
            Font f = new Font("宋体", Font.BOLD, 14);
            setText(stringValue);
            this.setFont(f);
            Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                    hasFocus);
            label.setBorder(border);
        }

        return this;
    }
}
