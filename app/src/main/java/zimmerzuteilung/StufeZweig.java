package zimmerzuteilung;

public class StufeZweig {
    public int stufe;
    public Zweig zweig;

    public StufeZweig(int stufe, Zweig zweig) throws IllegalArgumentException{
        if(stufe<9 || stufe>12){
            throw new IllegalArgumentException();
        }
        this.stufe = stufe;
        this.zweig = zweig;
    }
}
