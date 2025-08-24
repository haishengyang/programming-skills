package org.example.demo1.generic;

import java.util.*;

/**
 * 泛型方法示例
 * 参考 Effective Java Item 30: Favor generic methods
 */
public class GenericMethods {

    /**
     * 基本泛型方法 - 类型推断
     */
    public static <T> Set<T> union(Set<T> s1, Set<T> s2) {
        Set<T> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }

    /**
     * 有界类型参数
     */
    public static <T extends Comparable<T>> T max(Collection<T> collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Empty collection");
        }
        
        T candidate = null;
        for (T element : collection) {
            if (candidate == null || element.compareTo(candidate) > 0) {
                candidate = element;
            }
        }
        return candidate;
    }

    /**
     * 多个类型参数
     */
    public static <K, V> Map<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * 递归类型限制 - 展示复杂的泛型约束
     */
    public static <T extends Comparable<T>> List<T> sort(List<T> list) {
        List<T> result = new ArrayList<>(list);
        Collections.sort(result);
        return result;
    }

    /**
     * 泛型单例工厂模式
     */
    private static final Set<Object> EMPTY_SET = new HashSet<>();

    @SuppressWarnings("unchecked")
    public static <T> Set<T> emptySet() {
        return (Set<T>) EMPTY_SET;
    }

    /**
     * 自限定类型 - 展示Builder模式中的应用
     */
    public static abstract class Builder<T extends Builder<T>> {
        protected String name;
        protected int value;

        public T setName(String name) {
            this.name = name;
            return self();
        }

        public T setValue(int value) {
            this.value = value;
            return self();
        }

        protected abstract T self();
        public abstract Object build();
    }

    public static class ConcreteBuilder extends Builder<ConcreteBuilder> {
        @Override
        protected ConcreteBuilder self() {
            return this;
        }

        @Override
        public String build() {
            return "Built: " + name + " with value " + value;
        }
    }

    /**
     * 演示方法
     */
    public static void demonstrateGenericMethods() {
        System.out.println("=== 泛型方法演示 ===");
        
        // 基本泛型方法
        Set<String> guys = new HashSet<>(Arrays.asList("Tom", "Dick", "Harry"));
        Set<String> stooges = new HashSet<>(Arrays.asList("Larry", "Moe", "Curly"));
        Set<String> aflCio = union(guys, stooges);
        System.out.println("Union result: " + aflCio);
        
        // 有界类型参数
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        System.out.println("Max string: " + max(strings));
        
        List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9);
        System.out.println("Max number: " + max(numbers));
        
        // 排序演示
        System.out.println("Original: " + numbers);
        System.out.println("Sorted: " + sort(numbers));
        
        // 泛型单例工厂
        Set<String> emptyStringSet = emptySet();
        Set<Integer> emptyIntSet = emptySet();
        System.out.println("Empty sets created: " + emptyStringSet.getClass());
        
        // 自限定类型演示
        String result = new ConcreteBuilder()
                .setName("Example")
                .setValue(42)
                .build();
        System.out.println("Builder result: " + result);
    }
}
