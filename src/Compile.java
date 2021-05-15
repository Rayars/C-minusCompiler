import  java.io.IOException;
import functions.*;

public class Compile{
    public static void main(String[] args) throws IOException {
        scanner input=new scanner("C:\\Users\\27545\\IdeaProjects\\C-minusCompiler\\src\\test.txt");
        int i=40;
        while(i!=0) {
            System.out.println(input.readToken());
            i--;
        }
    }
}