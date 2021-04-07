import java.io.FileReader;
import  java.io.IOException;

public class Compile{
    public static void main(String[] args) throws IOException {
        //System.out.println(getToken.readToken());
        FileReader input=new FileReader("C:\\Users\\Rayars\\Desktop\\C-minusCompiler\\src\\test.txt");
        int Ch=' ';
        while((Ch=input.read())!=-1){
            System.out.print((char)Ch);
        }
        System.out.println(Character.isDigit(52));
    }
}