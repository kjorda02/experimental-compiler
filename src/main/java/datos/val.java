package datos;

/**
 *
 * @author kjorda
 */
public class val {
    public Object val;
    public Type type;

    public val(Object v, Type t) {
        val = v;
        type = t;
    }
    
    @Override
    public String toString() {
        switch (type) {
            case INT:
                return ""+((int) val);
            case BOOL:
                return ""+((boolean) val);
        }
        return null;
    }
}