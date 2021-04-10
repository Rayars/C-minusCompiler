import java.io.FileReader;
import  java.io.IOException;
import functions.getToken;

public class Compile{
    public static void main(String[] args) throws IOException {
        getToken gt=new getToken();
        gt.readToken("C:\\Users\\Rayars\\Desktop\\C-minusCompiler\\src\\test.txt");
    }
}