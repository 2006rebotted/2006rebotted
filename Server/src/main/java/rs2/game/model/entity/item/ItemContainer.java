package rs2.game.model.entity.item;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.out.UpdateItemsOnWidgetPacket;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.base.Preconditions;

/**
 * The container that represents a collection of items.
 *
 * @author lare96 <http://github.com/lare96>
 */
public class ItemContainer implements Iterable<Item> {

    /**
     * The capacity of this item container.
     */
    private final int capacity;

    /**
     * The policy of this item container.
     */
    private final ItemContainerPolicy policy;

    /**
     * The listener functionality for this container.
     */
    private final List<ItemContainerListener> listeners = new LinkedList<>();

    /**
     * The items contained by this container.
     */
    private Item[] items;

    /**
     * Creates a new {@link ItemContainer}.
     *
     * @param capacity
     *            the capacity of this item container.
     * @param policy
     *            the policy of this item container.
     */
    public ItemContainer(int capacity, ItemContainerPolicy policy) {
        this.capacity = capacity;
        this.policy = policy;
        this.items = new Item[capacity];
    }

    @Override
    public void forEach(Consumer<? super Item> action) {
        Objects.requireNonNull(action);
        for (Item item : items) {
            if (item == null)
                continue;
            action.accept(item);
        }
    }

    @Override
    public Spliterator<Item> spliterator() {
        return Spliterators.spliterator(items, 0, items.length, Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public Iterator<Item> iterator() {
        return new ItemContainerIterator(this);
    }

    /**
     * Attempts to add {@code item} to {@code slot} in this container.
     *
     * @param item
     *            the item to add to this container.
     * @param slot
     *            the slot to add this item in ({@code -1} for any slot).
     * @return {@code true} if the item was added, {@code false} otherwise.
     */
    public boolean add(Item item, int slot) {
        if (!Item.valid(item) || item.getAmount() > Integer.MAX_VALUE) {
            listeners.forEach(l -> l.onAdd(ItemContainer.this, item, false));
            return false;
        }
        int newSlot = (slot > -1) ? slot : freeSlot();
        if ((item.definition().isStackable() || policy.equals(ItemContainerPolicy.STACK_ALWAYS)) && !policy
            .equals(ItemContainerPolicy.STACK_NEVER)) {
            if (amount(item.getId()) > 0) {
                newSlot = searchSlot(item.getId());
            }
        }
        if (newSlot == -1) {
            listeners.forEach(l -> l.onAdd(ItemContainer.this, item, false));
            return false;
        }
        if (get(newSlot) != null) {
            newSlot = freeSlot();
        }
        if (item.definition().isStackable() || policy == ItemContainerPolicy.STACK_ALWAYS && policy != ItemContainerPolicy.STACK_NEVER) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null && items[i].getId() == item.getId()) {
                    set(i, new Item(items[i].getId(), items[i].getAmount() + item.getAmount()));
                    listeners.forEach(l -> l.onAdd(ItemContainer.this, item, true));
                    return true;
                }
            }
            if (newSlot == -1) {
                listeners.forEach(l -> l.onAdd(ItemContainer.this, item, false));
                return false;
            }
            set(slot > -1 ? newSlot : freeSlot(), item);
            listeners.forEach(l -> l.onAdd(ItemContainer.this, item, true));
            return true;
        }
        int remainingSlots = remaining();
        if (item.getAmount() > remainingSlots && !item.definition().isStackable()) {
            item.setAmount(remainingSlots);
        }
        for (int i = 0; i < item.getAmount(); i++) {
            set(slot > -1 ? newSlot : freeSlot(), new Item(item.getId(), 1));
        }
        listeners.forEach(l -> l.onAdd(ItemContainer.this, item, true));
        return true;
    }

    /**
     * Attempts to add {@code item} to any slot in this container.
     *
     * @param item
     *            the item to add to this container.
     * @return {@code true} if the item was added, {@code false} otherwise.
     */
    public boolean add(Item item) {
        return add(item.copy(), -1);
    }

    /**
     * Attempts to add all items from {@code c} to any slots in this container.
     *
     * @param c
     *            the items to add to this container.
     * @return {@code true} if this container changed as a result of the
     *         operation, {@code false} otherwise.
     */
    public boolean addAll(Collection<? extends Item> c) {
        boolean val = false;
        for (Item item : c) {
            if (item == null)
                continue;
            if (add(item))
                val = true;
        }
        return val;
    }

    /**
     * Attempts to add all items from {@code items} to any slots in this
     * container.
     *
     * @param items
     *            the items to add to this container.
     * @return {@code true} if this container changed as a result of the
     *         operation, {@code false} otherwise.
     */
    public boolean addAll(Item... items) {
        return addAll(Arrays.asList(items));
    }

    /**
     * Attempts to add all items from {@code container} to any slots in this
     * container.
     *
     * @param container
     *            the container of items to add to this container.
     * @return {@code true} if this container changed as a result of the
     *         operation, {@code false} otherwise.
     */
    public boolean addAll(ItemContainer container) {
        return addAll(Arrays.asList(container.container()));
    }

    /**
     * Attempts to remove {@code item} from {@code slot} in this container.
     *
     * @param item
     *            the item to remove from this container.
     * @param slot
     *            the slot to remove this item from ({@code -1} for any slot).
     * @return {@code true} if the item was removed, {@code false} otherwise.
     */
    public boolean remove(Item item, int slot) {
        if (!Item.valid(item) || item.getAmount() > Integer.MAX_VALUE) {
            listeners.forEach(l -> l.onRemove(ItemContainer.this, item, false));
            return false;
        }
        if ((item.definition().isStackable() || policy.equals(ItemContainerPolicy.STACK_ALWAYS)) && !policy
            .equals(ItemContainerPolicy.STACK_NEVER)) {
            int slotHolder = searchSlot(item.getId());
            Item stack = get(slotHolder);
            if (stack == null) {
                listeners.forEach(l -> l.onRemove(ItemContainer.this, item, false));
                return false;
            }
            if (stack.getAmount() > item.getAmount()) {
                set(slotHolder, new Item(stack.getId(), stack.getAmount() - item.getAmount()));
            } else {
                set(slotHolder, null);
            }
        } else {
            for (int i = 0; i < item.getAmount(); i++) {
                int slotHolder = searchSlot(item.getId());
                if (i == 0 && slot != -1) {
                    Item inSlot = get(slot);
                    if (inSlot == null) {
                        listeners.forEach(l -> l.onRemove(ItemContainer.this, item, false));
                        return false;
                    }
                    if (inSlot.getId() == item.getId()) {
                        slotHolder = slot;
                    }
                }
                if (slotHolder != -1) {
                    set(slotHolder, null);
                } else {
                    listeners.forEach(l -> l.onRemove(ItemContainer.this, item, false));
                    return false;
                }
            }
        }
        listeners.forEach(l -> l.onRemove(ItemContainer.this, item, true));
        return true;
    }

    /**
     * Attempts to remove {@code item} from any slot in this container.
     *
     * @param item
     *            the item to remove from this container.
     * @return {@code true} if the item was removed, {@code false} otherwise.
     */
    public boolean remove(Item item) {
        return remove(item, -1);
    }

    /**
     * Attempts to remove all items from {@code c} from any slots in this
     * container.
     *
     * @param c
     *            the items to remove from this container.
     * @return {@code true} if this container changed as a result of the
     *         operation, {@code false} otherwise.
     */
    public boolean removeAll(Collection<? extends Item> c) {
        boolean val = false;
        for (Item item : c) {
            if (item == null)
                continue;
            if (remove(item))
                val = true;
        }
        return val;
    }

    /**
     * Attempts to remove all items from {@code items} from any slots in this
     * container.
     *
     * @param items
     *            the items to remove from this container.
     * @return {@code true} if this container changed as a result of the
     *         operation, {@code false} otherwise.
     */
    public boolean removeAll(Item... items) {
        return removeAll(Arrays.asList(items));
    }

    /**
     * Attempts to replace the first occurrence of {@code oldId} with
     * {@code newId}. If {@code oldId} isn't in this container {@code newId}
     * will not be added.
     * 
     * @param oldId
     *            the identifier to replace.
     * @param newId
     *            the identifier to replace with.
     * @return {@code true} if the operation was successful, {@code false}
     *         otherwise.
     */
    public boolean replace(int oldId, int newId) {
        int idx = searchSlot(oldId);
        if (idx == -1)
            return false;
        Item onSlot = get(idx);
        if (remove(onSlot, idx)) {
            boolean successful = add(new Item(newId, onSlot.getAmount()), idx);
            return successful;
        }
        return false;
    }

    /**
     * Attempts to replace all occurrences of {@code oldId} with {@code newId}.
     * If {@code oldId} isn't in this container {@code newId} will not be added.
     * 
     * @param oldId
     *            the identifier to replace.
     * @param newId
     *            the identifier to replace with.
     * @return {@code true} if at least one replace operation was successful,
     *         {@code false} otherwise.
     */
    public boolean replaceAll(int oldId, int newId) {
        boolean successful = false;
        while(replace(oldId, newId))
            successful = true;
        return successful;
    }

    /**
     * Determines if there is enough space in this container for {@code item}.
     *
     * @param item
     *            the item to determine if enough space for.
     * @return {@code true} if there is enough space, {@code false} otherwise.
     */
    public boolean spaceFor(Item item) {
        if (item.definition().isStackable() || policy == ItemContainerPolicy.STACK_ALWAYS && policy != ItemContainerPolicy.STACK_NEVER) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] != null && items[i].getId() == item.getId()) {
                    int totalCount = item.getAmount() + items[i].getAmount();
                    if (totalCount >= Integer.MAX_VALUE || totalCount < 1) {
                        return false;
                    }
                    return true;
                }
            }
            int slot = freeSlot();
            return slot != -1;
        }
        int slots = remaining();
        return slots >= item.getAmount();
    }

    /**
     * Determines if this container contains any items with {@code id}.
     *
     * @param id
     *            the identifier to check this container for.
     * @return {@code true} if this container contains the identifier,
     *         {@code false} otherwise.
     */
    public boolean contains(int id) {
        return searchSlot(id) != -1;
    }

    /**
     * Determines if this container contains any items with all
     * {@code identifiers}.
     *
     * @param identifiers
     *            the identifiers to check this container for.
     * @return {@code true} if this container contains all of the identifiers,
     *         {@code false} otherwise.
     */
    public boolean containsAll(int... identifiers) {
        return Arrays.stream(identifiers).allMatch(this::contains);
    }

    /**
     * Determines if this container contains any items with any
     * {@code identifiers}.
     *
     * @param identifiers
     *            the identifiers to check this container for.
     * @return {@code true} if this container contains any of the identifiers,
     *         {@code false} otherwise.
     */
    public boolean containsAny(int... identifiers) {
        return Arrays.stream(identifiers).anyMatch(this::contains);
    }

    /**
     * Determines if this container contains {@code item}.
     *
     * @param item
     *            the item to check this container for.
     * @return {@code true} if this container contains the item, {@code false}
     *         otherwise.
     */
    public boolean contains(Item item) {
        return stream().filter(Objects::nonNull).anyMatch(i -> i.getId() == item.getId() && i.getAmount() >= item.getAmount());
    }

    /**
     * Determines if this container contains all of {@code items}.
     *
     * @param items
     *            the items to check this container for.
     * @return {@code true} if this container contains all of the items,
     *         {@code false} otherwise.
     */
    public boolean containsAll(Item... items) {
        return Arrays.stream(items).filter(Objects::nonNull).allMatch(this::contains);
    }

    /**
     * Determines if this container contains any of {@code items}.
     *
     * @param items
     *            the items to check this container for.
     * @return {@code true} if this container contains any of the items,
     *         {@code false} otherwise.
     */
    public boolean containsAny(Item... items) {
        return Arrays.stream(items).filter(Objects::nonNull).anyMatch(this::contains);
    }

    /**
     * Transfers the item in {@code slot} to {@code newSlot}. If an item already
     * exists in the new slot, the items in this container will be shifted to
     * accommodate for the transfer.
     *
     * @param slot
     *            the slot of the item to transfer.
     * @param newSlot
     *            the slot to transfer the item to.
     * @return {@code true} if the transfer was successful, {@code false}
     *         otherwise.
     */
    public boolean transfer(int slot, int newSlot) {
        Item from = items[slot];
        if (from == null) {
            return false;
        }
        items[slot] = null;
        if (slot > newSlot) {
            int shiftFrom = newSlot;
            int shiftTo = slot;
            for (int i = (newSlot + 1); i < slot; i++) {
                if (items[i] == null) {
                    shiftTo = i;
                    break;
                }
            }
            Item[] slice = new Item[shiftTo - shiftFrom];
            System.arraycopy(items, shiftFrom, slice, 0, slice.length);
            System.arraycopy(slice, 0, items, shiftFrom + 1, slice.length);
        } else {
            int sliceStart = slot + 1;
            int sliceEnd = newSlot;
            for (int i = (sliceEnd - 1); i >= sliceStart; i--) {
                if (items[i] == null) {
                    sliceStart = i;
                    break;
                }
            }
            Item[] slice = new Item[sliceEnd - sliceStart + 1];
            System.arraycopy(items, sliceStart, slice, 0, slice.length);
            System.arraycopy(slice, 0, items, sliceStart - 1, slice.length);
        }
        items[newSlot] = from;
        return true;
    }

    /**
     * Shifts all items in this container to the left to fill any {@code null}
     * slots.
     */
    public void shift() {
        Item[] previousItems = items;
        items = new Item[capacity];
        int newIndex = 0;
        for (int i = 0; i < items.length; i++) {
            if (previousItems[i] != null) {
                items[newIndex] = previousItems[i];
                newIndex++;
            }
        }
    }

    /**
     * Refreshes the contents of this container to {@code widget}.
     *
     * @param player
     *            the player to refresh this container for.
     * @param widget
     *            the interface to refresh the contents of this container on.
     */
    public void refresh(Player player, int widget) {
        player.queuePacket(new UpdateItemsOnWidgetPacket(widget, container()));
    }

    /**
     * Swaps the positions of two items in this container.
     *
     * @param slot
     *            the slot of the first item to swap.
     * @param otherSlot
     *            the slot of the second item to swap.
     */
    public void swap(int slot, int otherSlot) {
        Item temp = get(slot);
        set(slot, get(otherSlot));
        set(otherSlot, temp);
    }

    /**
     * Sets the container of items to {@code items}. The container will not hold
     * any references to the array, nor the item instances in the array.
     *
     * @param items
     *            the new array of items, the capacities of this must be equal
     *            to or lesser than the container.
     */
    public final void setItems(Item[] items) {
        Preconditions.checkArgument(items.length <= capacity);
        clear();
        for (int i = 0; i < items.length; i++) {
            this.items[i] = items[i] == null ? null : items[i].copy();
        }
    }

    /**
     * Determines if {@code slot} does not have an item on it.
     *
     * @param slot
     *            the to determine if free or not.
     * @return {@code true} if the slot is free, {@code false} otherwise.
     */
    public boolean free(int slot) {
        return items[slot] == null;
    }

    /**
     * Determines if {@code slot} does have an item on it.
     *
     * @param slot
     *            the to determine if used or not.
     * @return {@code true} if the slot is used, {@code false} otherwise.
     */
    public boolean used(int slot) {
        return !free(slot);
    }

    /**
     * Places {@code item} on {@code slot} regardless of if there is an existing
     * item on the slot or not.
     *
     * @param slot
     *            the slot to place the item on.
     * @param item
     *            the item to place on the slot.
     */
    public void set(int slot, Item item) {
        items[slot] = item;
    }

    /**
     * Searches and returns the first item found with {@code id}.
     *
     * @param id
     *            the identifier to search this container for.
     * @return the item wrapped within an optional, or an empty optional if no
     *         item was found.
     */
    public Optional<Item> searchItem(int id) {
        return stream().filter(i -> i != null && id == i.getId()).findFirst();
    }

    /**
     * Retrieves the slot of the first item found with {@code id}.
     *
     * @param id
     *            the identifier to search this container for.
     * @return the slot of the item with the identifier.
     */
    public int searchSlot(int id) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null || items[i].getId() != id)
                continue;
            return i;
        }
        return -1;
    }

    /**
     * Retrieves the item located on {@code slot}.
     *
     * @param slot
     *            the slot to get the item on.
     * @return the item on the slot, or {@code null} if no item exists on the
     *         slot.
     */
    public Item get(int slot) {
        if (slot == -1 || slot >= items.length)
            return null;
        return items[slot];
    }

    /**
     * Retrieves the identifier for the item located on {@code slot}.
     *
     * @param slot
     *            the slot to get the item identifier on.
     * @return the item identifier on the slot, or {@code -1} if no item exists
     *         on the slot.
     */
    public int getId(int slot) {
        if (items[slot] == null)
            return -1;
        return items[slot].getId();
    }

    /**
     * Gets the total quantity of all items with {@code id}.
     *
     * @param id
     *            the item identifier to retrieve the total quantity of.
     * @return the total quantity of items in this container with the
     *         identifier.
     */
    public int amount(int id) {
        return stream().filter(i -> Item.valid(i) && i.getId() == id).mapToInt(i -> i.getAmount()).sum();
    }

    /**
     * Retrieves a free slot from this container.
     *
     * @return the free slot, or {@code -1} if no slot was found.
     */
    public int freeSlot() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Retrieves the remaining free slots in this container.
     *
     * @return the remaining free slots.
     */
    public int remaining() {
        return capacity - size();
    }

    /**
     * Retrieves the maximum amount of items that can be in this container.
     *
     * @return the maximum amount of items.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Clears all of the items in this container.
     */
    public void clear() {
        items = new Item[capacity];
    }

    /**
     * The amount of elements without a value of {@code null} in this container.
     *
     * @return the size of this container.
     */
    public int size() {
        return stream().filter(Objects::nonNull).mapToInt(item -> 1).sum();
    }

    /**
     * Returns a stream associated with the elements in this container.
     *
     * @return the stream for this container.
     */
    public Stream<Item> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Adds a new listener to this item container.
     *
     * @param listener
     *            the listener to add to this container.
     * @return {@code true} if the listener was successfully added,
     *         {@code false} otherwise.
     */
    public final boolean addListener(ItemContainerListener listener) {
        return listeners.add(listener);
    }

    /**
     * Removes an existing listener from this item container.
     *
     * @param listener
     *            the listener to remove from this container.
     * @return {@code true} if the listener was successfully removed,
     *         {@code false} otherwise.
     */
    public final boolean removeListener(ItemContainerListener listener) {
        return listeners.remove(listener);
    }

    /**
     * Gets the list of listeners in this container. Changes made to the
     * returned list will have an effect on this container.
     *
     * @return the list of listeners.
     */
    public final List<ItemContainerListener> getListeners() {
        return listeners;
    }

    /**
     * Retrieves the items contained within this container. Modifications made
     * to the returned array will be reflected on the backing array. Use
     * {@link ItemContainer#containerCopy()} for a "safe" copy of the backing
     * array.
     *
     * @return the items contained within this container.
     */
    public final Item[] container() {
        return items;
    }

    /**
     * Retrieves a "safe" version of the items contained within this container.
     * Modifications made to the returned array will not be reflected on the
     * backing array. Use {@link ItemContainer#container()} for a copy of the
     * backing array that holds references.
     *
     * @return a safe copy the items contained within this container.
     */
    public final Item[] containerCopy() {
        Item[] container = new Item[capacity];
        for (int i = 0; i < capacity; i++)
            container[i] = items[i] == null ? null : items[i].copy();
        return container;
    }

    /**
     * The iterator that will iterate over elements in an item container.
     *
     * @author lare96 <http://github.com/lare96>
     */
    private static final class ItemContainerIterator implements Iterator<Item> {

        /**
         * The container that is storing the elements.
         */
        private final ItemContainer container;

        /**
         * The current index that the iterator is iterating over.
         */
        private int index;

        /**
         * The last index that the iterator iterated over.
         */
        private int lastIndex = -1;

        /**
         * Creates a new {@link ItemContainer}.
         *
         * @param container
         *            the container that is storing the elements.
         */
        public ItemContainerIterator(ItemContainer container) {
            this.container = container;
        }

        @Override
        public boolean hasNext() {
            return !(index + 1 > container.capacity());
        }

        @Override
        public Item next() {
            if (index >= container.capacity())
                throw new ArrayIndexOutOfBoundsException("There are no " + "elements left to iterate over!");
            lastIndex = index;
            index++;
            return container.get(lastIndex);
        }

        @Override
        public void remove() {
            if (lastIndex == -1)
                throw new IllegalStateException("This method can only be " + "called once after \"next\".");
            Item item = container.get(lastIndex);
            container.remove(item, lastIndex);
            index = lastIndex;
            lastIndex = -1;
        }
    }
}