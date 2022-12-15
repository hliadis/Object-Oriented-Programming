/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw2;

/**
 *
 * @author Maria Pantazi
	Ilias Iliadis
 */
public class YUVPixel {
    private int YUV;
    private int y;
    private int u;
    private int v;

    //constants for AND mask for each color of the pixel.
    static final int Y = 0x0000FFFF;
    static final int U = 0x00FF00FF;
    static final int V = 0x00FFFF00;
    
    //constructor that puts red, green, blue into the pixel.
    public YUVPixel(short y, short u, short v){
        this.y = y;
        YUV = (this.y << 16) | YUV;
        this.u = u;
        YUV = (this.u << 8) | YUV;
        this.v = v;
        YUV = this.v | YUV;
    }

    public YUVPixel(YUVPixel pixel){
        YUV = pixel.YUV;
    }
    
    //constructor that initializes a YUVPixel from an RGBPixel.
    public YUVPixel(RGBPixel pixel){
        
        y = ((66 * pixel.getRed() + 129 * pixel.getGreen() + 25 * pixel.getBlue() + 128) >> 8) + 16;
        u = ((-38 * pixel.getRed() - 74 * pixel.getGreen() + 112 * pixel.getBlue() + 128) >> 8) + 128;
        v = ((112 * pixel.getRed() - 94 * pixel.getGreen() - 18 * pixel.getBlue() + 128) >> 8) + 128;

        setYUV((short)y, (short)u, (short)v);
    }

    short getY(){
        short y = (short)(YUV >> 16);

        return y;
    }

    short getU(){
        short u = (short)((YUV >> 8) & 255);

        return u;
    }

    short getV(){
        short v = (short)(YUV & 255);

        return v;
    }

    void setY(short y){
        this.y = y;
        YUV = YUV & Y;
        YUV = (this.y << 16) | YUV;
    }
    
    void setU(short u){
        this.u = u;
        YUV = YUV & U;
        YUV = (this.u << 8) | YUV;
    }

    void setV(short v){
        this.v = v;
        YUV = YUV & V;
        YUV = this.v | YUV;
    }

    int getYUV(){
        return YUV;
    }

    void setYUV(int value){
        YUV = value;
    }

    final void setYUV(short y, short u, short v){
        setY(y);
        setU(u);
        setV(v);
    }
    
    @Override
    public String toString() {
        return (getY() + " " + getU() + " " + getV());
    }
}
