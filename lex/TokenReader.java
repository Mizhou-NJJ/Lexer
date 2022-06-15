package zhou.lex;

import java.util.List;

public class TokenReader {
    private List<Token> tokens;
    private int index;
    private String tokenStirng;
    private TokenReader(){}
    public TokenReader(List<Token> tokens){
        this.tokens=tokens;
    }
    public Token next(){
        ++index;
        if (index>tokens.size()-1) return null;
        else {
            return tokens.get(index);
        }
    }
    public Token current(){
        if (index>tokens.size()-1) return null;
//        else return tokens.get(index);
        return tokens.get(index);
    }
    public void reRead(){
        this.index=0;
    }
    public String getTokenForString(){

        if (tokenStirng==null){
            StringBuilder b =new StringBuilder();
            for(Token t:tokens){
                b.append(t.value);
            }
            this.tokenStirng=b.toString();
        }
        return tokenStirng;
    }
}
