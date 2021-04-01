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
        StringBuilder token=new StringBuilder(10);
        FileReader input=new FileReader("test.txt");
        int state=START;
        char Ch= (char) input.read();
        while(state!=DONE){
            switch(state){
                case START:
                    if(Character.isLetter(Ch)){
                        state=INID;
                    }else if(Character.isDigit(Ch)){
                        state=INNUM;
                    }else if(Ch==':'){

                    }else{
                        state=DONE;
                        switch (Ch){
                            case
                        }
                    }


                    try{
                        nextCh=(char)input.read();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    if(nextCh=='a'){//状态转移对应的字符是‘a’
                        token.append(nextCh);//nextCh收入token
                        state=2;
                    }else{
                        try {
                            input.unread(nextCh);//nextCh退回输入流
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        state=-1;
                    }
                    break;
            }
            if(state==0){
                return token;
            }else{
                //error
            }
        }
        token.append(1);
        return token;
    }
}
