package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.*;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 */
public class Hero extends PointImpl implements Joystick, Tickable, State<Elements, Player> {

    private Field field;
    private boolean alive;
    private Direction direction;
    private boolean fire;

    public Hero(Point xy) {
        super(xy);
        direction = null;
        fire = false;
        alive = true;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        if (!alive) return;

        fire = true;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (fire){
            field.addBullet(this.getX(), this.getY());
            fire = false;
        }

        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);
            move(newX, newY);
        }
}

    public void die(){
        alive = false;
    }
    public boolean isAlive() {
        return alive;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.HERO;
    }
}
