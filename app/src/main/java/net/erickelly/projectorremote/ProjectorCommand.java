package net.erickelly.projectorremote;

public abstract class ProjectorCommand {

    public enum Power implements Command {
        ON, OFF, TOGGLE;

        @Override
        public String toString() {
            return name().toUpperCase();
        }

        public String endpoint() {
            return "/power";
        }
    }

    public enum Mute implements Command {
        ON, OFF, TOGGLE;

        @Override
        public String toString() {
            return name().toUpperCase();
        }

        public String endpoint() {
            return "/mute";
        }
    }

    public interface Command {
        String endpoint();
    }
}
