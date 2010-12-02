package org.richfaces.demo.tree.adaptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class FileSystemNode {
    private String path;

    private List<FileSystemNode> children;

    private String shortPath;

    public FileSystemNode(String path) {
        this.path = path;
        int idx = path.lastIndexOf('/');
        if (idx != -1) {
            shortPath = path.substring(idx + 1);
        } else {
            shortPath = path;
        }
    }

    public synchronized List<FileSystemNode> getNodes() {
        if (children == null) {
            children = new ArrayList<FileSystemNode>();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            Set resourcePaths = externalContext.getResourcePaths(this.path);
            if (resourcePaths != null) {
                Object[] nodes = (Object[]) resourcePaths.toArray();
                for (Object node : nodes) {
                    String nodePath = node.toString();
                    if (nodePath.endsWith("/")) {
                        nodePath = nodePath.substring(0, nodePath.length() - 1);
                    }
                    children.add(new FileSystemNode(nodePath));
                }
            } 
        }
        return children;
    }

    public String getShortPath() {
        return shortPath;
    }

}