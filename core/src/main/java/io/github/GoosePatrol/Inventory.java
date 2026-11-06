package io.github.GoosePatrol;
/** 10-slot inventory for holding inactive items */
public class Inventory {
    public static int SLOT_COUNT = 10;
    private final Item[] slots = new Item[SLOT_COUNT];
    /**
     * adds item to first avlible slot in the inventory
     * @param item
     */
    public void add(Item item){
        for (int i = 0; i< SLOT_COUNT; i++){
            if (slots[i] == null){
                slots[i] = item;
            }
        }
    }
    /**
     * removes item at selected slot in inventory
     * @param index
     * @return the removed item
     */
    public Item remove(int index){
        Item item = slots[index];
        slots[index] = null;
        return item;
    }
    public Item get(int index){
        return slots[index];
    }
}
