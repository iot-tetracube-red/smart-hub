using System;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using MQTTnet;
using MQTTnet.Client.Receiving;
using SmartHub.Settings;

namespace SmartHub.Messaging.Workers
{
    public class TopicsConsumersBackgroundService : BackgroundService
    {
        private readonly ILogger<TopicsConsumersBackgroundService> _logger;
        private readonly MessagingClient _messagingClient;
        private readonly SmartHubSettings _smartHubSettings;

        public TopicsConsumersBackgroundService(MessagingClient messagingClient,
                                                IConfiguration configuration,
                                                ILogger<TopicsConsumersBackgroundService> logger)
        {
            _logger = logger;
            _messagingClient = messagingClient;
            _smartHubSettings = configuration.GetSection("SmartHub").Get<SmartHubSettings>();
        }

        protected override Task ExecuteAsync(CancellationToken stoppingToken)
        {
            _messagingClient.MqttClient.SubscribeAsync(
                new MqttTopicFilter[] {
                    new MqttTopicFilterBuilder()
                        .WithTopic(_smartHubSettings.Messaging.Topic.DeviceProvisioning)
                        .WithQualityOfServiceLevel(MQTTnet.Protocol.MqttQualityOfServiceLevel.ExactlyOnce)
                        .Build()
                });
            _messagingClient.MqttClient.ApplicationMessageReceivedHandler = new MqttApplicationMessageReceivedHandlerDelegate(HandleDeviceProvisioningMessage);
            return Task.CompletedTask;
        }

        private void HandleDeviceProvisioningMessage(MqttApplicationMessageReceivedEventArgs eventArgs)
        {
            if (eventArgs.ApplicationMessage.Topic != _smartHubSettings.Messaging.Topic.DeviceProvisioning) {
                return;
            }
            _logger.LogInformation($"Message arrived {eventArgs.ApplicationMessage.Topic}: {Encoding.UTF8.GetString(eventArgs.ApplicationMessage.Payload)}");
        }

    }
}