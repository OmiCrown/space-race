package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Sample#tick()}
 */
public class Sample implements Tickable, Field {

    public static final int NEW_APPEAR_PERIOD = 3;
    private List<Player> players;

    private final int size;
    private Dice dice;
    private List<Wall> walls;
    private List<Bullet> bullets;
    private List<Explosion> explosions;
    private List<Stone> stones;
    private boolean isNewStone = true;
    private int countStone = 0;
    private int countBomb = 0;
    private List<Bomb> bombs;
    private boolean isNewBomb = true;

    public Sample(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        size = level.getSize();
        players = new LinkedList<>();
        stones = new LinkedList<>();
        bombs = new LinkedList<>();
        bullets = new LinkedList<>();
        explosions = new LinkedList<>();

    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        explosions.clear();

        createStone();
        createBomb();
        tickHeroes();
        tickBombs();
        tickBullets();
        removeStoneDestroyedByBullet();
        tickStones();
        removeStoneDestroyedByBullet();
        removeStoneOutOfBoard();
        removeBulleteOutOfBoard();

    }

    private void createBomb() {
        countBomb++;
        if (countBomb == NEW_APPEAR_PERIOD) {
            int x = dice.next(size - 2);
            if (x != -1) {
                addBomb(x + 1);
                countBomb = 0;
            }
        }
    }

    private void tickBombs() {
        for (Bomb bomb : bombs) {
            bomb.tick();
        }
    }

    private void createStone() {
        countStone++;
        if (countStone == NEW_APPEAR_PERIOD) {
            int x = dice.next(size - 2);
            if (x != -1) {
                addStone(x + 1);
                countStone = 0;
            }
        }
    }

    private void tickHeroes() {
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
        }
    }

    private void tickBullets() {
        for (Bullet bullet : bullets) {
            bullet.tick();
        }
    }

    private void tickStones() {
        for (Stone stone : stones) {
            stone.tick();
        }
    }

    private void removeStoneOutOfBoard() {
        for (Bullet bullet : new ArrayList<>(bullets)){
            if (bullet.isOutOf(size)){
                bullets.remove(bullet);
            }
        }
    }

    private void removeBulleteOutOfBoard() {
        for (Stone stone : new ArrayList<>(stones)){
            if (stone.isOutOf(size)){
                stones.remove(stone);
            }
        }
    }

    private void removeStoneDestroyedByBullet() {
        for (Bullet bullet : new ArrayList<>(bullets)) { // TODO to use iterator.remove
            if (stones.contains(bullet)) {
                bullets.remove(bullet);
                stones.remove(bullet);
                explosions.add(new Explosion(bullet));
            }
        }
    }

    public int size() {
        return size;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Sample.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<>();
                result.addAll(getHeroes());
                result.addAll(stones);
                result.addAll(bombs);
                result.addAll(walls);
                result.addAll(bullets);
                result.addAll(explosions);
                return result;
            }
        };
    }

    public List<Hero> getHeroes() {
        List<Hero> heroes = new LinkedList<>();
        for (Player player : players) {
           heroes.add(player.getHero());
        }
        return heroes;
    }

    @Override
    public void addBullet(int x, int y) {
        bullets.add(new Bullet(x, y));
    }

    @Override
    public void addStone(int x) {
        stones.add(new Stone(x, size));
        isNewStone = false;
    }

    public void addBomb(int x) {
        bombs.add(new Bomb(x, size));
        isNewBomb = false;
    }
}
















