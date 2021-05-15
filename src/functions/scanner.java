package functions;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class scanner {//创建对象，代表文件的字符流
    private final int START=-1,DONE=0,ERROR=1,INNUM=2,INID=3,INASSIGN=4,INCOMMENT=5,INCOMP=6;
    private final List<String> reserved=new ArrayList<String>();//保留字列表
    private FileReader input;//文件字符流
    private boolean hasNextToken=true;
    boolean EOF=false;
    boolean unget = false;
    int beforeCh = 0;

    public scanner(String filePath) throws IOException{
        reserved.add("int");
        reserved.add("if");
        reserved.add("else");
        reserved.add("return");
        reserved.add("void");
        reserved.add("switch");
        reserved.add("case");
        input = new FileReader(filePath);
    }

    public Token readToken() throws IOException {//返回读取的token
        StringBuilder currentToken = new StringBuilder(10);
        Token newToken=new Token();
        if(!EOF) {
            currentToken = new StringBuilder(10);
            int TokenType=0;
            int state = START;
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
                        } else if (Ch == 32|| Ch == 13||Ch==10) {
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
                        }else if (Character.isLetter(Ch)) {
                            state = ERROR;
                        }else {
                            unget = true;
                            beforeCh = Ch;
                            TokenType=Token.Num;
                            state = DONE;//字符回退
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
                        System.out.println("ERROR");
                        System.exit(1);
                        break;
                    case DONE:
                        save = true;
                        break;
                }
            }
            if (save&&currentToken.length()!=0) {
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

