package red.tetracube.smarthub.data.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @Column(name = "feedback_topic", nullable = false, unique = true)
    private String feedbackTopic;

    @Column(name = "color_code", nullable = false)
    private String colorCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device", targetEntity = Feature.class)
    private List<Feature> features = new ArrayList<>();
}
