package functions;

import java.io.IOException;


public class SyntexAnalysis {
    //同一时间保存三个token，语法树根节点SyntexTree
    private Token currentToken,nextToken,thirdToken;
    private scanner input;//文件流对象
    private sTreeNode SyntexTree;

    public SyntexAnalysis(String filepath) throws IOException {
        input=new scanner(filepath);
        currentToken=input.readToken();
        nextToken=input.readToken();
        thirdToken=input.readToken();
        SyntexTree=program();
    }

    public sTreeNode makeLeafNode(sTreeNode.ExpKind kind,Token token){
        sTreeNode temp=new sTreeNode();
        temp.setKind(kind);
        temp.setName(token.getName());
        return temp;
    }

    public void getToken() throws IOException{
        //三个token依次向前移动
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


    public void match(String token) throws IOException {
        if (currentToken.getName().toString() == token) {
            getToken();
        }else{
            error();
        }
    }

    public void match(int type) throws IOException {
        if (currentToken.getType()==type) {
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
        sTreeNode temp= new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.declaration);
        temp.Children.add(declaration());
        if(hasNextToken()){
            temp.sibling=declaration_list();
        }
        return temp;
    }

    public sTreeNode declaration() throws IOException{//根据第三个Token判断是变量定义还是函数定义
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.declaration);
        if(thirdToken.getName().toString()=="("){
            temp.Children.add(fun_declaration());
        }else if(thirdToken.getName().toString()==";"||thirdToken.getName().toString()=="["){
            temp.Children.add(var_declaration());
        }else{
            error();
        }
        return temp;
    }

    public sTreeNode var_declaration() throws IOException{
        //var_declaration->type_spec ID;|type_spec ID [NUM];
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.var_declaration);
        temp.Children.add(type_specifier());
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Identifier,currentToken));
        match(Token.ID);
        if(thirdToken.getName().toString()==";"){
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,currentToken));
            match(";");
        }else if(thirdToken.getName().toString()=="["){
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,currentToken));
            match("[");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Num,currentToken));
            match(Token.Num);
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,currentToken));
            match("]");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,currentToken));
            match(";");
        }else{
            error();
        }
        return temp;
    }

    public sTreeNode type_specifier() throws IOException{
        //type-specifier → int | void
        sTreeNode temp=makeLeafNode(sTreeNode.ExpKind.type_spec,currentToken);
        return temp;
    }

    public sTreeNode fun_declaration() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.fun_declaration);
        temp.Children.add(type_specifier());
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Identifier,currentToken));
        match(Token.ID);
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,currentToken));
        match("(");
        temp.Children.add(params());
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,currentToken));
        match(")");
        temp.Children.add(compound_stmt());
        return temp;
    }

    public sTreeNode params(){

    }


    public sTreeNode compound_stmt(){

    }
}


