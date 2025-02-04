package arbol.type;

import arbol.fun.arglist_node;
import arbol.node;
import arbol.terminal_node;
import arbol.val.expr_node;
import datos.basicType;
import experimental_compiler.Main;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author kjorda
 */
public abstract class complexType extends node {
    public String name; // Null if anonymous type
    public int bytes; // Size in memory in bytes
    
    @Override
    public String toString() {
        return "o_O";
    }
    
    @Override
    public void gest() {
        
    }   
    
    public static class primitive extends complexType {
        public basicType btype;
        
//        public primitive(terminal_node<String> n, terminal_node<basicType> b) {
//            this(n.value, b.value);
//            left = b.left;
//            right = b.right;
//        }
        
        public primitive(String n, basicType b) {
            name = n;
            btype = b;
            bytes = btype.bytes;
            error = false;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof primitive))
                return false;
            
            primitive other = (primitive) o;
            if (name != null && other.name != null && !name.equals(other.name))
                return false;
            return btype == other.btype;
        }
        
        @Override
        public String toString() {
            return btype.toString();
        }
    }
    
    public static class pointer extends complexType {
        public complexType baseType;
        
        public pointer(String n, complexType c) {
            name = n;
            baseType = c;
            bytes = 8; // TODO: Change based on system size
            error = false;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof pointer))
                return false;
            
            pointer other = (pointer) o;
            if (name != null && other.name != null && !name.equals(other.name))
                return false;
            return baseType.equals(other.baseType);
        }
        
        @Override
        public String toString() {
            return "*"+baseType.toString();
        }
    }
    
    public static class array extends complexType {
        public complexType baseType;
        public expr_node size;
        
        public array(String n, complexType c, expr_node s) {
            left = s.left;
            right = s.right;
            
            if (s.isEmpty())
                return;
            
            if (!(s.type instanceof complexType.primitive) || ((complexType.primitive) s.type).btype != basicType.INT) {
                Main.report_error("Cannot declare array with non-integer size ("+s.type.toString()+")", this);
                return;
            }
            name = n;
            baseType = c;
            size = s;
            if (size.value != null)
                bytes = (int) (size.value*baseType.bytes); // TODO: Maybe keep array size in memory for runtime checks?
            
            error = false;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof array))
                return false;
            
            array other = (array) o;
            if (name != null && other.name != null && !name.equals(other.name))
                return false;
            
            return size == other.size && baseType.equals(other.baseType);
        }
        
        @Override
        public void gest() {
            size.gest();
        }
        
        @Override
        public String toString() {
            String s = "[";
            if (size.value == null)
                s += "?"; // Variable length array, determined at runtime
            else
                s += String.valueOf(size.value);
            return s + "]" + baseType.toString();
        }
    }
    
    public static class struct extends complexType {
        HashMap<String, field> fields;
        int size; // Number of fields
        
        public struct(String n) {
            name = n;
            fields = new HashMap<>();
            size = 0;
            error = false;
        }
        
//        public struct(String structName, terminal_node<String> fieldName, complexType fieldType) {
//            this(structName, fieldName.value, fieldType);
//            left = fieldType.left;
//            right = fieldName.right;
//        }
        
        public struct(String structName, String fieldName, complexType fieldType) {
            name = structName;
            fields = new HashMap<>();
            size = 1;
            fields.put(fieldName, new field(bytes, fieldType));
            bytes += fieldType.bytes;
        }
        
//        public complexType.struct addField(terminal_node<String> name, complexType c) {
//            fields.put(name.value, new field(bytes, c));
        
        public complexType.struct addField(String name, complexType c) {
            fields.put(name, new field(bytes, c));
            bytes += c.bytes;
            size++;
            //left = c.left; // In production (fields -> type ID SEMI fields), we expand the left to the left of type
            return this;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof struct))
                return false;
            
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
        
        @Override
        public String toString() {
            List<Entry<String, field>> sortedFields = new ArrayList<>(fields.entrySet());
            Collections.sort(sortedFields, (a, b) -> Integer.compare(a.getValue().offset, b.getValue().offset));
            
            StringBuilder s = new StringBuilder("struct{");

            for (Entry<String, field> f : sortedFields) {
                s.append(f.getValue().type.toString());
                s.append(" ");
                s.append(f.getKey());
                s.append(";");
            }
            s.append("}");
            return s.toString();
        }
    }
    
    public static class funcsig extends complexType { // function signature
        public complexType returnType;
        public ArrayList<complexType> paramTypes;
        public terminal_node<String> name;
        
        public funcsig(complexType c, terminal_node<String> n, arglist_node l) {
            name = n;
            returnType = c;
            paramTypes = new ArrayList<>();
            for ( ; l.list != null; l = l.list) {
                paramTypes.add(l.type);
            }
            error = false;
        }
        
        public void addParam(complexType c) {
            paramTypes.add(c);
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof funcsig))
                return false;
            
            funcsig other = (funcsig) o;
            return returnType.equals(other.returnType) && paramTypes.equals(other.paramTypes);
        }
    }
}
