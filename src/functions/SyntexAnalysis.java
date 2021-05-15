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

    public void match(String value) throws IOException{//用于运算符、界符、保留字的match
        if(currentToken.getName().toString()==value){
            currentToken=input.readToken();
        }else{
            error();
        }
    }

    public void match(int type) throws IOException{//用于ID,NUM类型的token
        if(currentToken.getType()==type){
            currentToken=input.readToken();
        }else{
            error();
        }
    }

    public void error(){
        System.out.println("ERROR!");
        System.exit(1);
    }

    public sTreeNode program() throws IOException{
        sTreeNode temp=declaration_list();
        return temp;
    }

    public sTreeNode declaration_list() throws IOException{
        sTreeNode temp=declaration();
        if(input.HasNextToken()){
            temp.rightChild=declaration_list();
        }
        return temp;
    }

    public sTreeNode declaration() throws IOException{
        sTreeNode temp=var_declaration();
        if(temp==null){
            temp=fun_declaration();
        }
        return temp;
    }

    public sTreeNode var_declaration() throws IOException{
        sTreeNode temp=type_specifier();
        if(currentToken.getType()==Token.ID){
            match(Token.ID);
        }
        return temp;
    }

    public sTreeNode type_specifier() throws IOException{
        sTreeNode temp=new sTreeNode();
        if(currentToken.getName().toString()=="int"){
            temp=new sTreeNode(sTreeNode.ExpKind.name,currentToken.getName());
            match("int");
        }else if(currentToken.getName().toString()=="void"){
            temp=new sTreeNode(sTreeNode.ExpKind.name,currentToken.getName());
            match("void");
        }
        return temp;
    }

    public sTreeNode fun_declaration() throws IOException{
        sTreeNode temp=new sTreeNode();
        return temp;
    }

    public void statement(){

    }
}


