package com.yayaway.narwhal.injection;

/**
 * Interface representing a contract for clients that contains a component for dependency injection.
 */
public interface HasComponent<T> {
    T getComponent();
}
