import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

//Vector.sub(ep, tp);
//ev.setmag(max_speed);

//ev = Vector.sub(ep, Vector.add(tp, Vector.mult(tv,t)))
//ev.setmag(max_speed);


public class Game extends JFrame implements KeyListener{

    private String Key;

    public int pSize = 100;
    //window vars
    private final int MAX_FPS; //maximum refresh rate
    private final int WIDTH; //window width
    private final int HEIGHT; //window height

    enum GAME_STATES{
        MENU,
        PLAY,
    }
    public GAME_STATES GameStates = GAME_STATES.PLAY;

    //double buffer strategy
    private BufferStrategy strategy;
    public ArrayList<Integer> keys = new ArrayList<>();

    //loop variables
    private boolean isRunning = true; //is the window running
    private long rest = 0; //how long to sleep the main thread

    //timing variables
    private float dt; //delta time
    private long lastFrame; //time since last frame
    private long startFrame; //time since start of frame
    private int fps; //current fps

    private Vector p;
    private Vector v;
    private Vector a;
    private Vector d;

    static int rate = 500;

    public Game(int width, int height, int fps){
        super("Game");
        this.MAX_FPS = fps;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    /*
     * init()
     * initializes all variables needed before the window opens and refreshes
     */
    public void init(){
        //initializes window size
        setBounds(0, 0, WIDTH, HEIGHT);
        setResizable(false);

        //set jframe visible
        setVisible(true);

        //set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create double buffer strategy
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        addKeyListener(this);
        setFocusable(true);

        //set initial lastFrame var
        lastFrame = System.currentTimeMillis();

        //set background window color
        setBackground(Color.BLUE);
        p = new Vector(200 + (100 - pSize) / 2, 200 + (100 - pSize) / 2);
        v = new Vector(0, 0);
        a = new Vector(0, 0);
        d = new Vector(p.ix + 100, p.iy);
    }

    /*
     * update()
     * updates all relevant game variables before the frame draws
     */
    private void update() {
        //update current fps
        fps = (int) (1f / dt);

        switch(GameStates) {
            case PLAY:
                handleKeys();
                break;
        }
    }

    /*
     * draw()
     * gets the canvas (Graphics2D) and draws all elements
     * disposes canvas and then flips the buffer
     */
    private void draw(){
        //get canvas
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

        switch(GameStates) {
            case MENU:
                break;
            case PLAY:
                //clear screen
                g.clearRect(0,0,WIDTH, HEIGHT);

                g.setColor(Color.YELLOW);
                g.fillRect( p.ix, p.iy, pSize, pSize);

                g.setColor(Color.YELLOW);
                g.fillRect(p.ix + 100, p.iy, pSize, pSize);


                g.setColor(Color.RED);
                g.fillRect(320, 320, 60, 60);

                //draw fps
                g.setColor(Color.GREEN);
                g.drawString(Long.toString(fps), 10, 40);
                break;
        }


        //release resources, show the buffer
        g.dispose();
        strategy.show();
    }

    private void handleKeys() {
        for(int i = 0; i < keys.size(); i++) {
            switch (keys.get(i)) {
                case KeyEvent.VK_LEFT:
                    v = new Vector(rate, v.y);
                    Key = "right";
                    break;
                case KeyEvent.VK_RIGHT:
                    v = new Vector(-rate, v.y);
                    Key = "left";
                    break;
                case KeyEvent.VK_DOWN:
                    v = new Vector(v.x, -rate);
                    Key = "up";
                    break;
                case KeyEvent.VK_UP:
                    v = new Vector(v.x, rate);
                    Key = "down";
                    break;
            }
            //for(i = 0; i < 2; i++ ) {}

            Collision.CollisionOut(320, 320, 60, 60, p, v, pSize, Key);
            Collision.CollisionOut(320, 320, 60, 60, d, v, pSize, Key);

            //Collision.CollisionIn(0, 0, WIDTH, HEIGHT, p, v, 40, Key);

            p.add(Vector.mult(v, dt));
            d.add(Vector.mult(v, dt));
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(!keys.contains((keyEvent.getKeyCode()))) {
            keys.add(keyEvent.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        for(int i = keys.size() - 1; i >= 0; i--)
            if(keys.get(i) == keyEvent.getKeyCode())
                keys.remove(i);
        v = new Vector(0, 0);
    }

    /*
         * run()
         * calls init() to initialize variables
         * loops using isRunning
            * updates all timing variables and then calls update() and draw()
            * dynamically sleeps the main thread to maintain a framerate close to target fps
         */
    public void run(){
        init();

        while(isRunning){

            //new loop, clock the start
            startFrame = System.currentTimeMillis();

            //calculate delta time
            dt = (float)(startFrame - lastFrame)/1000;

            //update lastFrame for next dt
            lastFrame = startFrame;

            //call update and draw methods
            update();
            draw();

            //dynamic thread sleep, only sleep the time we need to cap the framerate
            //rest = (max fps sleep time) - (time it took to execute this frame)
            rest = (1000/MAX_FPS) - (System.currentTimeMillis() - startFrame);
            if(rest > 0){ //if we stayed within frame "budget", sleep away the rest of it
                try{ Thread.sleep(rest); }
                catch (InterruptedException e){ e.printStackTrace(); }
            }
        }
    }

    //entry point for application
    public static void main(String[] args){
        Game game = new Game(700, 700, 60);
        game.run();
    }

}

