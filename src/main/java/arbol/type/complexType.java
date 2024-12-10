package arbol.type;

import datos.basicType;
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
    
    public static class primitive extends complexType {
        public basicType btype;
        
        public primitive(String n, basicType b) {
            name = n;
            btype = b;
            bytes = btype.bytes;
        }
        
        @Override
        public boolean equals(Object o) {
            primitive other = (primitive) o;
            if (name != null && other.name != null && !name.equals(other.name))
                return false;
            return btype == other.btype;
        }
    }
    
    public static class pointer extends complexType {
        public complexType baseType;
        
        public pointer(String n, complexType c) {
            name = n;
            baseType = c;
            bytes = 8; // TODO: Change based on system size
        }
        
        @Override
        public boolean equals(Object o) {
            pointer other = (pointer) o;
            if (name != null && other.name != null && !name.equals(other.name))
                return false;
            return baseType.equals(other.baseType);
        }
    }
    
    public class array extends complexType {
        public complexType baseType;
        public int size;
        
        public array(String n, complexType c, int s) {
            name = n;
            baseType = c;
            size = s;
            bytes = size*baseType.bytes; // TODO: Maybe keep array size in memory for runtime checks?
        }
        
        @Override
        public boolean equals(Object o) {
            array other = (array) o;
            if (name != null && other.name != null && !name.equals(other.name))
                return false;
            
            return size == other.size && baseType.equals(other.baseType);
        }
    }
    
    public class struct extends complexType {
        HashMap<String, field> fields;
        int size; // Number of fields
        
        public struct(String n) {
            name = n;
            fields = new HashMap<>();
            size = 0;
        }
        
        public void addField(String name, complexType c) {
            fields.put(name, new field(bytes, c));
            bytes += c.bytes;
            size++;
        }
        
        @Override
        public boolean equals(Object o) {
            struct other = (struct) o;
            if (name != null && other.name != null && !name.equals(other.name))
                return false;
            
            return fields.equals(other.fields);
        }
        
        class field { // (dc) field descriptor
            public int offset;
            public complexType type;
            
            public field(int o, complexType c){
                offset = 0;
                type = c;
            }
            
            @Override
            public boolean equals(Object o) {
                field other = (field) o;
                return offset == other.offset && type.equals(other.type);
            }
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
        
        @Override
        public boolean equals(Object o) {
            funcsig other = (funcsig) o;
            return returnType.equals(other.returnType) && paramTypes.equals(other.paramTypes);
        }
    }
}
