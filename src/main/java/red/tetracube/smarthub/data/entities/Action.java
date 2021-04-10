package red.tetracube.smarthub.data.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "actions")
public class Action {

    @Id
    private UUID id;

    @Column(name = "trigger_topic", nullable = false)
    private String triggerTopic;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "feature_id", nullable = false)
    @ManyToOne(targetEntity = Feature.class, fetch = FetchType.LAZY)
    private Feature feature;
}
