package com.example.demo.ui.paging;

import java.util.Objects;

class ItemData {

    String id;
    String name;

    public ItemData(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemData itemData = (ItemData) o;
        return Objects.equals(id, itemData.id)
                && Objects.equals(name, itemData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
