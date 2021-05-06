package functions;
import functions.*;

import java.io.IOException;


public class SyntexAnalysis {

    private StringBuilder currentToken;
    private getToken Tokens;

    public SyntexAnalysis() throws IOException {
        Tokens=new getToken("C:\\Users\\27545\\IdeaProjects\\C-minusCompiler\\src\\test.txt");
    }

    public void match(StringBuilder Vn){

    }

    public void program(){
        stmt_sequence();
    }

    public void stmt_sequence(){
        statement();

    }

    public void statement(){

    }
}


