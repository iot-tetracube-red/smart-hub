using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace SmartHub.Data.Entities
{
    public class Telemetry
    {
        [Key]
        public Guid Id { get; set; }

        public DateTime RevealedAt { get; set; }

        [Column(TypeName = "jsonb")]
        public string DeviceStatus { get; set; }

        public Guid DeviceId { get; set; }

        [ForeignKey("DeviceId")]
        public Device Device { get; set; }
    }
}