package functions;

import java.io.IOException;


public class SyntexAnalysis {

    private Token currentToken,nextToken,thirdToken;//当前token
    private scanner input;//文件流对象
    private sTreeNode SyntexTree;

    public SyntexAnalysis(String filepath) throws IOException {
        input=new scanner(filepath);
        currentToken=input.readToken();
        nextToken=input.readToken();
        thirdToken=input.readToken();
        SyntexTree=program();
    }

    public void getToken() throws IOException{
        if(nextToken!=null){
            currentToken=nextToken;
        }else{
            currentToken=null;
            return;
        }
        if(thirdToken!=null){
            nextToken=thirdToken;
        }else{
            nextToken=null;
            return;
        }
        if(input.HasNextToken()) {
            thirdToken = input.readToken();
        }else{
            thirdToken=null;
        }
    }

    public boolean hasNextToken(){
        if(currentToken!=null){
            return true;
        }else{
            return false;
        }
    }


    public void match(int type) throws IOException{
        if(currentToken.getType()==type){
            getToken();
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
        sTreeNode temp= declaration();
        if(hasNextToken()){
            sTreeNode newTemp=new sTreeNode(sTreeNode.ExpKind.declaration);
            newTemp.leftChild=temp;
            newTemp.rightChild=declaration_list();
            temp=newTemp;
        }
        return temp;
    }

    public sTreeNode declaration() throws IOException{//根据第三个Token判断是变量定义还是函数定义
        sTreeNode temp=new sTreeNode();
        if(thirdToken.getName().toString()=="("){
            temp=fun_declaration();
        }else if(thirdToken.getName().toString()==";"||thirdToken.getName().toString()=="["){
            temp=var_declaration();
        }else{
            error();
        }
        return temp;
    }

    public sTreeNode var_declaration() throws IOException{
        sTreeNode temp=new sTreeNode(sTreeNode.ExpKind.var_declaration);
        temp.leftChild=type_specifier();//消耗一个token
        if(currentToken.getType()==Token.ID) {
            temp.rightChild=new sTreeNode(sTreeNode.ExpKind.Identifier,currentToken.getName());
            match(Token.ID);//如果当前token是ID的话就消耗
        }else{
            error();
        }
        if(currentToken.getName().toString()==";"){
            match(Token.Bound);
        }else if(currentToken.getName().toString()=="["){
            match(Token.Bound);
            temp.moreChild=new sTreeNode(sTreeNode.ExpKind.var_num,currentToken.getName());
            match(Token.Num);
            match(Token.Bound);
        }else{
            error();
        }
        return temp;
    }

    public sTreeNode type_specifier() throws IOException{
        sTreeNode temp=new sTreeNode();
        if(currentToken.getName().toString()=="int"||currentToken.getName().toString()=="void") {
            temp = new sTreeNode(sTreeNode.ExpKind.type, currentToken.getName());
            match(Token.Reserved);
        }
        return temp;
    }

    public sTreeNode fun_declaration() throws IOException{
        sTreeNode temp=new sTreeNode(sTreeNode.ExpKind.fun_declaration);
        temp.leftChild=type_specifier()
        return temp;
    }

    public void statement(){

    }
}


