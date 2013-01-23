package org.richfaces.javascript;

import com.google.common.collect.ImmutableList;
import org.richfaces.resource.ResourceKey;

import java.util.Collections;

final class LibraryFunctionImplementation implements LibraryFunction {
    private final Iterable<ResourceKey> library;
    private final String functionName;

    LibraryFunctionImplementation(String functionName, Iterable<ResourceKey> dependencies) {
        this.library = ImmutableList.copyOf(dependencies);
        this.functionName = functionName;
    }

    LibraryFunctionImplementation(String functionName, String resource, String library) {
        this.library = Collections.singleton(ResourceKey.create(resource, library));
        this.functionName = functionName;
    }

    public Iterable<ResourceKey> getResources() {
        return library;
    }

    public String getName() {
        return functionName;
    }
}