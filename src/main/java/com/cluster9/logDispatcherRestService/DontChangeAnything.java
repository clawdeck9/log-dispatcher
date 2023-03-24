package com.cluster9.logDispatcherRestService;

@FunctionalInterface
public interface DontChangeAnything<T> {
	
	T writeAComment(T t);
	
	default T writeADefaultComment(T t) {
		System.out.println("functional interface default comment");
		return t;
	}

}
