package iot.tetracubered.mqttClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.mutiny.core.eventbus.EventBus;
import iot.tetracubered.configurations.SmartHubConfiguration;
import iot.tetracubered.mqttClient.payloads.DeviceProvisioningPayload;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MqttClientCallbacks implements MqttCallback {

    @Inject
    SmartHubConfiguration smartHubConfiguration;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    EventBus eventBus;

    private final static Logger LOGGER = LoggerFactory.getLogger(MqttClientCallbacks.class);

    @Override
    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {

    }

    @Override
    public void mqttErrorOccurred(MqttException e) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        LOGGER.info("arrived message in topic: " + topic + " " + new String(mqttMessage.getPayload()));

        if (topic.equals(this.smartHubConfiguration.mqttClient().topics().deviceProvisioning())) {
            var provisioningPayload = this.objectMapper.readValue(mqttMessage.getPayload(), DeviceProvisioningPayload.class);
            this.eventBus.sendAndForget("device-provisioning", provisioningPayload);
        }
    }

    @Override
    public void deliveryComplete(IMqttToken iMqttToken) {

    }

    @Override
    public void connectComplete(boolean b, String s) {
        LOGGER.info("Connection to MQTT broker success");
    }

    @Override
    public void authPacketArrived(int i, MqttProperties mqttProperties) {

    }
}
