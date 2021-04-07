package rs2.net.codec;

/**
 * Represent modifications that can be performed on bytes.
 * 
 * @author SeVen
 */
public enum ByteModification {
    
    /**
     * No modifications
     */
    STANDARD,
    
    /**
     * Adds 128 to the value.
     */
    ADDITION,
    
    /**
     * Places a negative sign on the value.
     */
    NEGATION,
    
    /**
     * Subtracts the value from 128.
     */
    SUBTRACTION
    
}
