package de.daver.unihub.util;

import de.daver.unihub.UniverseException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @since 1.0.0
 */
public class Reflections {

    private Reflections(){}

    /**
     * Sets a value to a Field
     * <p>
     * if the field doesn't exist it throws a {@link UniverseException}
     * @param object an Object
     * @param fieldName the name of the field
     * @param value the new Value for the field
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getField(object, fieldName);
        if (field == null) {
            try {
                throw new UniverseException("Cant set Value of Field: " + fieldName + " in Object: " + object.toString());
            } catch (UniverseException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            field.set(object, value);
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the value of a Field
     * <p>
     * Gets a value from a field in the {@link Object} object
     * @param object an instance of a class
     * @param fieldName an fieldName of the objects class
     * @return the value of the {@link Field}
     */
    public static Object getFieldValue(Object object, String fieldName){
        Field field = getField(object, fieldName);
        if(field == null) return null;
        try {
            return field.get(object);
        }catch (IllegalAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the field of the object
     * @param object can be any {@link Object} but not null
     * @param name is the name of the field
     * @return null if the doesn't exist or the given {@link Object} is null
     */
    public static Field getField(Object object, String name){
        if(object == null) return null;
        try{
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field;
        }catch (NoSuchFieldException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Invokes a method and checks if anything is null
     * @param object the instance of the method not null
     * @param method the invoked method not null
     * @param values values for the parameters of the method
     */
    public static void invoke(Object object, Method method, Object...values) {
        if (object == null) return;
        if (method == null) return;
        if(values.length != method.getParameterCount()) return;
        try {
            method.invoke(object, values);
        }catch (InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads a class for given name
     * catches {@link ClassNotFoundException} and printing the stacktrace
     * @param className the full name of the class
     */
    public static void loadClass(String className) {
        try {
            Class.forName(className);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
