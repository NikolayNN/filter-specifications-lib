package by.nhorushko.filterspecification;

import java.util.NoSuchElementException;

class FieldNameIterator {

    private String[] source;
    private int currentIndex = 0;

    public FieldNameIterator(String fieldPath) {
        this.source = fieldPath.split("\\.");
    }

    public Item next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements in the iterator");
        }

        String token = source[currentIndex++];
        if (isCollectionColumn(token)) {
            return new Item(token.substring(1), Type.COLLECTION);
        }
        if (isClass(token)) {
            return parseClassName(token);
        }
        return new Item(token, Type.PROPERTY);
    }

    public boolean hasNext() {
        return currentIndex < source.length;
    }

    private boolean isCollectionColumn(String columnName) {
        return columnName.charAt(0) == FilterSpecificationConstants.COLLECTION_COLUMN_PREFIX;
    }

    private boolean isClass(String columnName) {
        return columnName.startsWith("t:");
    }

    private Item parseClassName(String initialToken) {
        StringBuilder className = new StringBuilder(initialToken.substring(2));
        while (currentIndex < source.length) {
            String token = source[currentIndex++];
            className.append(".").append(token);
            if (Character.isUpperCase(token.charAt(0))) {
                break;
            }
        }
        return new Item(className.toString(), Type.CLASS);
    }

    public static class Item {
        private String value;
        private Type type;

        public Item(String value, Type type) {
            this.value = value;
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public Type getType() {
            return type;
        }

        public boolean isCollection() {
            return Type.COLLECTION == type;
        }

        public boolean isClass() {
            return Type.CLASS == type;
        }

        public boolean isProperty() {
            return Type.PROPERTY == type;
        }
    }

    private enum Type {
        PROPERTY, CLASS, COLLECTION
    }
}
