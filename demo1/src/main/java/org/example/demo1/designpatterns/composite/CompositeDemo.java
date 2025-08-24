package org.example.demo1.designpatterns.composite;

/**
 * 组合模式演示类
 * 展示文件系统的组合模式实现
 */
public class CompositeDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 组合模式演示 - 文件系统 ===\n");
        
        // 创建根目录
        Directory root = new Directory("root");
        
        // 创建子目录
        Directory documents = new Directory("Documents");
        Directory pictures = new Directory("Pictures");
        Directory projects = new Directory("Projects");
        
        // 创建文件
        File readme = new File("README.md", 1024, "# 项目说明\n这是一个演示项目...");
        File config = new File("config.txt", 512, "server.port=8080\ndb.url=localhost");
        File photo1 = new File("vacation.jpg", 2048000);
        File photo2 = new File("family.png", 1536000);
        
        // 构建文件系统结构
        System.out.println("=== 构建文件系统结构 ===");
        root.add(documents);
        root.add(pictures);
        root.add(projects);
        root.add(readme);
        
        documents.add(config);
        pictures.add(photo1);
        pictures.add(photo2);
        
        // 创建项目子目录
        Directory javaProject = new Directory("JavaProject");
        Directory pythonProject = new Directory("PythonProject");
        projects.add(javaProject);
        projects.add(pythonProject);
        
        // 添加项目文件
        File mainJava = new File("Main.java", 2048, "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n}");
        File pomXml = new File("pom.xml", 1024, "<?xml version=\"1.0\"?>\n<project>...</project>");
        File mainPy = new File("main.py", 512, "def main():\n    print('Hello Python!')\n\nif __name__ == '__main__':\n    main()");
        
        javaProject.add(mainJava);
        javaProject.add(pomXml);
        pythonProject.add(mainPy);
        
        System.out.println();
        
        // 1. 显示完整的文件系统结构
        System.out.println("=== 1. 完整文件系统结构 ===");
        root.display(0);
        System.out.println();
        
        // 2. 演示统一接口的使用
        System.out.println("=== 2. 统一接口演示 ===");
        demonstrateUniformInterface(root);
        demonstrateUniformInterface(documents);
        demonstrateUniformInterface(readme);
        System.out.println();
        
        // 3. 搜索功能演示
        System.out.println("=== 3. 搜索功能演示 ===");
        searchDemo(root);
        System.out.println();
        
        // 4. 统计信息演示
        System.out.println("=== 4. 统计信息演示 ===");
        statisticsDemo(root);
        System.out.println();
        
        // 5. 动态操作演示
        System.out.println("=== 5. 动态操作演示 ===");
        dynamicOperationsDemo(root);
        System.out.println();
        
        // 6. 文件操作演示
        System.out.println("=== 6. 文件操作演示 ===");
        fileOperationsDemo(mainJava);
    }
    
    /**
     * 演示统一接口的使用
     * 客户端代码无需区分是文件还是目录
     */
    private static void demonstrateUniformInterface(FileSystemComponent component) {
        System.out.println("Component: " + component.getName());
        System.out.println("  Type: " + component.getType());
        System.out.println("  Size: " + component.getSize() + " bytes");
        System.out.println("  String representation: " + component.toString());
        System.out.println();
    }
    
    /**
     * 搜索功能演示
     */
    private static void searchDemo(Directory root) {
        String[] searchNames = {"README.md", "Pictures", "Main.java", "nonexistent.txt"};
        
        for (String name : searchNames) {
            FileSystemComponent found = root.find(name);
            if (found != null) {
                System.out.println("Found: " + found.getName() + " (" + found.getType() + ")");
            } else {
                System.out.println("Not found: " + name);
            }
        }
    }
    
    /**
     * 统计信息演示
     */
    private static void statisticsDemo(Directory root) {
        Directory.DirectoryStats stats = root.getStats();
        System.out.println("Root directory statistics: " + stats);
        
        System.out.println("Java files count: " + root.getFileCount("java"));
        System.out.println("Python files count: " + root.getFileCount("py"));
        System.out.println("Image files count: " + (root.getFileCount("jpg") + root.getFileCount("png")));
        System.out.println("All files count: " + root.getFileCount(null));
    }
    
    /**
     * 动态操作演示
     */
    private static void dynamicOperationsDemo(Directory root) {
        // 创建新的临时目录和文件
        Directory temp = new Directory("temp");
        File tempFile = new File("temp.txt", 256, "This is a temporary file");
        
        // 添加到根目录
        root.add(temp);
        temp.add(tempFile);
        
        System.out.println("After adding temp directory:");
        root.display(0);
        System.out.println();
        
        // 移除临时目录
        root.remove(temp);
        System.out.println("After removing temp directory:");
        root.display(0);
    }
    
    /**
     * 文件操作演示
     */
    private static void fileOperationsDemo(File file) {
        System.out.println("Original file content:");
        String content = file.read();
        System.out.println(content);
        System.out.println();
        
        // 修改文件内容
        String newContent = "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello Composite Pattern!\");\n    }\n}";
        file.write(newContent);
        
        System.out.println("Modified file content:");
        System.out.println(file.read());
        System.out.println("New file size: " + file.getSize() + " bytes");
    }
}
