package Tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectorHelper {

    public static Field[] getAllFields(Class<?> type) throws SecurityException {
        List<Field> fields = new ArrayList<>();
        Collections.reverse(getFields(type, fields));
        return fields.toArray(new Field[0]);
    }

    private static List<Field> getFields(Class<?> type, List<Field> fields) {
        fields.addAll(Arrays.stream(type.getDeclaredFields()).collect(Collectors.toList()));
        if (type.getSuperclass() != null) {
            getFields(type.getSuperclass(), fields);
        }
        return fields;
    }
}
