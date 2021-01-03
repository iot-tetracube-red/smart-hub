using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace SmartHub.Data.Entities
{
    public class Device
    {
        [Key]
        public Guid Id { get; set; }
        
        public string Name { get; set; }
        
        public string FeedbackTopic { get; set; }
        
        public bool IsOnline { get; set; }
        
        public string AlexaSlotId { get; set; }

        public List<Action> Actions { get; set; }
        
        public List<Telemetry> TelemetryData { get; set; }
    }
}