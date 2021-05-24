package red.tetracube.smarthub.annotations.processors;

import io.vertx.mutiny.sqlclient.Row;
import red.tetracube.smarthub.annotations.Column;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.NoSuchElementException;

@ApplicationScoped
public class EntityProcessor {

    public <T> T mapTableToEntity(T entity, Row tableRow) throws IllegalAccessException {
        var entityClass = entity.getClass();
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            var columnIdx = tableRow.getColumnIndex(getName(field));
            if (columnIdx == -1) {
                throw new NoSuchElementException("Column " + columnIdx + " does not exist");
            } else {
                if (tableRow.getValue(columnIdx) == null) {
                    continue;
                }
                if (field.getType().isEnum()) {
                    var enumConstant = Arrays.stream(field.getType().getEnumConstants())
                            .filter(value -> value.toString().equals(tableRow.getString(columnIdx)))
                            .findFirst();
                    if (enumConstant.isEmpty()) {
                        throw new NoSuchElementException("Column " + columnIdx + " does not exist");
                    }
                    field.set(entity, enumConstant.get());
                } else {
                    var columnValue = tableRow.get(field.getType(), columnIdx);
                    field.set(entity, columnValue);
                }
            }
        }
        return entity;
    }

    private String getName(Field field) {
        String value = field.getAnnotation(Column.class)
                .name();
        return value.isEmpty() ? field.getName() : value;
    }
}
