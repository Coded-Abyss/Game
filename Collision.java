public class Collision {


    public static void CollisionIn(int x, int y, int sizeX, int sizeY, Vector p, Vector v, int pSize, String s) {

        if(p.x < x && s == "left") {
            v.setX(0);
            v.setY(0);
        }

        if(p.x > sizeX - pSize && s == "right") {
            v.setX(0);
            v.setY(0);
        }

        if(p.y < y + 20 && s == "up") {
            v.setX(0);
            v.setY(0);
        }

        if(p.y > sizeY - pSize && s == "down") {
            v.setX(0);
            v.setY(0);
        }
    }
    
    public static void CollisionOut(int x, int y, int sizeX, int sizeY, Vector p, Vector v, int pSize, String s) {

        //vertical
        if (p.y > y - pSize && p.y < y - pSize + sizeY && s == "down" && p.x + pSize > x && p.x < x + sizeX) {
            v.setX(0);
            v.setY(0);
        }

        if (p.y < y + sizeY && p.y > y + sizeY - pSize && s == "up" && p.x + pSize > x && p.x < x + sizeX) {
            v.setX(0);
            v.setY(0);
        }

        //horizontal
        if (p.x > x - pSize && p.x < x - pSize + sizeX && s == "right" && p.y + pSize > y && p.y < y + sizeX) {
            v.setX(0);
            v.setY(0);
        }

        if (p.x < x + sizeX && p.x > x + sizeX - pSize && s == "left" && p.y + pSize > y && p.y < y + sizeY) {
            v.setX(0);
            v.setY(0);
        }
    }
}
