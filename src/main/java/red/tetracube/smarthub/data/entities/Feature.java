package red.tetracube.smarthub.data.entities;

import red.tetracube.smarthub.enumerations.FeatureType;
import red.tetracube.smarthub.enumerations.RequestSourceType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "feature")
public class Feature {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "feature_type", nullable = false)
    private FeatureType featureType;

    @Column(name = "is_running", nullable = false)
    private Boolean isRunning;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "source_type")
    private RequestSourceType sourceType;

    @Column(name = "running_reference_id")
    private String runningReferenceId;

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class)
    private Device device;

//    @OneToMany(targetEntity = Action.class, fetch = FetchType.LAZY, mappedBy = "feature")
  //  private List<Action> actions = new ArrayList<>();
}
