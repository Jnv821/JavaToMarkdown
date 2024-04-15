# Description

Java to markdown is a simple GUI tool to generate markdown documents with a preset style from java classes. JTM will autogenerate
a class diagram to use with mermaid.

## Important Notice

JavaToMarkdown is an internal tool developed for my use case. It's supposed to be used along Docusaurus and plug the output of JTM to the docusarus project. It is not an actively developed project. The implementation of the program is not the best and efficiency issues are to be expected. This was a learning project more than an actual production grade documentation generating app. It has very limited features and I won't develop it further as it is better to rewrite it from scratch with the knowledge I know have.

You are free to use this tool if you like it but don't expect updates or new features since they're not planned. Nevertheless there are some things that I might consider adding and they'll be described at the end of this document.

## Usage

To use the tool you must write comments between the functions, parameters and class delcarations you want to add to the markdown document in the Java class.

Supported comments:

- `//@StartOfMethod` & `//@EndOfMethod`
- `//@StartOfParameters` & `//@EndOfParameters`
- `//@StartOfClass` & `//@EndOfClass`

### Method Comments

```java
//@StartOfMethod
public void MyTestMethod(){

}
//@EndOfMethod
```

### Parameters Comments

Yes, parameters and fields are not the same. Oversight in my part when developing this tool. This will most likely be fixed if I get to change or add what I want. More at the end.

> [!CAUTION]
> Please have each parameter or field in one and only one line. JTM **DOES NOT** support declarations such as `int a, b = 0;`

```java
//@StartOfParameters
private int a = 0;
public int b = 1;
protected int c = 2
int d = 2;
//@EndOfParameters

```

### Class Comments

> [!WARNING]
> JTM most likely doesn't support internal classes but you're free to try it.

```java
//@StartOfClass
public class MyTestClass{
    // code here
}
//@EndOfClass
```

## Possible Additions

Apart from refactoring a lot of badly named code and really obfuscated usage of method chaining the next thing that is going to be added if it is added would be both annotation support and custom comments to delimit the areas of methods, fields, and classes.