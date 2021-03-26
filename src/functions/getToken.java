package functions;
import java.io.IOException;
import java.io.PushbackInputStream;

public class getToken {
    public static StringBuilder readToken(){
        StringBuilder token=new StringBuilder(10);
        PushbackInputStream input=new PushbackInputStream(System.in);
        int state=1;
        char nextCh='a';
        while(state!=0&&state!=-1){
            switch(state){
                case 1:
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
