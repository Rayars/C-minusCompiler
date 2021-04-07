package functions;

import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackInputStream;


public class getToken {
    public static final int START=-1;
    public static final int DONE=0;
    public static final int ERROR=1;
    public static final int INNUM=2;
    public static final int INID=3;
    public static final int INASSIGN=4;
    public static final int INCOMMENT=5;

    public static StringBuilder readToken() throws IOException {
        FileReader input = new FileReader("C:\\Users\\Rayars\\Desktop\\C-minusCompiler\\src\\test.txt");
        int state = START;
        boolean unget = false;
        int beforeCh = 0;
        boolean save = true;
        StringBuilder currentToken = new StringBuilder(10);
        while (state != DONE) {
            int Ch = 0;
            if (!unget) {
                Ch=input.read();
            } else {
                Ch = beforeCh;
            }
            unget = false;
            switch (state) {
                case START:
                    if (Character.isLetter(Ch)) {
                        state = INID;
                    } else if (Character.isDigit(Ch)) {
                        state = INNUM;
                    } else if (Ch == ':') {//赋值
                        state = INASSIGN;
                    } else {
                        state = DONE;
                        switch (Ch) {
                            case -1:
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
                            case '/':
                                currentToken.append('/');
                                break;
                            default:
                                currentToken.append("error");
                                state = ERROR;
                                break;
                        }
                    }
                case INID:
                    if (Character.isLetter(Ch) || Character.isDigit(Ch)) {
                        currentToken.append((char)Ch);
                        state = INID;
                    } else {
                        unget = true;
                        beforeCh = Ch;
                        state = DONE;//字符回退
                    }
                case INNUM:
                    if(Character.isDigit(Ch)){
                        currentToken.append((char)Ch);
                        state=INNUM;
                    }else if(Character.isLetter(Ch)){
                        state=ERROR;
                    }else{
                        unget = true;
                        beforeCh = Ch;
                        state = DONE;//字符回退
                    }
                case INASSIGN:

                case INCOMMENT:

                case ERROR:
                    save=false;
                    currentToken.delete(0,currentToken.length());
                case DONE:
            }
        }
        return currentToken;
    }
}