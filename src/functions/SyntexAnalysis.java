package functions;

import java.io.IOException;


public class SyntexAnalysis {

    private Token currentToken;//当前token
    private scanner input;//文件流对象
    private sTreeNode SyntexTree;

    public SyntexAnalysis(String filepath) throws IOException {
        input=new scanner(filepath);
        currentToken=input.readToken();
        SyntexTree=program();
    }

    public void match(StringBuilder Vn) throws IOException{

    }

    public void error(){

    }

    public sTreeNode program() throws IOException{
        sTreeNode temp=declaration_list();
        return temp;
    }

    public sTreeNode declaration_list() throws IOException{
        sTreeNode temp=declaration();
        if(input.HasNextToken()){
            declaration_list();
        }
        return temp;
    }

    public sTreeNode declaration() throws IOException{

    }

    public void statement(){

    }
}


