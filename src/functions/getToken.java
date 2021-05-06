package functions;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;


public class getToken {
    public final int START=-1;
    public final int DONE=0;
    public final int ERROR=1;
    public final int INNUM=2;
    public final int INFLOAT=9;
    public final int INID=3;
    public final int INASSIGN=4;
    public final int INCOMMENT=5;
    public final int INCOMP=6;
    public final int ID=7;
    public final int NUM=8;
    public final List<String> reserved=new ArrayList<String>();
    private FileReader input;

    public getToken(String filePath) throws IOException{
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

    public StringBuilder readToken() throws IOException {
        StringBuilder currentToken = new StringBuilder(10);
        boolean EOF=false;
        boolean unget = false;
        int beforeCh = 0;
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
                            TokenType=NUM;
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
                                        state = DONE;//字符回退
                                    }
                                    break;
                            }
                        }else if(FloatFlag==1){
                            switch (FloatState){
                                case 0:
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
                                case 1:
                                    if(Character.isDigit(Ch)){
                                        FloatState=4;
                                        currentToken.append((char)Ch);
                                    }else {
                                        unget = true;
                                        beforeCh = Ch;
                                        state=ERROR;
                                    }
                                    break;
                                case 2:
                                    if(Character.isDigit(Ch)){
                                        FloatState=4;
                                        currentToken.append((char)Ch);
                                    }else{
                                        unget = true;
                                        beforeCh = Ch;
                                        state = DONE;//字符回退
                                    }
                                    break;
                            }
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
                        currentToken.append("\\reserved");
                    }else{
                        currentToken.append("\\id");
                    }
                }else if(TokenType==NUM){
                    currentToken.append("\\num");
                }
            }
        }
        return currentToken;
    }
}