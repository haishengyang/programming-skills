package org.example.demo1.generic;

/**
 * 泛型综合演示主类
 * 运行所有泛型示例
 */
public class GenericDemo {
    
    public static void main(String[] args) {
        System.out.println("Java泛型综合演示");
        System.out.println("================");
        
        // 1. 泛型栈演示
        demonstrateGenericStack();
        
        // 2. 通配符演示
        WildcardExamples.demonstrateWildcards();
        
        // 3. 泛型方法演示
        GenericMethods.demonstrateGenericMethods();
        
        // 4. 类型擦除演示
        TypeErasureDemo.demonstrateTypeErasure();
        TypeErasureDemo.demonstrateTypeSafety();
        
        System.out.println("\n演示完成！");
    }
    
    private static void demonstrateGenericStack() {
        System.out.println("=== 泛型栈演示 ===");
        
        // 字符串栈
        GenericStack<String> stringStack = new GenericStack<>();
        stringStack.push("First");
        stringStack.push("Second");
        stringStack.push("Third");
        
        System.out.println("String stack:");
        stringStack.printAll();
        
        System.out.println("Popping: " + stringStack.pop());
        stringStack.printAll();
        
        // 整数栈
        GenericStack<Integer> intStack = new GenericStack<>();
        intStack.push(1);
        intStack.push(2);
        intStack.push(3);
        
        System.out.println("\nInteger stack:");
        intStack.printAll();
        
        System.out.println("Popping: " + intStack.pop());
        intStack.printAll();
        
        System.out.println();
    }
}
