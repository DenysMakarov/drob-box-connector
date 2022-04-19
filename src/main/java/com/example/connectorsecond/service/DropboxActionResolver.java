package com.example.connectorsecond.service;

@FunctionalInterface
public interface DropboxActionResolver<T> {
    T perform() throws Exception;
}
