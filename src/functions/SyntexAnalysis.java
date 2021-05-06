package functions;

import java.io.IOException;


public class SyntexAnalysis {

    private StringBuilder currentToken;
    private getToken Tokens;

    public SyntexAnalysis() throws IOException {
        Tokens=new getToken("C:\\Users\\27545\\IdeaProjects\\C-minusCompiler\\src\\test.txt");
        currentToken=Tokens.readToken();
    }

    public void match(StringBuilder Vn) throws IOException{
        if(Vn==currentToken){
            currentToken=Tokens.readToken();
        }else{
            error();
        }
    }

    public void error(){

    }

    public void program() throws IOException{
        stmt_sequence();
    }

    public void stmt_sequence() throws IOException{
        statement();
        StringBuilder Vt=new StringBuilder("10");
        Vt.append(";");
        match(Vt);

    }

    public void statement(){

    }
}


