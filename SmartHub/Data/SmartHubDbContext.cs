using Microsoft.EntityFrameworkCore;
using SmartHub.Data.Entities;

namespace SmartHub.Data
{
    public class SmartHubDbContext : DbContext
    {
        public DbSet<Action> Actions { get; set; }
        public DbSet<Device> Devices { get; set; }
        public DbSet<Telemetry> TelemetryData { get; set; }

        public SmartHubDbContext(DbContextOptions<SmartHubDbContext> options) : base(options)
        {

        }
    }
}