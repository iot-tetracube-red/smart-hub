using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace SmartHub.Data.Entities
{
    public class Action
    {
        [Key]
        public Guid Id { get; set; }

        public string Name { get; set; }
        
        public string AlexaSlotId { get; set; }
        
        public string CommandTopic { get; set; }
        
        public string QueryingTopic { get; set; }
        
        public Guid DeviceId { get; set; }

        [ForeignKey("DeviceId")]
        public Device Device { get; set; }
    }
}