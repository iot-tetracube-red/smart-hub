namespace SmartHub.Settings
{
    public class MessagingSettings
    {
        public string ClientId { get; set; }
        public string Host { get; set; }
        public int Port { get; set; }
        public string User { get; set; }
        public string Password { get; set; }
        public TopicSettings Topic { get; set; }
    }
}