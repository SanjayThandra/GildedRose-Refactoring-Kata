package com.gildedrose;

import com.gildedrose.domain.Item;

import static com.gildedrose.constants.ItemTypeUtility.AGED_BRIE;
import static com.gildedrose.constants.ItemTypeUtility.CONJURED;
import static com.gildedrose.constants.ItemTypeUtility.BACKSTAGE_PASSES;
import static com.gildedrose.constants.ItemTypeUtility.SULFURAS;


public class GildedRose {

    Item[] items;

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public GildedRose(Item[] items) {
        this.items = items;
    }


    public void updateQuality() {
        for (Item item : items) {
            updateItemQuality(item);
        }
    }


    private void updateItemQuality(Item item) {
        boolean isExpired = item.getSellIn() < 1;
        int qualityDegrade = getQualityDegrade(item, isExpired);
        boolean isDegrading = !item.getName().equals(AGED_BRIE)
                && !item.getName().equals(BACKSTAGE_PASSES)
                && !item.getName().equals(SULFURAS);

        if (isDegrading) {
            adjustQuality(item, qualityDegrade);
        }
        if (item.getName().equals(AGED_BRIE)) {
           // Once the sell by date has passed, Quality degrades twice as fast
            //"Aged Brie" actually increases in Quality the older it gets
            int value = isExpired ? 2 : 1;
            adjustQuality(item, value);
        }
        if (item.getName().equals(BACKSTAGE_PASSES)) {
            updateBackStageQuality(item, isExpired);
        }

        if (!item.getName().equals(SULFURAS)) {
            //At the end of each day our system lowers both values for every item
            item.setSellIn(item.getSellIn() - 1);
        }
    }

    /***
     * - "Backstage passes", like aged brie, increases in Quality as its SellIn value approaches;
     * @param item
     * @param isExpired
     */
    private void updateBackStageQuality(Item item, boolean isExpired) {
        adjustQuality(item, 1);
        if (item.getSellIn() < 11) {
            adjustQuality(item, 1);
        }
        if (item.getSellIn() < 6) {
            adjustQuality(item, 1);
        }
        if (isExpired) {
            item.setQuality(0);
        }
    }

    private int getQualityDegrade(Item item, boolean isExpired) {
        int qualityDegrade = item.getName().equals(CONJURED) ? -2 : -1;
        return isExpired ? qualityDegrade * 2 : qualityDegrade;
    }

    private void adjustQuality(Item item, int value) {
        int quality = item.getQuality() + value;
        if (quality <= 50 && quality >= 0) {
            item.setQuality(quality);
        }
    }
}