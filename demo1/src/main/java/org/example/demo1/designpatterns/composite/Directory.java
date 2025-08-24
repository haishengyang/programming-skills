package org.example.demo1.designpatterns.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式 - 组合节点 (Composite)
 * 
 * 表示文件系统中的目录，可以包含文件和子目录
 * 组合节点可以有子节点，实现了组件接口的所有操作
 */
public class Directory extends FileSystemComponent {
    
    private List<FileSystemComponent> children;
    private int maxDepth;
    
    public Directory(String name) {
        super(name);
        this.children = new ArrayList<>();
        this.maxDepth = Integer.MAX_VALUE;
    }
    
    public Directory(String name, int maxDepth) {
        this(name);
        this.maxDepth = maxDepth;
    }
    
    @Override
    public void display(int depth) {
        String indent = getIndent(depth);
        System.out.println(indent + "📁 " + name + "/ (" + children.size() + " items, " + getSize() + " bytes total)");
        
        // 递归显示子组件
        if (depth < maxDepth) {
            for (FileSystemComponent child : children) {
                child.display(depth + 1);
            }
        } else if (!children.isEmpty()) {
            System.out.println(getIndent(depth + 1) + "... (" + children.size() + " items)");
        }
    }
    
    @Override
    public int getSize() {
        int totalSize = 0;
        for (FileSystemComponent child : children) {
            totalSize += child.getSize();
        }
        return totalSize;
    }
    
    @Override
    public String getType() {
        return "Directory";
    }
    
    @Override
    public void add(FileSystemComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        
        // 防止添加自己，避免循环引用
        if (component == this) {
            throw new IllegalArgumentException("Cannot add directory to itself");
        }
        
        children.add(component);
        System.out.println("Added " + component.getName() + " to directory " + this.name);
    }
    
    @Override
    public void remove(FileSystemComponent component) {
        if (children.remove(component)) {
            System.out.println("Removed " + component.getName() + " from directory " + this.name);
        } else {
            System.out.println("Component " + component.getName() + " not found in directory " + this.name);
        }
    }
    
    @Override
    public FileSystemComponent getChild(int index) {
        if (index < 0 || index >= children.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + children.size());
        }
        return children.get(index);
    }
    
    @Override
    public FileSystemComponent find(String name) {
        // 首先检查自己
        if (this.name.equals(name)) {
            return this;
        }
        
        // 递归搜索子组件
        for (FileSystemComponent child : children) {
            FileSystemComponent found = child.find(name);
            if (found != null) {
                return found;
            }
        }
        
        return null;
    }
    
    /**
     * 获取所有子组件
     */
    public List<FileSystemComponent> getChildren() {
        return new ArrayList<>(children); // 返回副本，防止外部修改
    }
    
    /**
     * 获取子组件数量
     */
    public int getChildCount() {
        return children.size();
    }
    
    /**
     * 检查目录是否为空
     */
    public boolean isEmpty() {
        return children.isEmpty();
    }
    
    /**
     * 清空目录
     */
    public void clear() {
        children.clear();
        System.out.println("Cleared directory " + this.name);
    }
    
    /**
     * 获取指定类型的文件数量
     */
    public int getFileCount(String extension) {
        int count = 0;
        for (FileSystemComponent child : children) {
            if (child instanceof File) {
                File file = (File) child;
                if (extension == null || extension.isEmpty() || 
                    file.getExtension().equalsIgnoreCase(extension)) {
                    count++;
                }
            } else if (child instanceof Directory) {
                count += ((Directory) child).getFileCount(extension);
            }
        }
        return count;
    }
    
    /**
     * 获取目录统计信息
     */
    public DirectoryStats getStats() {
        return new DirectoryStats(this);
    }
    
    /**
     * 目录统计信息内部类
     */
    public static class DirectoryStats {
        private int totalFiles;
        private int totalDirectories;
        private int totalSize;
        private int maxDepth;
        
        public DirectoryStats(Directory directory) {
            calculateStats(directory, 0);
        }
        
        private void calculateStats(Directory directory, int currentDepth) {
            maxDepth = Math.max(maxDepth, currentDepth);
            
            for (FileSystemComponent child : directory.children) {
                if (child instanceof File) {
                    totalFiles++;
                    totalSize += child.getSize();
                } else if (child instanceof Directory) {
                    totalDirectories++;
                    calculateStats((Directory) child, currentDepth + 1);
                }
            }
        }
        
        @Override
        public String toString() {
            return String.format("Stats{files=%d, directories=%d, totalSize=%d bytes, maxDepth=%d}", 
                               totalFiles, totalDirectories, totalSize, maxDepth);
        }
        
        // Getters
        public int getTotalFiles() { return totalFiles; }
        public int getTotalDirectories() { return totalDirectories; }
        public int getTotalSize() { return totalSize; }
        public int getMaxDepth() { return maxDepth; }
    }
    
    @Override
    public String toString() {
        return "Directory{name='" + name + "', children=" + children.size() + ", size=" + getSize() + "}";
    }
}
