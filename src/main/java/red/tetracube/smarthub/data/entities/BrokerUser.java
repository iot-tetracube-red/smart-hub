package red.tetracube.smarthub.data.entities;

import io.vertx.core.json.JsonArray;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import red.tetracube.smarthub.data.columnsDefinitions.JsonArrayType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "vmq_auth_acl")
@TypeDefs({
        @TypeDef(name = JsonArrayType.NAME, typeClass = JsonArrayType.class)
})
public class BrokerUser {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "mountpoint", nullable = false)
    private String mountpoint;

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Type(type = JsonArrayType.NAME)
    @Column(name = "publish_acl", nullable = false)
    private JsonArray publishAcl;

    @Type(type = JsonArrayType.NAME)
    @Column(name = "subscribe_acl", nullable = false)
    private JsonArray subscribeAcl;

    public BrokerUser() {
    }

    public BrokerUser(UUID id,
                      String mountpoint,
                      String clientId,
                      String username,
                      String password,
                      JsonArray publishAcl,
                      JsonArray subscribeAcl) {
        this.id = id;
        this.mountpoint = mountpoint;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.publishAcl = publishAcl;
        this.subscribeAcl = subscribeAcl;
    }
}
