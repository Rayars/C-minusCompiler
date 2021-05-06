import  java.io.IOException;
import functions.getToken;

public class Compile{
    public static void main(String[] args) throws IOException {
        getToken gt=new getToken("C:\\Users\\27545\\IdeaProjects\\C-minusCompiler\\src\\test.txt");
        int i=40;
        while(i!=0) {
            System.out.println(gt.readToken());
            i--;
        }
    }
}