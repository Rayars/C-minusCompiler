package functions;

import java.util.ArrayList;
import java.util.List;

public class sTreeNode {
    private ExpKind kind;//节点类型
    private OPType operator;//运算符类型
    private int val;//常量值
    private StringBuilder name;//ID
    public List<sTreeNode> Children;

    public enum ExpKind{
        operator,Num,Identifier,expression,expression_stmt,IfStmt,Program,declaration,
        fun_declaration,var_declaration,local_declaration,var,delimiter,type_spec,params,param_list,param,
        compound_stmt,statement_list,statement,reserved,iteration_stmt,return_stmt,simple_expression,
        additive_expression,term,call,args,arg_list
    }

    public enum OPType{
        plus("+"),
        minus("-"),
        mult("*"),
        div("/"),
        EQ("=="),
        NE("!="),
        GT(">"),
        LT("<"),
        GE(">="),
        LE("<="),
        assign("=");

        private String OP;
        OPType(String Op){
            this.OP=Op;
        }

        public String getOP(){
            return OP;
        }
    }

    public sTreeNode(){
        this.Children=new ArrayList<sTreeNode>();
    }

    public void setKind(ExpKind kind){
        this.kind=kind;
    }

    public void setOperator(OPType op){
        this.operator=op;
    }

    public void setVal(int val){
        this.val=val;
    }

    public void setName(StringBuilder n){this.name=n;}

    public ExpKind getKind(){
        return this.kind;
    }

    public OPType getOperator(){
        return operator;
    }

    public int getVal(){
        return val;
    }

    public StringBuilder getName(){
        return name;
    }
}
