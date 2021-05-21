import  java.io.IOException;
import functions.*;

public class Compile{
    public static void main(String[] args) throws IOException {
        scanner input=new scanner("C:\\Users\\27545\\IdeaProjects\\C-minusCompiler\\src\\test.txt");

        while(input.HasNextToken()) {
            Token currentToken=input.readToken();
            if(currentToken!=null){
                System.out.println(currentToken.getName().toString());
            }
        }

        SyntexAnalysis syntex=new SyntexAnalysis("C:\\Users\\27545\\IdeaProjects\\C-minusCompiler\\src\\test.txt");
        syntex.printTree();
    }
}