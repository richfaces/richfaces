package org.richfaces.validator;

final class LibraryFunctionImplementation implements LibraryFunction {
    private final LibraryResource library;
    private final String functionName;

    LibraryFunctionImplementation(LibraryResource library, String functionName) {
        this.library = library;
        this.functionName = functionName;
    }

    LibraryFunctionImplementation(String library, String resource, String functionName) {
        this.library = new LibraryResource(library, resource);
        this.functionName = functionName;
    }

    public LibraryResource getResource() {
        return library;
    }

    public String getName() {
        return functionName;
    }
}