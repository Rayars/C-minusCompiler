package functions;

public class Token {
    private int Type;
    private StringBuilder name;
    public static final int Reserved=1,ID=2,Num=3,Op=4,Bound=5;

    public Token(){
        Type=0;
        name=new StringBuilder(10);
    }

    public Token(int t,StringBuilder n){
        Type=t;name=n;
    }

    public void setType(int T){
        Type=T;
    }

    public void setName(StringBuilder N){
        name=N;
    }

    public int getType(){
        return Type;
    }

    public StringBuilder getName(){
        return name;
    }
}
