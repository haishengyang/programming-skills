package org.example.demo1.designpatterns.composite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 组合模式测试类
 */
public class CompositeTest {
    
    private Directory root;
    private Directory documents;
    private File readme;
    private File config;
    
    @BeforeEach
    public void setUp() {
        // 创建测试用的文件系统结构
        root = new Directory("root");
        documents = new Directory("Documents");
        readme = new File("README.md", 1024, "# Test Project");
        config = new File("config.txt", 512, "port=8080");
        
        root.add(documents);
        root.add(readme);
        documents.add(config);
    }
    
    @Test
    public void testFileBasicOperations() {
        assertEquals("README.md", readme.getName());
        assertEquals(1024, readme.getSize());
        assertEquals("File (MD)", readme.getType());
        assertEquals("md", readme.getExtension());
        assertEquals("# Test Project", readme.getContent());
    }
    
    @Test
    public void testFileContentOperations() {
        String newContent = "# Updated Project";
        readme.write(newContent);
        
        assertEquals(newContent, readme.read());
        assertEquals(newContent.length(), readme.getSize());
    }
    
    @Test
    public void testDirectoryBasicOperations() {
        assertEquals("root", root.getName());
        assertEquals("Directory", root.getType());
        assertEquals(2, root.getChildCount());
        assertFalse(root.isEmpty());
    }
    
    @Test
    public void testDirectorySize() {
        // root包含documents目录和readme文件
        // documents包含config文件
        // 总大小应该是readme(1024) + config(512) = 1536
        assertEquals(1536, root.getSize());
        assertEquals(512, documents.getSize());
    }
    
    @Test
    public void testAddAndRemoveComponents() {
        File newFile = new File("test.txt", 256, "test content");
        
        // 测试添加
        documents.add(newFile);
        assertEquals(2, documents.getChildCount());
        assertEquals(768, documents.getSize()); // 512 + 256
        
        // 测试移除
        documents.remove(newFile);
        assertEquals(1, documents.getChildCount());
        assertEquals(512, documents.getSize());
    }
    
    @Test
    public void testGetChild() {
        FileSystemComponent child = root.getChild(0);
        assertEquals("Documents", child.getName());
        
        // 测试索引越界
        assertThrows(IndexOutOfBoundsException.class, () -> {
            root.getChild(10);
        });
    }
    
    @Test
    public void testFindComponent() {
        // 测试找到组件
        FileSystemComponent found = root.find("README.md");
        assertNotNull(found);
        assertEquals("README.md", found.getName());
        
        // 测试递归查找
        FileSystemComponent configFound = root.find("config.txt");
        assertNotNull(configFound);
        assertEquals("config.txt", configFound.getName());
        
        // 测试找不到的情况
        FileSystemComponent notFound = root.find("nonexistent.txt");
        assertNull(notFound);
    }
    
    @Test
    public void testFileCountByExtension() {
        File javaFile = new File("Main.java", 1024);
        File pythonFile = new File("script.py", 512);
        
        documents.add(javaFile);
        documents.add(pythonFile);
        
        assertEquals(1, root.getFileCount("java"));
        assertEquals(1, root.getFileCount("py"));
        assertEquals(1, root.getFileCount("md"));
        assertEquals(1, root.getFileCount("txt"));
        assertEquals(4, root.getFileCount(null)); // 所有文件
    }
    
    @Test
    public void testDirectoryStats() {
        File javaFile = new File("Main.java", 2048);
        Directory subDir = new Directory("SubDir");
        File subFile = new File("sub.txt", 256);
        
        documents.add(javaFile);
        documents.add(subDir);
        subDir.add(subFile);
        
        Directory.DirectoryStats stats = root.getStats();
        assertEquals(4, stats.getTotalFiles()); // readme, config, javaFile, subFile
        assertEquals(2, stats.getTotalDirectories()); // documents, subDir
        assertEquals(3840, stats.getTotalSize()); // 1024+512+2048+256
        assertEquals(2, stats.getMaxDepth()); // root->documents->subDir
    }
    
    @Test
    public void testClearDirectory() {
        assertFalse(documents.isEmpty());
        documents.clear();
        assertTrue(documents.isEmpty());
        assertEquals(0, documents.getChildCount());
        assertEquals(0, documents.getSize());
    }
    
    @Test
    public void testLeafNodeOperations() {
        // 叶子节点不支持组合操作
        assertThrows(UnsupportedOperationException.class, () -> {
            readme.add(new File("test.txt", 100));
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            readme.remove(config);
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            readme.getChild(0);
        });
    }
    
    @Test
    public void testSelfAdditionPrevention() {
        // 防止目录添加自己，避免循环引用
        assertThrows(IllegalArgumentException.class, () -> {
            root.add(root);
        });
    }
    
    @Test
    public void testNullComponentAddition() {
        // 防止添加null组件
        assertThrows(IllegalArgumentException.class, () -> {
            root.add(null);
        });
    }
    
    @Test
    public void testUniformInterface() {
        // 测试统一接口 - 客户端代码无需区分文件和目录
        FileSystemComponent[] components = {root, documents, readme, config};
        
        for (FileSystemComponent component : components) {
            assertNotNull(component.getName());
            assertNotNull(component.getType());
            assertTrue(component.getSize() >= 0);
            assertNotNull(component.toString());
            
            // find方法对所有组件都可用
            FileSystemComponent self = component.find(component.getName());
            assertEquals(component, self);
        }
    }
    
    @Test
    public void testFileExtensionHandling() {
        File noExtension = new File("README", 100);
        File multiDot = new File("archive.tar.gz", 200);
        File hiddenFile = new File(".gitignore", 50);
        
        assertEquals("", noExtension.getExtension());
        assertEquals("gz", multiDot.getExtension());
        assertEquals("", hiddenFile.getExtension()); // 隐藏文件没有扩展名
    }
}
