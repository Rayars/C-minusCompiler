import  java.io.IOException;
import functions.*;

public class Compile{
    public static void main(String[] args) throws IOException {
        scanner input=new scanner("C:\\Users\\27545\\IdeaProjects\\C-minusCompiler\\src\\test.txt");
        int i=70;
        while(i!=0) {
            Token currentToken=input.readToken();
            System.out.println(currentToken.getName().toString());
            i--;
        }
    }
}