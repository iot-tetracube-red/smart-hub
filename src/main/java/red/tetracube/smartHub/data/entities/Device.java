package red.tetracube.smartHub.data.entities;

import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.UUID;

@MongoEntity(collection = "Devices")
public class Device {

    @BsonId
    private ObjectId id;

    @BsonProperty("circuit_id")
    private UUID circuitId;

    @BsonProperty("name")
    private String name;

    @BsonProperty("is_online")
    private Boolean isOnline;

    @BsonProperty("feedback_topic")
    private String feedbackTopic;

    @BsonProperty("color_code")
    private String colorCode;

    @BsonProperty("created_at")
    private Calendar createdAt;

    @BsonProperty("updated_at")
    private Calendar updatedAt;
}
