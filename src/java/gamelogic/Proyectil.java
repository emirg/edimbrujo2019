package gamelogic;

import engine.Action;
import engine.State;
import engine.StaticState;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import org.dyn4j.geometry.Vector2;
import org.json.simple.JSONObject;

public class Proyectil extends Entity {

    protected int number;

    public Proyectil(String name, boolean destroy, String id, double x, double y, double velocidadX, double velocidadY, int number) {
        super("Proyectil", destroy, id, x, y, velocidadX, velocidadY, 25, 25);
        this.number = number;
    }

    @Override
    public LinkedList<State> generate(LinkedList<State> states, LinkedList<StaticState> staticStates, HashMap<String, LinkedList<Action>> actions) {

        for (State state : states) {
            if (state.getName().equals("NavePlayer") && !((NavePlayer) state).dead) {
                NavePlayer player = ((NavePlayer) state);
                if (x == player.x && y == player.y) {
                    state.addEvent("hit");
                    this.addEvent("hit");
                }
            }
        }

        return null;
    }

    @Override
    public State next(LinkedList<State> states, LinkedList<StaticState> staticStates, HashMap<String, LinkedList<Action>> actions) {
        hasChanged = true;
        double nuevoX = x + velocidad.x;
        double nuevoY = y + velocidad.y;
        boolean destruido = destroy;

        //falta considerar que es un mundo de 360°
        LinkedList<String> events = getEvents();
        if (!events.isEmpty()) {
            hasChanged = true;
            for (String event : events) {
                switch (event) {
                    case "hit":
                        destruido = true;
                        break;
                }
            }
        }
        Proyectil newArrow = new Proyectil(name, destruido, id, nuevoX, nuevoY, velocidad.x, velocidad.y, number);
        return newArrow;
    }

    @Override
    public void setState(State newProyectil) {
        super.setState(newProyectil);
        id = ((Proyectil) newProyectil).id;
        number = ((Proyectil) newProyectil).number;
        velocidad = new Vector2(velocidad.x, velocidad.y);
    }

    @Override
    protected Object clone() {
        Proyectil clon = new Proyectil(name, destroy, id, x, y, velocidad.x, velocidad.y, number);
        return clon;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jProyectil = new JSONObject();
        JSONObject jsonAttrs = new JSONObject();
        jsonAttrs.put("super", super.toJSON());
        jsonAttrs.put("number", number);
        jProyectil.put("Proyectil", jsonAttrs);
        return jProyectil;
    }

}
