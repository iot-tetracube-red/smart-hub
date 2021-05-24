package red.tetracube.smarthub.data.entities;

import java.util.*;

public class Device {

    private UUID id;
    private String name;
    private boolean isOnline;
    private String feedbackTopic;
    private String colorCode;

    private List<Feature> features;

    public Device(UUID id,
                  String name,
                  boolean isOnline,
                  String feedbackTopic,
                  String colorCode) {
        this.id = id;
        this.name = name;
        this.isOnline = isOnline;
        this.feedbackTopic = feedbackTopic;
        this.colorCode = colorCode;
    }
}