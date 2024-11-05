package datos;

/**
 *
 * @author kjorda
 */
public class typeDesc {
    //String name; // Identifier/name of the type (e.g. "int", "bool")
    public basicType basicType;
    public int bytes;

    public typeDesc(basicType bt, int b) {
        basicType = bt;
        bytes = b;
    }
}