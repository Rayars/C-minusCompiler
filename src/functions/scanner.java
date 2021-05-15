package functions;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class scanner {//创建对象，代表文件的字符流
    private final int START=-1,DONE=0,ERROR=1,INNUM=2,INFLOAT=9,INID=3,INASSIGN=4,INCOMMENT=5,INCOMP=6;
    private final List<String> reserved=new ArrayList<String>();//保留字列表
    private FileReader input;//文件字符流
    private boolean hasNextToken=true;

    public scanner(String filePath) throws IOException{
        reserved.add("int");
        reserved.add("float");
        reserved.add("long");
        reserved.add("if");
        reserved.add("else");
        reserved.add("return");
        reserved.add("void");
        reserved.add("char");
        reserved.add("switch");
        reserved.add("case");
        input = new FileReader(filePath);
    }

    public Token readToken() throws IOException {//返回读取的token
        StringBuilder currentToken = new StringBuilder(10);
        boolean EOF=false;
        boolean unget = false;
        int beforeCh = 0;
        Token newToken=new Token();
        if(!EOF) {
            currentToken = new StringBuilder(10);
            int TokenType=0;
            int state = START;
            int FloatState=0;//浮点数的状态转换
            int FloatFlag=0;//记录浮点数的形式，E或.
            boolean save = true;
            while (state != DONE) {
                int Ch = 0;
                if (!unget) {
                    Ch = input.read();
                } else {
                    Ch = beforeCh;
                    unget = false;
                }
                switch (state) {
                    case START:
                        if (Character.isLetter(Ch)) {
                            currentToken.append((char) Ch);
                            state = INID;
                        } else if (Character.isDigit(Ch)) {
                            currentToken.append((char)Ch);
                            state = INNUM;
                        }else if (Ch == ':') {//赋值
                            currentToken.append((char)Ch);
                            state = INASSIGN;
                        } else if (Ch == '/') {
                            currentToken.append((char)Ch);
                            state = INCOMMENT;//包括了除法的情况
                        } else if (Ch == '<' || Ch == '>' || Ch == '=' || Ch == '!') {
                            currentToken.append((char)Ch);
                            state = INCOMP;
                        } else if (Ch == 32 || Ch == 13||Ch==10) {
                            state = START;//读入空格或回车忽略
                        } else {
                            state = DONE;
                            switch (Ch) {
                                case -1:
                                    EOF = true;
                                    break;//同EOF
                                case '+':
                                    currentToken.append('+');
                                    TokenType=Token.Op;
                                    break;
                                case '-':
                                    currentToken.append('-');
                                    TokenType=Token.Op;
                                    break;
                                case '*':
                                    currentToken.append('*');
                                    TokenType=Token.Op;
                                    break;
                                case '(':
                                    currentToken.append('(');
                                    TokenType=Token.Bound;
                                    break;
                                case ')':
                                    currentToken.append(')');
                                    TokenType=Token.Bound;
                                    break;
                                case '[':
                                    currentToken.append('[');
                                    TokenType=Token.Bound;
                                    break;
                                case ']':
                                    currentToken.append(']');
                                    TokenType=Token.Bound;
                                    break;
                                case '{':
                                    currentToken.append('{');
                                    TokenType=Token.Bound;
                                    break;
                                case '}':
                                    currentToken.append('}');
                                    TokenType=Token.Bound;
                                    break;
                                case ';':
                                    currentToken.append(';');
                                    TokenType=Token.Bound;
                                    break;
                                case ',':
                                    currentToken.append(',');
                                    TokenType=Token.Bound;
                                    break;
                                default:
                                    state = ERROR;
                                    break;
                            }
                        }
                        break;
                    case INID:
                        if (Character.isLetter(Ch) || Character.isDigit(Ch)) {
                            currentToken.append((char) Ch);
                            state = INID;
                        } else {
                            unget = true;
                            beforeCh = Ch;
                            TokenType=Token.ID;
                            state = DONE;//字符回退
                        }
                        break;
                    case INNUM:
                        if (Character.isDigit(Ch)) {
                            currentToken.append((char)Ch);
                            state = INNUM;
                        } else if (Character.isLetter(Ch)) {
                            state = ERROR;
                        }else if(Ch=='.'||Ch=='e'||Ch=='E'){
                            if(Ch=='.'){
                                FloatFlag=0;
                            }else{
                                FloatFlag=1;
                            }
                            currentToken.append((char)Ch);
                            state=INFLOAT;
                        } else {
                            unget = true;
                            beforeCh = Ch;
                            TokenType=Token.Num;
                            state = DONE;//字符回退
                        }
                        break;
                    case INFLOAT:
                        if(FloatFlag==0){
                            switch (FloatState){
                                case 0:
                                    if(Character.isDigit(Ch)){
                                        FloatState=1;
                                        currentToken.append((char)Ch);
                                    }else{
                                        unget = true;
                                        beforeCh = Ch;
                                        state=ERROR;
                                    }
                                    break;
                                case 1:
                                    if(Character.isDigit(Ch)){
                                        FloatState=1;
                                        currentToken.append((char)Ch);
                                    }else if(Ch=='e'||Ch=='E'){
                                        FloatState=2;
                                        currentToken.append((char)Ch);
                                    }else{
                                        unget = true;
                                        beforeCh = Ch;
                                        TokenType=Token.Num;
                                        state = DONE;//字符回退
                                    }
                                    break;
                                case 2:
                                    if(Ch=='+'||Ch=='-'){
                                        FloatState=3;
                                        currentToken.append((char)Ch);
                                    }else if(Character.isDigit(Ch)){
                                        FloatState=4;
                                        currentToken.append((char)Ch);
                                    }else{
                                        unget = true;
                                        beforeCh = Ch;
                                        state=ERROR;
                                    }
                                    break;
                                case 3:
                                    if(Character.isDigit(Ch)){
                                        FloatState=4;
                                        currentToken.append((char)Ch);
                                    }else {
                                        unget = true;
                                        beforeCh = Ch;
                                        state=ERROR;
                                    }
                                    break;
                                case 4:
                                    if(Character.isDigit(Ch)){
                                        FloatState=4;
                                        currentToken.append((char)Ch);
                                    }else{
                                        unget = true;
                                        beforeCh = Ch;
                                        TokenType=Token.Num;
                                        state = DONE;//字符回退
                                    }
                                    break;
                            }
                        }else if(FloatFlag==1){
                            switch (FloatState){
                                case 0:
                                    if(Ch=='+'||Ch=='-'){
                                        FloatState=1;
                                        currentToken.append((char)Ch);
                                    }else if(Character.isDigit(Ch)){
                                        FloatState=2;
                                        currentToken.append((char)Ch);
                                    }else{
                                        unget = true;
                                        beforeCh = Ch;
                                        state=ERROR;
                                    }
                                    break;
                                case 1:
                                    if(Character.isDigit(Ch)){
                                        FloatState=2;
                                        currentToken.append((char)Ch);
                                    }else {
                                        unget = true;
                                        beforeCh = Ch;
                                        state=ERROR;
                                    }
                                    break;
                                case 2:
                                    if(Character.isDigit(Ch)){
                                        FloatState=2;
                                        currentToken.append((char)Ch);
                                    }else{
                                        unget = true;
                                        beforeCh = Ch;
                                        TokenType=Token.Num;
                                        state = DONE;//字符回退
                                    }
                                    break;
                            }
                        }
                        break;
                    case INASSIGN:
                        if (Ch == '=') {
                            currentToken.append(Ch);
                            TokenType=Token.Op;
                            state = DONE;
                        } else {
                            unget = true;
                            beforeCh = Ch;
                            state = ERROR;
                        }
                        break;
                    case INCOMMENT:
                        int sub_state = 0;
                        while (state != DONE) {
                            switch (sub_state) {
                                case 0:
                                    if (Ch == '*') {
                                        sub_state = 1;
                                        currentToken.delete(0, 1);//确定为注释，将之前放入的'/'删除
                                        Ch = input.read();
                                    } else {
                                        unget = true;
                                        beforeCh = Ch;
                                        state = DONE;
                                    }
                                    break;
                                case 1:
                                    if (Ch == '*') {
                                        sub_state = 2;
                                        Ch = input.read();
                                    } else {
                                        sub_state = 1;
                                        Ch = input.read();
                                    }
                                    break;
                                case 2:
                                    if (Ch == '/') {
                                        state = DONE;
                                    } else if (Ch == '*') {
                                        sub_state = 2;
                                        Ch = input.read();
                                    } else {
                                        sub_state = 1;
                                        Ch = input.read();
                                    }
                                    break;
                            }
                        }
                        break;
                    case INCOMP:
                        if (Ch == '=') {
                            currentToken.append('=');
                            TokenType=Token.Op;
                            state = DONE;
                        } else {
                            unget = true;
                            beforeCh = Ch;
                            TokenType=Token.Op;
                            state = DONE;
                        }
                        break;
                    case ERROR:
                        save = false;
                        currentToken.delete(0, currentToken.length());
                        currentToken.append("ERROR!");
                        break;
                    case DONE:
                        save = true;
                        break;
                }
            }
            if (save == true&&currentToken.length()!=0) {
                if(reserved.contains(currentToken.toString())){
                    TokenType=Token.Reserved;
                }
                newToken=new Token(TokenType,currentToken);
            }
        }else{
            hasNextToken=false;
        }
        return newToken;
    }

    public boolean HasNextToken(){
        return hasNextToken;
    }
}

