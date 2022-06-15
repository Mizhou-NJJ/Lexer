package zhou.lex;

import java.awt.event.KeyEvent;
import java.net.IDN;
import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    private int rowNumber;
    private int pointer;
    private String text;
    private char [] ctext;
    private long tlen;
    public HashMap<String, Integer> kw;//关键字
    public final static char BLK = ' ';
    public final static char TAB = '\t';
    public final static char EOL='\0';
    public final static char NL = '\n';
    public final static char WNL = '\r';
    private ArrayList<Token> tks;

    public Lexer(String text){
        this.text=text;
        this.tlen = text.length();
        this.ctext = text.toCharArray();
        this.tks = new ArrayList<>();
        kw = new HashMap<>();
        kw.put("int",0);
        kw.put("return",1);
        kw.put("while",2);
        kw.put("if",3);
        kw.put("else",4);
        kw.put("elif",5);
        kw.put("double",6);
        kw.put("float",7);
        kw.put("long",8);
        kw.put("void",9);
        kw.put("boolean",10);
        kw.put("for",11);
        kw.put("char",12);

    }
    public void start(){
        char c;
        while (pointer<tlen){
            c = ctext[pointer];
            if (c==BLK||c==TAB||c=='"') pointer++;
            else if (c==NL||c==WNL){
                rowNumber++;
                pointer++;
            }else{
                lexPart(c);
            }
        }

    }
    private void lexPart(char c){
        if (isDigit(c)){
            // 如果第一个字母是数字，
            String tk = splOne();
            if (isNumber(tk)){
                tks.add(new Token(tk,Token.CNS,"常数"));
            }else{// 非法标识符
                try {
                    throw new TokenException(rowNumber,"“"+tk+"“是非法标识符");
                } catch (TokenException e) {
                    e.printStackTrace();
                }
            }
        }else if (isAlpha(c)){
            /*
             *  第一个是字母 则可能是 标识符，关键字
             * */
            String tk = splOne();
            if (isIdentify(tk)){
                if (isKeyword(tk)){
                    tks.add(new Token(tk,Token.KW,Token.C_KW));
                }else{ // 标识符
                    tks.add(new Token(tk,Token.IDS,Token.C_IDS));
                }
            }else{
                try {
                    throw new TokenException(rowNumber,"“"+tk+"”是非法的标识符!");
                } catch (TokenException e) {
                    e.printStackTrace();
                }
            }

        }else{
           switch (c){
               case '[':
                   tks.add(new Token("[",Token.ALP,Token.C_RP));
                   break;
               case ']':
                   tks.add(new Token("]",Token.ARP,Token.C_RP));
                   break;
               case ',':
                   tks.add(new Token(",",Token.SPL,Token.C_SPL));
                   break;
               case ';':
                   tks.add(new Token(";",Token.SEMI,Token.C_CEMI));
                   break;
               case '(':
                   tks.add(new Token("(",Token.SLP,Token.C_LP));
                   break;
               case ')':
                   tks.add(new Token(")",Token.SRP,Token.C_LP));
                   break;
               case '{':
                   tks.add(new Token("{",Token.LP,Token.C_LP));
                   break;
               case '}':
                   tks.add(new Token("}",Token.RP,Token.C_LP));
                   break;
               case '+':
                   /*
                   *  可能是 += ++ +
                   * */
                   if (pointer+1<tlen){
                       if (ctext[pointer+1]=='+'){ // ++
                           tks.add(new Token("++",Token.INC,Token.C_OPER));
                           pointer++;
                       }else if (ctext[pointer+1]=='='){//+=
                           tks.add(new Token("+=",Token.INCEQ,Token.C_ASSIGN));
                           pointer++;
                       }else {
                           tks.add(new Token("+",Token.PLUS,Token.C_OPER));
                       }
                   }
                   break;
               case '-':
                   /*
                   *  可能是 -= ,--,-
                   * */
                   if (pointer+1<tlen){
                       if (ctext[pointer+1]=='-'){ // ++
                           tks.add(new Token("--",Token.DEC,Token.C_OPER));
                           pointer++;
                       }else if (ctext[pointer+1]=='='){//+=
                           tks.add(new Token("-=",Token.DECEQ,Token.C_OPER));
                           pointer++;
                       }else {
                           tks.add(new Token("-",Token.SUB,Token.C_OPER));
                       }
                   }else{
                       try {
                           throw  new  TokenException(rowNumber,"语法错误!");
                       } catch (TokenException e) {
                           e.printStackTrace();
                       }
                   }
                   break;
               case '!':
                   /*
                   *  可能是 ! ,!=
                   * */
                   if (pointer<tlen){
                       if (ctext[pointer+1]=='='){
                           tks.add(new Token("!=",Token.NE,Token.C_THAN));
                           pointer++;
                       }else {
                           tks.add(new Token("!",Token.NOT,Token.C_THAN));
                       }
                   }else {
                       try {
                           throw new TokenException(rowNumber,"语法错误!");
                       } catch (TokenException e) {
                           e.printStackTrace();
                       }
                   }
                   break;
               case '=':
                   /*
                   * 可能是 =,==
                   * */
                   if (pointer<tlen){
                       if (ctext[pointer+1]=='='){
                           tks.add(new Token("==",Token.EQ,Token.C_THAN));
                           pointer++;
                       }else{
                           tks.add(new Token("=",Token.ASSIGN,Token.C_ASSIGN));
                       }
                   }else {
                       try {
                           throw new TokenException(rowNumber,"语法错误!");
                       } catch (TokenException e) {
                           e.printStackTrace();
                       }
                   }
                   break;
               case '<':
                   if (pointer+1<tlen){
                       if (ctext[pointer+1]=='='){
                           tks.add(new Token("<=",Token.LESSEQ,Token.C_THAN));
                           pointer++;
                       }else {
                           tks.add(new Token("<",Token.LESSTHAN,Token.C_THAN));
                       }
                   }else try {
                       throw new TokenException(rowNumber,"语法错误!");
                   } catch (TokenException e) {
                       e.printStackTrace();
                   }
                   break;
               case '>':
                   if (pointer+1<tlen){
                       if (ctext[pointer+1]=='='){
                           tks.add(new Token(">=",Token.THANEQ,Token.C_THAN));
                           pointer++;
                       }else {
                           tks.add(new Token(">",Token.THAN,Token.C_THAN));
                       }
                   }else try {
                       throw new TokenException(rowNumber,"语法错误");
                   } catch (TokenException e) {
                       e.printStackTrace();
                   }
                   break;
               case '*':
                   /*
                   *  可能 *,*=,*IDS,**IDS,***IDS
                   *
                   * */
                   if (pointer+1<tlen){
                       if (ctext[pointer+1]=='='){
                           tks.add(new Token("*=",Token.MULEQ,Token.C_ASSIGN));
                           pointer++;
                       }else {
                           tks.add(new Token("*",Token.MUL,Token.C_OPER));
                       }
                   }else try {
                       throw new TokenException(rowNumber,"语法错误!");
                   } catch (TokenException e) {
                       e.printStackTrace();
                   }
                   break;
               case '/':
                   /*
                   *  可能是 /,//,/=，
                   *  多行注释 以/*开头以 start start/结尾
                   * */
                   if (pointer+1<tlen){
                       if (ctext[pointer+1]=='='){
                           tks.add(new Token("/=",Token.DIVEQ,Token.C_ASSIGN));
                           pointer++;
                       }else if (ctext[pointer+1]=='/'){
                           while (pointer<tlen&&(ctext[pointer]!=NL&&ctext[pointer]!=WNL)) pointer++;
                           rowNumber++;
                       } else if (ctext[pointer+1]=='*'){ // 多行注释
                           while (pointer<tlen){
                               if (ctext[pointer]==NL||ctext[pointer]==WNL) rowNumber++;
                              if (ctext[pointer]=='/'&&ctext[pointer-1]=='*'&&ctext[pointer-2]=='*'){
                                  break;
                              }
                              pointer++;
                           }
                       } else{
                           tks.add(new Token("/",Token.DIV,Token.C_OPER));
                       }
                   }else try {
                       throw new TokenException(rowNumber,"语法错误!");
                   } catch (TokenException e) {
                       e.printStackTrace();
                   }
                   break;
               case '%':
                   if (pointer+1<tlen){
                       if (ctext[pointer+1]=='='){
                           tks.add(new Token("%=",Token.MODEQ,Token.C_ASSIGN));
                           pointer++;
                       }else {
                           tks.add(new Token("%",Token.MOD,Token.C_OPER));
                       }
                   }else try {
                       throw new TokenException(rowNumber,"语法错误!");
                   } catch (TokenException e) {
                       e.printStackTrace();
                   }
           }
           pointer++;
        }
    }
    public String splOne(){
        StringBuilder b= new StringBuilder();
        while (pointer<tlen){
            if (isAlpha(ctext[pointer])||isDigit(ctext[pointer])){
                b.append(ctext[pointer]);
                pointer++;
            }else return b.toString();
        }
        return b.toString();
    }
    public boolean isNumber(String t) {
        char ct[] = t.toCharArray();
        char c = ct[0];
        if (isDigit(c)){
            if (c=='0'){
                if (t.length()==1) return true;
                /*
                 *  可能是 0b 0x
                 * */
                if (ct[1]=='x'){ // 16进制前缀
                    if (isHexNumber(ct,2)){
                        return true;
                    }else {
                        try {
                            throw new TokenException(rowNumber,"“"+t+"”是非法的16进制数");
                        } catch (TokenException e) {
                            e.printStackTrace();
                        }
                    }
                }else if (ct[1]=='b'){//二进制前缀

                    if (isBinaryNumber(ct,2)){
                        return true;
                    }else try {
                        throw new TokenException(rowNumber,"“"+t+"”是非法的2进制数");
                    } catch (TokenException e) {
                        e.printStackTrace();
                    }
                }else{ //十进制
                    if (isDecimal(ct,1)){
                        return true;
                    }else try {
                        throw new TokenException(rowNumber,"“"+t+"”是非法的10进制数");
                    } catch (TokenException e) {
                        e.printStackTrace();
                    }
                }
            }else{ // 1-9
                if (isDecimal(ct,0)){
                    return true;
                }else try {
                    throw new TokenException(rowNumber,"“"+t+"”是非法的10进制数");
                } catch (TokenException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     *
     * @param ct 待分析字符数组 ex: {''}
     * @param p
     * @return
     */
    private boolean isHexNumber(char [] ct,int p){

        boolean f=false;
        for(int i=p;i<ct.length;i++){
            if (isDigit(ct[i])||(ct[i]>='a'&&ct[i]<='f')||(ct[i]>='A'&&ct[i]<='F')) f=true;
            else return false;
        }
        return f;
    }
    private boolean isBinaryNumber(char [] ct,int p){
        boolean f=false;
        for(int i=p;i<ct.length;i++){
            if (ct[i]=='1'||ct[i]=='0') f=true;
            else return false;
        }
        return f;
    }
    private boolean isDecimal(char [] ct,int p){
        boolean f=false;
        for(int i=p;i<ct.length;i++){
            if (isDigit(ct[i])) f=true;
            else return false;
        }
        return f;
    }
    private boolean isAlpha(char c){
        if ((c>='a'&&c<='z')||(c>='A'&&c<='Z')) return true;
        return false;
    }
    private boolean isDigit(char c){
        if (c>='0'&&c<='9') return  true;
        return false;
    }
    public boolean isIdentify(String tk){
        char [] ctk = tk.toCharArray();
        if (!isAlpha(ctk[0])&&ctk[0]!='_') return false;
        else if (tk.length()==1) return true;
        boolean f=false;
        for(int i=1;i<ctk.length;i++){
           if (isAlpha(ctk[i])||ctk[i]=='_'||isDigit(ctk[i])) f = true;
           else return false;
        }
        return f;
    }
    public boolean isKeyword(String tk){
        return kw.containsKey(tk);
    }

    public ArrayList<Token> getTks() {
        return tks;
    }
}
