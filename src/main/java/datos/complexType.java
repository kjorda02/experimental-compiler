package datos;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kjorda
 */
public abstract class complexType {
    public String name; // Null if anonymous type
    public int bytes; // Size in memory in bytes
    
    @Override
    public String toString() {
        return "o_O";
    }
    
    public class primitive extends complexType {
        public basicType btype;
        
        public primitive(basicType b) {
            btype = b;
            bytes = btype.bytes;
        }
    }
    
    public class pointer extends complexType {
        public complexType baseType;
        
        public pointer(complexType c) {
            baseType = c;
            bytes = 8; // TODO: Change based on system size
        }
    }
    
    public class array extends complexType {
        public complexType baseType;
        public int size;
        
        public array(complexType c, int s) {
            baseType = c;
            size = s;
            bytes = size*baseType.bytes; // TODO: Maybe keep array size in memory for runtime checks?
        }
    }
    
    public class struct extends complexType {
        class field { // (dc) field descriptor
            public int offset;
            public complexType type;
            
            public field(int o, complexType c){
                offset = 0;
                type = c;
            }
        }
        HashMap<String, field> fields;
        int size; // Number of fields
        
        public struct() {
            fields = new HashMap<>();
            size = 0;
        }
        
        public void addField(String name, complexType c) {
            fields.put(name, new field(bytes, c));
            bytes += c.bytes;
            size++;
        }
    }
    
    public class funcsig extends complexType {
        public complexType returnType;
        public ArrayList<complexType> paramTypes;
        
        public funcsig(complexType c) {
            returnType = c;
            paramTypes = new ArrayList<>();
        }
        
        public void addParam(complexType c) {
            paramTypes.add(c);
        }
    }
}
