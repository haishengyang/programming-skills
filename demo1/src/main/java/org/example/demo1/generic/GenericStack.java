package org.example.demo1.generic;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * 泛型栈实现 - 展示基本泛型类的使用
 */
public class GenericStack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public GenericStack() {
        // 不能创建泛型数组，需要强制转换
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E item) {
        ensureCapacity();
        elements[size++] = item;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        E result = elements[--size];
        elements[size] = null; // 消除过期引用
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    // 演示方法：打印栈中所有元素
    public void printAll() {
        System.out.print("Stack contents: [");
        for (int i = 0; i < size; i++) {
            System.out.print(elements[i]);
            if (i < size - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}
