package ecs.Components;

public class Ship extends Component{
    public boolean collision;
    public boolean win;

    public Ship() {
        this.collision = false;
        this.win = false;
    }
}
