package ecs.Components;

import edu.usu.audio.Sound;

public class WinSound extends Component{
    public edu.usu.audio.Sound sound;
    public String name;
    public WinSound(Sound sound, String name) {
        this.sound = sound;
        this.name = name;
    }
}
