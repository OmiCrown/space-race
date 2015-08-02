package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.*;

/**
 * Created by Pyatnitskiy.a on 02.08.2015.
 */
public class Bomb extends PointImpl implements State<Elements, Player>, Tickable {
    private Direction direction;

    public Bomb(int x, int y) {
        super(x, y);
        direction = Direction.DOWN;
    }

    public Bomb(Point pt) {
        super(pt);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BOMB;
    }

    @Override
    public void tick() {
        if (direction != null) {

            int newX = direction.changeX(x);
            int newY = direction.changeY(y);
            move(newX, newY);
        }

    }
}
