package iot.tetracubered.data.entities;

import io.vertx.mutiny.sqlclient.Row;

public interface BaseEntity {

    void populateFromRow(Row row);
}
