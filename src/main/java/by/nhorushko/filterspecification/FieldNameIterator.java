package by.nhorushko.filterspecification;

import java.util.NoSuchElementException;

class FieldNameIterator {

    private final String[] parts;
    private int currentIndex = 0;

    public FieldNameIterator(String fieldName) {
        this.parts = fieldName.split("\\.");
    }

    public Item next() {
        if (currentIndex >= parts.length) {
            throw new NoSuchElementException();
        }
        String current = parts[currentIndex++];
        if (isCollectionColumn(current)) {
            return new Item(current.substring(1), Type.COLLECTION);
        } else {
            return new Item(current, Type.PROPERTY);
        }
    }

    private boolean isCollectionColumn(String columnName) {
        return columnName.charAt(0) == FilterSpecificationConstants.COLLECTION_COLUMN_PREFIX;
    }

    public boolean hasNext() {
        return currentIndex < parts.length;
    }

    public static class Item {
        private final String value;
        private final Type type;

        public Item(String value, Type type) {
            this.value = value;
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public boolean isCollection() {
            return type == Type.COLLECTION;
        }
    }

    private enum Type {
        COLLECTION, PROPERTY
    }
}
