package by.nhorushko.filterspecification;

import java.util.NoSuchElementException;


class FieldNameIterator {

    private final String[] parts;
    private int currentIndex = 0;
    private int joinMaxIndex = -1;

    public FieldNameIterator(String fieldName) {
        this.parts = fieldName.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            if (isCollectionColumn(parts[i])) {
                parts[i] = parts[i].substring(1);
                joinMaxIndex = i;
            }
        }
    }

    private boolean isCollectionColumn(String columnName) {
        return columnName.charAt(0) == FilterSpecificationConstants.COLLECTION_COLUMN_PREFIX;
    }

    public Item next() {
        if (currentIndex >= parts.length) {
            throw new NoSuchElementException();
        }
        Item item = new Item(parts[currentIndex], determinateType(currentIndex));
        currentIndex++;
        return item;
    }

    private Type determinateType(int index) {
        if (index <= joinMaxIndex) {
            return Type.JOIN;
        } else {
            return Type.GET;
        }
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

        public boolean isJoin() {
            return type == Type.JOIN;
        }
    }

    private enum Type {
        JOIN, GET
    }
}
