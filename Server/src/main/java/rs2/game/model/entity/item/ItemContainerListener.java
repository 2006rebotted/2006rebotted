package rs2.game.model.entity.item;

/**
 * The listener that provides functionality for the operations of item
 * containers.
 *
 * @author lare96 <http://github.com/lare96>
 */
public interface ItemContainerListener {

    /**
     * The method executed when an attempt is made to add the {@code item} to
     * the {@code container}.
     *
     * @param container
     *            the container this listener is for.
     * @param item
     *            the item that was attempted to be added.
     * @param successful
     *            if the item was successfully added.
     */
    public void onAdd(ItemContainer container, Item item, boolean successful);

    /**
     * The method executed when an attempt is made to remove the {@code item}
     * from the {@code container}.
     *
     * @param container
     *            the container this listener is for.
     * @param item
     *            the item that was attempted to be removed.
     * @param successful
     *            if the item was successfully removed.
     */
    public void onRemove(ItemContainer container, Item item, boolean successful);
}
