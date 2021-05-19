import  java.io.IOException;
import functions.*;

public class Compile{
    public static void main(String[] args) throws IOException {
        scanner input=new scanner("C:\\Users\\Rayars\\Desktop\\C-minusCompiler\\src\\test.txt");

        while(input.HasNextToken()) {
            Token currentToken=input.readToken();
            System.out.println(currentToken.getName().toString());
        }

        SyntexAnalysis syntex=new SyntexAnalysis("C:\\Users\\Rayars\\Desktop\\C-minusCompiler\\src\\test.txt");
        syntex.printTree();
    }
}