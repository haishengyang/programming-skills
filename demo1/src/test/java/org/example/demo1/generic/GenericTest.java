package org.example.demo1.generic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * 泛型示例的单元测试
 */
public class GenericTest {

    @Test
    @DisplayName("测试泛型栈的基本功能")
    public void testGenericStack() {
        GenericStack<String> stack = new GenericStack<>();
        
        // 测试空栈
        assertTrue(stack.isEmpty());
        
        // 测试push和pop
        stack.push("first");
        stack.push("second");
        assertFalse(stack.isEmpty());
        
        assertEquals("second", stack.pop());
        assertEquals("first", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    @DisplayName("测试通配符PECS原则")
    public void testWildcards() {
        // 测试Producer extends
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        
        assertEquals(15.0, WildcardExamples.sum(integers), 0.001);
        assertEquals(6.6, WildcardExamples.sum(doubles), 0.001);
        
        // 测试Consumer super
        List<Number> numbers = new ArrayList<>();
        WildcardExamples.addNumbers(numbers);
        assertEquals(5, numbers.size());
        assertEquals(Integer.valueOf(1), numbers.get(0));
        
        // 测试无界通配符
        Set<String> set1 = new HashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> set2 = new HashSet<>(Arrays.asList("b", "c", "d"));
        assertEquals(2, WildcardExamples.numElementsInCommon(set1, set2));
    }

    @Test
    @DisplayName("测试泛型方法")
    public void testGenericMethods() {
        // 测试union方法
        Set<String> set1 = new HashSet<>(Arrays.asList("a", "b"));
        Set<String> set2 = new HashSet<>(Arrays.asList("b", "c"));
        Set<String> union = GenericMethods.union(set1, set2);
        
        assertEquals(3, union.size());
        assertTrue(union.containsAll(Arrays.asList("a", "b", "c")));
        
        // 测试max方法
        List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9);
        assertEquals(Integer.valueOf(9), GenericMethods.max(numbers));
        
        List<String> strings = Arrays.asList("apple", "banana", "cherry");
        assertEquals("cherry", GenericMethods.max(strings));
        
        // 测试Builder模式
        String result = new GenericMethods.ConcreteBuilder()
                .setName("Test")
                .setValue(100)
                .build();
        assertEquals("Built: Test with value 100", result);
    }

    @Test
    @DisplayName("测试类型安全容器")
    public void testTypeSafeContainer() {
        TypeErasureDemo.TypeSafeContainer container = new TypeErasureDemo.TypeSafeContainer();
        
        container.putFavorite(String.class, "Java");
        container.putFavorite(Integer.class, 42);
        container.putFavorite(Class.class, String.class);
        
        assertEquals("Java", container.getFavorite(String.class));
        assertEquals(Integer.valueOf(42), container.getFavorite(Integer.class));
        assertEquals(String.class, container.getFavorite(Class.class));
    }

    @Test
    @DisplayName("测试安全的varargs方法")
    public void testSafeVarargs() {
        List<String> result = TypeErasureDemo.asList("a", "b", "c");
        assertEquals(3, result.size());
        assertEquals(Arrays.asList("a", "b", "c"), result);
        
        // 测试flatten方法
        List<String> flattened = TypeErasureDemo.flatten(
            Arrays.asList("a", "b"),
            Arrays.asList("c", "d")
        );
        assertEquals(Arrays.asList("a", "b", "c", "d"), flattened);
    }
}
