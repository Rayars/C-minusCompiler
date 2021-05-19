package functions;

import sun.font.EAttribute;

import java.io.IOException;


public class SyntexAnalysis {
    //同一时间保存三个token，语法树根节点SyntexTree
    private Token currentToken,nextToken,thirdToken,acceptToken;
    private final scanner input;//文件流对象
    private final sTreeNode SyntexTree;

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

    public boolean isFirstType(Token token){
        if(token==null){
            return false;
        }
        return token.getName().toString().equals("int") || token.getName().toString().equals("void");
    }

    public boolean isFirstStmt(Token token){
        if(token==null){
            return false;
        }
        return token.getName().toString().equals("if") || token.getName().toString().equals("while") ||
                token.getName().toString().equals("return") || isFirstExp(token);
    }

    public boolean isFirstExp(Token token){
        if(token==null){
            return false;
        }
        return token.getName().toString().equals(";") || token.getType() == Token.ID ||
                token.getName().toString().equals("(") || token.getType() == Token.Num;

    }

    public boolean isRelop(Token token){
        if(token==null){
            return false;
        }
        return token.getName().toString().equals("<=") || token.getName().toString().equals(">=") || token.getName().toString().equals("==")
                || token.getName().toString().equals("<") || token.getName().toString().equals(">") || token.getName().toString().equals("!=");
    }

    public void getToken() throws IOException{
        //三个token依次向前移动
        if(currentToken!=null){
            acceptToken=currentToken;
        }
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
        return currentToken != null;
    }


    public void match(String token) throws IOException {
        if (currentToken.getName().toString().equals(token)) {
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
        temp.setKind(sTreeNode.ExpKind.Program);
        return temp;
    }

    public sTreeNode declaration_list() throws IOException{
        sTreeNode temp= new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.declaration);
        temp.Children.add(declaration());
        if(hasNextToken()){
            temp.Children.add(declaration_list());
        }
        return temp;
    }

    public sTreeNode declaration() throws IOException{//根据第三个Token判断是变量定义还是函数定义
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.declaration);
        if(thirdToken.getName().toString().equals("(")){
            temp.Children.add(fun_declaration());
        }else if(thirdToken.getName().toString().equals(";") || thirdToken.getName().toString().equals("[")){
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
        if(currentToken.getType()==Token.ID) {
            match(Token.ID);
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Identifier, acceptToken));
            if (currentToken.getName().toString().equals(";")) {
                match(";");
                temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter, acceptToken));
            } else if (currentToken.getName().toString().equals("[")) {
                match("[");
                temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter, acceptToken));
                match(Token.Num);
                temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Num, acceptToken));
                match("]");
                temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter, acceptToken));
                match(";");
                temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter, acceptToken));
            } else {
                error();
            }
        }else{
            error();
        }
        return temp;
    }

    public sTreeNode type_specifier() throws IOException{
        //type-specifier → int | void
        sTreeNode temp=new sTreeNode();
        if(currentToken.getName().toString().equals("int")) {
            match("int");
            temp = makeLeafNode(sTreeNode.ExpKind.type_spec, acceptToken);
        }else if(currentToken.getName().toString().equals("void")){
            match("void");
            temp = makeLeafNode(sTreeNode.ExpKind.type_spec, acceptToken);
        }
        return temp;
    }

    public sTreeNode fun_declaration() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.fun_declaration);
        temp.Children.add(type_specifier());
        match(Token.ID);
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Identifier,acceptToken));
        match("(");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        temp.Children.add(params());
        match(")");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        temp.Children.add(compound_stmt());
        return temp;
    }

    public sTreeNode params() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.params);
        if(currentToken.getName().toString().equals("void")){
            match("void");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.type_spec,acceptToken));
        }else{
            temp.Children.add(param_list());
        }
        return temp;
    }


    public sTreeNode compound_stmt() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.compound_stmt);
        match("{");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        if(isFirstType(currentToken)) {
            temp.Children.add(local_declarations());
        }
        if(isFirstStmt(currentToken)) {
            temp.Children.add(statement_list());
        }
        match("}");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        return temp;
    }

    public sTreeNode param_list() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.param_list);
        temp.Children.add(param());
        if(currentToken.getName().toString().equals(",")){
            match(",");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
            temp.Children.add(param_list());
        }
        return temp;
    }

    public sTreeNode param() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.param);
        temp.Children.add(type_specifier());
        match(Token.ID);
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Identifier,acceptToken));
        if(currentToken.getName().toString().equals("[")){
            match("[");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
            match("]");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        }
        return temp;
    }

    public sTreeNode local_declarations() throws IOException{
        //调用本函数代表已经确认不会推导出空串
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.local_declaration);
        temp.Children.add(var_declaration());
        if(isFirstType(currentToken)){
            temp.Children.add(local_declarations());
        }
        return temp;
    }

    public sTreeNode statement_list() throws IOException{
        //调用本函数代表已经确认不会推导出空串
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.statement_list);
        temp.Children.add(statement());
        if(!currentToken.getName().toString().equals("}")){
            temp.Children.add(statement_list());
        }
        return temp;
    }

    public sTreeNode statement() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.statement);
        if(isFirstExp(currentToken)){
            //expression_stmt
            temp.Children.add(expression_stmt());
        }else if(currentToken.getName().toString().equals("{")){
            temp.Children.add(compound_stmt());
        }else if(currentToken.getName().toString().equals("if")){
            temp.Children.add(selection_stmt());
        }else if(currentToken.getName().toString().equals("while")){
            temp.Children.add(iteration_stmt());
        }else if(currentToken.getName().toString().equals("return")){
            temp.Children.add(return_stmt());
        }
        return temp;
    }

    public sTreeNode selection_stmt()throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.IfStmt);
        match("if");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.reserved,acceptToken));
        match("(");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        temp.Children.add(expression());
        match(")");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        if(currentToken.getName().toString().equals("else")){
            match("else");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.reserved,acceptToken));
            temp.Children.add(statement());
        }
        return temp;
    }

    public sTreeNode iteration_stmt()throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.iteration_stmt);
        match("while");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.reserved,acceptToken));
        match("(");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        temp.Children.add(expression());
        match(")");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        temp.Children.add(statement());
        return temp;
    }

    public sTreeNode return_stmt()throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.return_stmt);
        match("return");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.reserved,acceptToken));
        if(currentToken.getName().toString().equals(";")){
            match(";");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        }else{
            temp.Children.add(expression());
        }
        return temp;
    }

    public sTreeNode expression_stmt() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.expression_stmt);
        if(!currentToken.getName().toString().equals(";")) {
            temp.Children.add(expression());
        }
        match(";");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter, acceptToken));
        return temp;
    }

    public sTreeNode expression() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.expression);
        if(currentToken.getType()==Token.ID){
            temp.Children.add(var());
            match("=");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.operator,acceptToken));
            temp.Children.add(expression());
        }else if(currentToken.getName().toString().equals("(") ||currentToken.getType()==Token.Num){
            temp.Children.add(simple_expression());
        }
        return temp;
    }

    public sTreeNode var() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.var);
        match(Token.ID);
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Identifier,acceptToken));
        if(currentToken.getName().toString().equals("[")){
            match("[");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
            temp.Children.add(expression());
            match("]");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        }
        return temp;
    }

    public sTreeNode simple_expression() throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.simple_expression);
        temp.Children.add(additive_expression());
        if(isRelop(currentToken)){
            temp.Children.add(relop());
            temp.Children.add(additive_expression());
        }
        return temp;
    }

    public sTreeNode additive_expression()throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.additive_expression);
        temp.Children.add(term());
        if(currentToken.getName().toString().equals("+") || currentToken.getName().toString().equals("-")){
            temp.Children.add(addop());
            temp.Children.add(additive_expression());
        }
        return temp;
    }

    public sTreeNode addop()throws IOException{
        match(currentToken.getName().toString());
        return makeLeafNode(sTreeNode.ExpKind.operator,acceptToken);
    }

    public sTreeNode relop() throws IOException{
        match(currentToken.getName().toString());
        return makeLeafNode(sTreeNode.ExpKind.operator,acceptToken);
    }

    public sTreeNode term()throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.term);
        temp.Children.add(factor());
        if(currentToken.getName().toString().equals("*")||currentToken.getName().toString().equals("/")){
            temp.Children.add(mulop());
            temp.Children.add(term());
        }
        return temp;
    }

    public sTreeNode factor()throws IOException{
        sTreeNode temp=new sTreeNode();
        if(currentToken.getName().toString().equals("(")){
            match("(");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
            temp.Children.add(expression());
            match(")");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        }else if(currentToken.getType()==Token.Num){
            match(Token.Num);
            temp=makeLeafNode(sTreeNode.ExpKind.Num,acceptToken);
        }else{
            if(nextToken.getName().toString().equals("(")){
                temp.Children.add(call());
            }else{
                temp.Children.add(var());
            }
        }
        return temp;
    }

    public sTreeNode mulop()throws IOException{
        match(currentToken.getName().toString());
        return makeLeafNode(sTreeNode.ExpKind.operator,acceptToken);
    }

    public sTreeNode call()throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.call);
        match(Token.ID);
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.Identifier,acceptToken));
        match("(");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        if(isFirstExp(currentToken)){
            temp.Children.add(args());
        }
        match(")");
        temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
        return temp;
    }

    public sTreeNode args()throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.args);
        temp.Children.add(arg_list());
        return temp;
    }

    public sTreeNode arg_list()throws IOException{
        sTreeNode temp=new sTreeNode();
        temp.setKind(sTreeNode.ExpKind.arg_list);
        temp.Children.add(expression());
        if(currentToken.getName().toString().equals(",")){
            match(",");
            temp.Children.add(makeLeafNode(sTreeNode.ExpKind.delimiter,acceptToken));
            temp.Children.add(arg_list());
        }
        return temp;
    }

    public void printTree(){

    }
}


