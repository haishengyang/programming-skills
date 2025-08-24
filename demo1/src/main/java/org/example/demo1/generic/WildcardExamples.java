package org.example.demo1.generic;

import java.util.*;

/**
 * 通配符使用示例
 * 参考 Effective Java Item 31: Use bounded wildcards to increase API flexibility
 */
public class WildcardExamples {

    /**
     * PECS原则演示：Producer-extends, Consumer-super
     */
    
    // Producer extends - 从集合中读取数据
    public static double sum(List<? extends Number> numbers) {
        double sum = 0.0;
        for (Number num : numbers) {
            sum += num.doubleValue();
        }
        return sum;
    }

    // Consumer super - 向集合中写入数据
    public static void addNumbers(List<? super Integer> list) {
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }
    }

    /**
     * 无界通配符示例
     */
    public static int numElementsInCommon(Set<?> s1, Set<?> s2) {
        int result = 0;
        for (Object o1 : s1) {
            if (s2.contains(o1)) {
                result++;
            }
        }
        return result;
    }

    /**
     * 泛型方法与通配符的对比
     */
    
    // 使用泛型方法 - 当需要类型参数之间的关系时
    public static <T> void swap(List<T> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    // 使用通配符 - 当不需要类型参数之间的关系时
    public static void reverse(List<?> list) {
        reverseHelper(list);
    }

    private static <T> void reverseHelper(List<T> list) {
        List<T> tmp = new ArrayList<>(list);
        for (int i = 0, n = list.size(); i < n; i++) {
            list.set(i, tmp.get(n - i - 1));
        }
    }

    /**
     * 演示方法
     */
    public static void demonstrateWildcards() {
        System.out.println("=== 通配符使用演示 ===");
        
        // Producer extends 演示
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        
        System.out.println("Integer sum: " + sum(integers));
        System.out.println("Double sum: " + sum(doubles));
        
        // Consumer super 演示
        List<Number> numbers = new ArrayList<>();
        addNumbers(numbers);
        System.out.println("Added numbers: " + numbers);
        
        // 无界通配符演示
        Set<String> set1 = new HashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> set2 = new HashSet<>(Arrays.asList("b", "c", "d"));
        System.out.println("Common elements: " + numElementsInCommon(set1, set2));
        
        // 泛型方法演示
        List<String> strings = new ArrayList<>(Arrays.asList("first", "second", "third"));
        System.out.println("Before swap: " + strings);
        swap(strings, 0, 2);
        System.out.println("After swap: " + strings);
        
        reverse(strings);
        System.out.println("After reverse: " + strings);
    }
}
