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
    public enum kind {PRIMITIVE, POINTER, ARRAY, STRUCT, FUNCSIG};
    
    class primitive extends complexType {
        public basicType btype;
        
        public primitive(basicType b) {
            btype = b;
            bytes = btype.bytes;
        }
    }
    
    class pointer extends complexType {
        public complexType baseType;
        
        public pointer(complexType c) {
            baseType = c;
            bytes = 8; // TODO: Change based on system size
        }
    }
    
    class array extends complexType {
        public complexType baseType;
        public int size;
        
        public array(complexType c, int s) {
            baseType = c;
            size = s;
            bytes = size*baseType.bytes; // TODO: Maybe keep array size in memory for runtime checks?
        }
    }
    
    class struct extends complexType {
        class field {
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
    
    class funcsig extends complexType {
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
