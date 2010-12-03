package org.richfaces.demo.tree.adaptors;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
public class FileSystemNode {
    
    private static final Function<String, FileSystemNode> FACTORY = new Function<String, FileSystemNode>() {
        public FileSystemNode apply(String from) {
            return new FileSystemNode(from.substring(0, from.length() - 1));
        };
    };

    private static final Function<String, String> TO_SHORT_PATH = new Function<String, String>() {
        public String apply(String from) {
            int idx = from.lastIndexOf('/');

            if (idx < 0) {
                return from;
            }
            
            return from.substring(idx + 1);
        };
    };
    
    private String path;

    private List<FileSystemNode> directories;

    private List<String> files;
    
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
    
    public synchronized List<FileSystemNode> getDirectories() {
        if (directories == null) {
            directories = Lists.newArrayList();

            Iterables.addAll(directories, transform(filter(getResourcePaths(), containsPattern("/$")), FACTORY));
        }

        return directories;
    }

    public synchronized List<String> getFiles() {
        if (files == null) {
            files = new ArrayList<String>();
            
            Iterables.addAll(files, transform(filter(getResourcePaths(), not(containsPattern("/$"))), TO_SHORT_PATH));
        }
        
        return files;
    }

    private Iterable<String> getResourcePaths() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Set<String> resourcePaths = externalContext.getResourcePaths(this.path);
        
        if (resourcePaths == null) {
            resourcePaths = Collections.emptySet();
        }
        
        return resourcePaths;
    }
    
    public String getShortPath() {
        return shortPath;
    }

}