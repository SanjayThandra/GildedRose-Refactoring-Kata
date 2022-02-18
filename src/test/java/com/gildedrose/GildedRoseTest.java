package com.gildedrose;

import com.gildedrose.constants.ItemTypeUtility;
import com.gildedrose.domain.Item;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {
    GildedRose app = null;

    @BeforeEach
    public void setup() {
        app = new GildedRose(null);
    }

    @Test
    void foo() {
        Item[] items = new Item[]{new Item("foo", 0, 0)};
        app.setItems(items);
        app.updateQuality();
        assertEquals("foo", app.getItems()[0].getName());
    }

    @Test
    void sellInDateDecreases_butQualityCannotBeNegative() {
        app.setItems(new Item[]{new Item("foo", 0, 0)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item("foo", -1, 0));
    }

    @Test
    void qualityDecreases() {
        app.setItems(new Item[]{new Item("foo", 10, 10)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item("foo", 9, 9));
    }

    @Test
    void qualityDecreasesFasterAfterSellInDateExpired() {
        app.setItems(new Item[]{new Item("foo", 0, 10)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item("foo", -1, 8));
    }

    @Test
    void item_conjured_decreasesInQuality_twiceTheSpeed() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.CONJURED, 3, 6)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.CONJURED, 2, 4));
    }

    @Test
    void item_conjured_decreasesInQuality_twiceTheSpeed_alsoWhenSellInExpired() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.CONJURED, 0, 6)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.CONJURED, -1, 2));
    }

    @Test
    void item_AgedBrie_increasesInQuality() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.AGED_BRIE, 2, 2)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.AGED_BRIE, 1, 3));
    }

    @Test
    void item_AgedBrie_increasesInQuality_DoublesWhenOff() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.AGED_BRIE, 0, 2)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.AGED_BRIE, -1, 4));
    }

    @Test
    void item_AgedBrie_cannotGoOver50Quality() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.AGED_BRIE, 2, 50)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.AGED_BRIE, 1, 50));
    }

    @Test
    void item_Sulfuras_neverChanges() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.SULFURAS, 100, 100)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.SULFURAS, 100, 100));
    }

    @Test
    void item_BackStagePasses_increasesInQuality_byOneOutside10Days() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.BACKSTAGE_PASSES, 20, 2)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.BACKSTAGE_PASSES, 19, 3));
    }

    @Test
    void item_BackStagePasses_increasesInQuality_byTwoInside10Days() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.BACKSTAGE_PASSES, 10, 2)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.BACKSTAGE_PASSES, 9, 4));
    }

    @Test
    void item_BackStagePasses_increasesInQuality_byThreeInside5Days() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.BACKSTAGE_PASSES, 5, 2)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.BACKSTAGE_PASSES, 4, 5));
    }

    @Test
    void itemBackStagePasses_increasesInQuality_goesToZeroWhenSellInExpires() {
        app.setItems(new Item[]{new Item(ItemTypeUtility.BACKSTAGE_PASSES, 0, 20)});
        app.updateQuality();
        assertItemEquals(app.getItems()[0], new Item(ItemTypeUtility.BACKSTAGE_PASSES, -1, 0));
    }

    private static void assertItemEquals(Item actual, Item expected) {
        Assumptions.assumeTrue(actual != null && expected != null);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getQuality(), actual.getQuality());
        assertEquals(expected.getSellIn(), actual.getSellIn());
    }
}