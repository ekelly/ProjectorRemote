package net.erickelly.projectorremote;

/**
 * Created by eric on 9/12/15.
 */
public abstract class ProjectorCommand {

    public String endpoint() {
        return "/";
    }

    public static class Power extends ProjectorCommand {
        private String state;
        public static Power ON = new Power("ON"),
                OFF = new Power("OFF"),
                TOGGLE = new Power("TOGGLE");

        private Power(String state) {
            this.state = state;
        }

        public String toString() {
            return state;
        }

        public String endpoint() {
            return "/power";
        }
    }

    public static class Mute extends ProjectorCommand {
        private String state;
        public static Mute ON = new Mute("ON"), OFF = new Mute("OFF"), TOGGLE = new Mute("TOGGLE");

        private Mute(String state) {
            this.state = state;
        }

        public String toString() {
            return state;
        }

        public String endpoint() {
            return "/mute";
        }
    }
}
