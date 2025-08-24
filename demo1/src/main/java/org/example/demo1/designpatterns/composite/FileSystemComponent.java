package org.example.demo1.designpatterns.composite;

/**
 * 组合模式 - 组件接口 (Component)
 * 
 * 定义了叶子节点和组合节点的统一接口
 * 这是组合模式的核心，确保客户端可以统一处理单个对象和组合对象
 */
public abstract class FileSystemComponent {
    
    protected String name;
    protected int size;
    
    public FileSystemComponent(String name) {
        this.name = name;
        this.size = 0;
    }
    
    // 基本操作 - 所有组件都需要实现
    public abstract void display(int depth);
    public abstract int getSize();
    public abstract String getType();
    
    // 组合操作 - 默认实现，叶子节点不支持
    public void add(FileSystemComponent component) {
        throw new UnsupportedOperationException("Cannot add component to a leaf node");
    }
    
    public void remove(FileSystemComponent component) {
        throw new UnsupportedOperationException("Cannot remove component from a leaf node");
    }
    
    public FileSystemComponent getChild(int index) {
        throw new UnsupportedOperationException("Cannot get child from a leaf node");
    }
    
    // 通用方法
    public String getName() {
        return name;
    }
    
    /**
     * 生成缩进字符串，用于显示层次结构
     */
    protected String getIndent(int depth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("  ");
        }
        return indent.toString();
    }
    
    /**
     * 搜索指定名称的组件
     */
    public FileSystemComponent find(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }
}
