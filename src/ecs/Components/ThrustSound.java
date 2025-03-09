package ecs.Components;

import edu.usu.audio.Sound;

public class ThrustSound extends Component{
    public edu.usu.audio.Sound sound;
    public String name;
    public ThrustSound (Sound sound, String name) {
        this.sound = sound;
        this.name = name;
    }
}
