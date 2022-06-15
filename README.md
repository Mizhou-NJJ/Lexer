# Lexer

Java实现的简单词法分析器

#### 使用方法

```java
Lexer lexer = new Lexer("(M+X)*(Y+(J*P+JK))+P");
//        System.out.println(lexer.isIdentify("intb"));
        lexer.start();
//        System.out.println(lexer.isIdentify("a"));
        ArrayList<Token> tks = lexer.getTks();
```

