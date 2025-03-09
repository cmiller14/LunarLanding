package ecs.Components;

import edu.usu.audio.Sound;

public class CrashSound extends Component{
    public edu.usu.audio.Sound sound;
    public String name;
    public CrashSound(Sound sound, String name) {
        this.sound = sound;
        this.name = name;
    }
}
