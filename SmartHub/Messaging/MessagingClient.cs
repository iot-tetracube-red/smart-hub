using System;
using MQTTnet;
using MQTTnet.Client.Options;
using MQTTnet.Extensions.ManagedClient;
using SmartHub.Settings;

namespace SmartHub.Messaging
{
    public class MessagingClient
    {
        private readonly SmartHubSettings _smartHubSettings;

        public IManagedMqttClient MqttClient {get;}
        
        public MessagingClient(SmartHubSettings smartHubSettings)
        {
            _smartHubSettings = smartHubSettings;

            var messageBuilder = new MqttClientOptionsBuilder()
                .WithClientId(_smartHubSettings.Messaging.ClientId)
                .WithCredentials(_smartHubSettings.Messaging.User, _smartHubSettings.Messaging.Password)
                .WithTcpServer(_smartHubSettings.Messaging.Host, _smartHubSettings.Messaging.Port)
                .WithCleanSession();
            var options = _smartHubSettings.Messaging.Port == 1883
                ? messageBuilder.Build()
                : messageBuilder
                    .WithTls()
                    .Build();
            var managedOptions = new ManagedMqttClientOptionsBuilder()
                .WithAutoReconnectDelay(TimeSpan.FromSeconds(5))
                .WithClientOptions(options)
                .Build();

            MqttClient = new MqttFactory().CreateManagedMqttClient();
            MqttClient.StartAsync(managedOptions);
        }
    }
}