package iot.tetracubered.data.repositories;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import iot.tetracubered.data.entities.BaseEntity;

import javax.inject.Inject;

public abstract class BaseRepository {

    @Inject
    PgPool pgPool;

    protected <T extends BaseEntity> Uni<T> mapRowToObject(Uni<RowSet<Row>> rowSetUni, T classToMap) {
        return rowSetUni.map(RowSet::iterator)
                .map(rowRowIterator -> rowRowIterator.hasNext() ? rowRowIterator.next() : null)
                .map(row -> {
                    if (row == null) {
                        return null;
                    }
                    classToMap.populateFromRow(row);
                    return classToMap;
                });
    }

    protected <T extends BaseEntity> Multi<T> mapRowsToObjects(Uni<RowSet<Row>> rowSetUni, T classToMap) {
        return rowSetUni.onItem()
                .transformToMulti(rows -> Multi.createFrom().iterable(rows))
                .map(row -> {
                    classToMap.populateFromRow(row);
                    return classToMap;
                });
    }
}
