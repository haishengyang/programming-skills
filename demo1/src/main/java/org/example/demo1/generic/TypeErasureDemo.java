package org.example.demo1.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 类型擦除和类型安全演示
 * 参考 Effective Java Item 32: Combine generics and varargs judiciously
 */
public class TypeErasureDemo {

    /**
     * 演示类型擦除的影响
     */
    public static void demonstrateTypeErasure() {
        System.out.println("=== 类型擦除演示 ===");
        
        List<String> stringList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        
        // 运行时类型相同
        System.out.println("String list class: " + stringList.getClass());
        System.out.println("Integer list class: " + intList.getClass());
        System.out.println("Classes equal: " + stringList.getClass().equals(intList.getClass()));
    }

    /**
     * 不安全的泛型数组创建 - 展示为什么不能创建泛型数组
     */
    // 这会编译错误：Cannot create a generic array of List<String>
    // static List<String>[] stringLists = new List<String>[1];
    
    /**
     * 安全的替代方案
     */
    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        List<T> result = new ArrayList<>();
        Collections.addAll(result, elements);
        return result;
    }

    /**
     * 不安全的varargs使用 - 仅用于演示，实际不应使用
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(T... elements) {
        return elements; // 这是不安全的！
    }

    /**
     * 安全的varargs使用
     */
    @SafeVarargs
    public static <T> List<T> flatten(List<? extends T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }

    /**
     * 类型令牌模式 - 解决类型擦除问题
     */
    public static class TypeToken<T> {
        private final Type type;

        protected TypeToken() {
            Type superclass = getClass().getGenericSuperclass();
            if (superclass instanceof ParameterizedType) {
                this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
            } else {
                throw new RuntimeException("Missing type parameter.");
            }
        }

        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return "TypeToken{" + type + "}";
        }
    }

    /**
     * 使用类型令牌的容器
     */
    public static class TypeSafeContainer {
        private final Map<Class<?>, Object> favorites = new HashMap<>();

        public <T> void putFavorite(Class<T> type, T instance) {
            favorites.put(Objects.requireNonNull(type), instance);
        }

        public <T> T getFavorite(Class<T> type) {
            return type.cast(favorites.get(type));
        }
    }

    /**
     * 演示方法
     */
    public static void demonstrateTypeSafety() {
        System.out.println("\n=== 类型安全演示 ===");
        
        // 安全的varargs使用
        List<String> strings = asList("a", "b", "c");
        List<Integer> integers = asList(1, 2, 3);
        System.out.println("Safe varargs - strings: " + strings);
        System.out.println("Safe varargs - integers: " + integers);
        
        // 列表扁平化
        List<String> flattened = flatten(
            Arrays.asList("a", "b"),
            Arrays.asList("c", "d"),
            Arrays.asList("e", "f")
        );
        System.out.println("Flattened lists: " + flattened);
        
        // 类型令牌演示
        TypeToken<List<String>> listToken = new TypeToken<List<String>>() {};
        System.out.println("Type token: " + listToken);
        
        // 类型安全容器
        TypeSafeContainer container = new TypeSafeContainer();
        container.putFavorite(String.class, "Java");
        container.putFavorite(Integer.class, 42);
        container.putFavorite(Class.class, TypeSafeContainer.class);
        
        String favoriteString = container.getFavorite(String.class);
        Integer favoriteInteger = container.getFavorite(Integer.class);
        Class<?> favoriteClass = container.getFavorite(Class.class);
        
        System.out.println("Favorite string: " + favoriteString);
        System.out.println("Favorite integer: " + favoriteInteger);
        System.out.println("Favorite class: " + favoriteClass.getSimpleName());
    }
}
