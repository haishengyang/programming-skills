package org.example.demo1.designpatterns.composite;

/**
 * 组合模式 - 叶子节点 (Leaf)
 * 
 * 表示文件系统中的文件，是组合结构中的叶子节点
 * 叶子节点没有子节点，实现了组件接口的基本操作
 */
public class File extends FileSystemComponent {
    
    private String content;
    private String extension;
    
    public File(String name, int size) {
        super(name);
        this.size = size;
        this.content = "";
        
        // 提取文件扩展名
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            this.extension = name.substring(dotIndex + 1).toLowerCase();
        } else {
            this.extension = "";
        }
    }
    
    public File(String name, int size, String content) {
        this(name, size);
        this.content = content != null ? content : "";
    }
    
    @Override
    public void display(int depth) {
        String indent = getIndent(depth);
        String icon = getFileIcon();
        System.out.println(indent + icon + " " + name + " (" + size + " bytes)");
        
        // 如果有内容，显示内容预览
        if (!content.isEmpty()) {
            String preview = content.length() > 50 ? 
                content.substring(0, 50) + "..." : content;
            System.out.println(indent + "  Content: " + preview);
        }
    }
    
    @Override
    public int getSize() {
        return size;
    }
    
    @Override
    public String getType() {
        return "File (" + extension.toUpperCase() + ")";
    }
    
    /**
     * 根据文件扩展名返回对应的图标
     */
    private String getFileIcon() {
        switch (extension) {
            case "txt":
            case "md":
                return "📄";
            case "java":
            case "py":
            case "js":
                return "💻";
            case "jpg":
            case "png":
            case "gif":
                return "🖼️";
            case "mp3":
            case "wav":
                return "🎵";
            case "mp4":
            case "avi":
                return "🎬";
            case "pdf":
                return "📕";
            case "zip":
            case "rar":
                return "📦";
            default:
                return "📄";
        }
    }
    
    // 文件特有的方法
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content != null ? content : "";
        // 更新文件大小（简化计算）
        this.size = this.content.length();
    }
    
    public String getExtension() {
        return extension;
    }
    
    /**
     * 读取文件内容
     */
    public String read() {
        System.out.println("Reading file: " + name);
        return content;
    }
    
    /**
     * 写入文件内容
     */
    public void write(String newContent) {
        System.out.println("Writing to file: " + name);
        setContent(newContent);
    }
    
    @Override
    public String toString() {
        return "File{name='" + name + "', size=" + size + ", extension='" + extension + "'}";
    }
}
