package functions;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;


public class getToken {
    public static final int START=-1;
    public static final int DONE=0;
    public static final int ERROR=1;
    public static final int INNUM=2;
    public static final int INID=3;
    public static final int INASSIGN=4;
    public static final int INCOMMENT=5;
    public static final int INCOMP=6;
    public static final int ID=7;
    public static final int NUM=8;
    public static final List<String> reserved=new ArrayList<String>();

    public getToken(){
        reserved.add("int");
        reserved.add("float");
        reserved.add("long");
        reserved.add("if");
        reserved.add("else");
        reserved.add("return");
        reserved.add("void");
    }

    public static void readToken(String fileName) throws IOException {
        FileReader input = new FileReader(fileName);
        boolean EOF=false;
        boolean unget = false;
        int beforeCh = 0;
        while(!EOF) {
            StringBuilder currentToken = new StringBuilder(10);
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
                        } else if (Ch == ':') {//赋值
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
                                    break;
                                case '-':
                                    currentToken.append('-');
                                    break;
                                case '*':
                                    currentToken.append('*');
                                    break;
                                case '(':
                                    currentToken.append('(');
                                    break;
                                case ')':
                                    currentToken.append(')');
                                    break;
                                case '[':
                                    currentToken.append('[');
                                    break;
                                case ']':
                                    currentToken.append(']');
                                    break;
                                case '{':
                                    currentToken.append('{');
                                    break;
                                case '}':
                                    currentToken.append('}');
                                    break;
                                case ';':
                                    currentToken.append(';');
                                    break;
                                case ',':
                                    currentToken.append(',');
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
                            TokenType=ID;
                            state = DONE;//字符回退
                        }
                        break;
                    case INNUM:
                        if (Character.isDigit(Ch)) {
                            currentToken.append((char)Ch);
                            state = INNUM;
                        } else if (Character.isLetter(Ch)) {
                            state = ERROR;
                        } else {
                            unget = true;
                            beforeCh = Ch;
                            TokenType=NUM;
                            state = DONE;//字符回退
                        }
                        break;
                    case INASSIGN:
                        if (Ch == '=') {
                            currentToken.append(Ch);
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
                            state = DONE;
                        } else {
                            unget = true;
                            beforeCh = Ch;
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
                if (TokenType == ID) {
                    if(reserved.contains(currentToken.toString())){
                        System.out.println(currentToken);
                    }else{
                        System.out.println("id,"+currentToken);
                    }
                }else if(TokenType==NUM){
                    System.out.println("num,"+currentToken);
                }else{
                    System.out.println(currentToken);
                }
            }
        }
    }
}