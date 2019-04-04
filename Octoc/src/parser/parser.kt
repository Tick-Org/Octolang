package parser

import org.json.*
//TODO: Perform Secret Hymn
import java.io.File
import java.io.InputStream
import java.lang.IndexOutOfBoundsException
/*
This is awful code.
I know it
This is the AST parser.

Variables in Octolang
int a = 42;int b = 34;string c = '23'
Functions in Octolang
fun int MeaningOfLife (int a ,int b) {
  return 42
}
This transpiles to:
'VariableInit':{
 "type":"Integer",
 "Name":"a",
 "Value":"42"}
'VariableInit':{
 "type":"Integer",
 "Name":"b",
 "Value":"34"}
'VariableInit':{
 "type":"String",
 "Name":"c",
 "Value":"'23'"
 }
*/
class Parser(code:String) {
    var code = code
    fun paramFinder(tokenList:List<String>):String{
      var param:String = ""

      var StringLiteral = false
      var paramBool = false
      for(token in tokenList){
        if(token.contains('{') && !StringLiteral){
          paramBool=false
        }
        if(token.contains('(')||paramBool){
          paramBool = true
          param += token + ' '
        }

        if(token.contains("'")){
            StringLiteral = !StringLiteral
          }
      }
      return param
    }
    fun arrayWorked(tokenList:List<String>):String{
      var arrBool = false
      var contents = ""
      var StringLiteral = false
      for(token in tokenList){

        if(token.contains("]") && !StringLiteral){
          arrBool = false
        }
        if(token.contains('[')||arrBool){
          arrBool = true
          contents += "$token "

        }
        if(token.contains("\"")){
            StringLiteral = !StringLiteral
        }
      }

      return contents

    }
    fun StringFinder(tokenList:List<String>):String{
      var string:String = ""
      var inside = false

      for(token in tokenList){
        if(token.contains("'") || inside){
          string+=token+" "
          inside = true
        }
        else if(token.contains("'") && inside){
          inside = false
        }
      }

      return string
    }
    fun parse():List<String>{
        val SplitCode:List<String>  = code.split(";")
        val TokenList = mutableListOf<String>()
        for( token in SplitCode){
            var VariableName:String=""
            var FuncName:String=""
            var Value:String=""
            var tokens = token.trim().split(" ").filter{!it.equals("")}.map{it.trim()}
            val contx =  tokens[0] //The Variable that would be changed
            try{VariableName = tokens[1]}catch(exp:IndexOutOfBoundsException){} // Type for functions, params for loops
            try{FuncName = tokens[2]}catch(exp:IndexOutOfBoundsException ){} // for functions. else its gonna return a '='
            try{Value = tokens[3]}catch(exp:IndexOutOfBoundsException){} // for params in functions
            when(contx){
              //TokenList.add returns a boolean
              "int" -> {TokenList.add("VariableInit:int");TokenList.add("$VariableName:$Value")}
              "String" -> {TokenList.add("VariableInit:string");Value =StringFinder(tokens);TokenList.add("$VariableName:$Value")}
              "bool" -> {TokenList.add("VariableInit:bool");TokenList.add("$VariableName:$Value")}
              "for" -> {TokenList.add("LoopInit:for");VariableName = paramFinder(tokens);TokenList.add("Parameters: $VariableName")}
              "if" -> {TokenList.add("Conditioning:if");VariableName = paramFinder(tokens);TokenList.add("Conditions: $VariableName")}
              "#" -> {TokenList.add("Comment:SLC")}
              "##" -> {TokenList.add("Comment:MLC")}
              "else" -> {TokenList.add("Conditioning:else");}
              "while" -> {TokenList.add("LoopInit:while");VariableName = paramFinder(tokens);TokenList.add("Parameters:$VariableName")}
              "arr" -> {TokenList.add("VariableInit:arr");Value=arrayWorked(tokens);TokenList.add("$VariableName:$Value")}
              "}" -> {TokenList.add("CodeBlock:End");}
              "fun" -> {TokenList.add("FunctionInit:$VariableName");TokenList.add("$FuncName");Value = paramFinder(tokens);TokenList.add("$Value")}
              else -> {
                  var equalSign:String = ""
                  try{equalSign =tokens[1]}catch(exp:IndexOutOfBoundsException){}
                  if(equalSign.contains("=")){
                    TokenList.add("Expression:Assignment")
                    var assigining = tokens[0]
                    var to = tokens[2]
                    TokenList.add("assiging:$assigining")
                    TokenList.add("to:$to")

                  }
                  else{
                    TokenList.add("Expression:Function")
                    var expression = tokens.joinToString(" ")
                    TokenList.add("Call:$expression")
                  }
                }
             }
          }
          return TokenList
    }
    //Creates the AST
    fun CreateParser(TokenList:List<String>):JSONObject{
        var ParentJSON:JSONObject = JSONObject()
        var Body:JSONArray = JSONArray()


        TokenList.forEachIndexed{ index,element ->
        var elements = element.split(":")
            if(elements[0]=="Expression"){
          if(elements[1]=="Assignment"){
              val assigning = TokenList[index+1].split(":")[1]
              val to = TokenList[index+2].split(":")[1]
              var ExpressionJSON:JSONObject = JSONObject()

              ExpressionJSON.put("Identifier","Expression")
              ExpressionJSON.put("type","assignment")
              ExpressionJSON.put("assigning","$assigning")
              ExpressionJSON.put("to","$to")
              Body.put(ExpressionJSON)

          }
          else{
            val to = TokenList[index+1].split(":")[1]
              var ExpressionJSON:JSONObject = JSONObject()

              ExpressionJSON.put( "Identifier","Expression")
              ExpressionJSON.put( "type","call")
              ExpressionJSON.put( "doing","$to")
              Body.put(ExpressionJSON)

          }
        }
        if(elements[0]=="CodeBlock"){
            var CodeBlockJSON:JSONObject = JSONObject()

            CodeBlockJSON.put( "Identifier","CodeBlock")
            Body.put(CodeBlockJSON)
        }
        if(elements[0] == "VariableInit"){

          val VarType  = TokenList[index].split(":")[1]
          val VariableName = TokenList[index+1].split(":")[0]
          val VariableValue = TokenList[index+1].split(":")[1].trim()
          var VariableJSON:JSONObject = JSONObject()
          VariableJSON.put( "Identifier","VariableInit")
          VariableJSON.put( "type","$VarType")
          VariableJSON.put( "Name","$VariableName")
          VariableJSON.put( "Value","$VariableValue" )
          Body.put(VariableJSON)

        }
        if (elements[0] == "FunctionInit"){
            val type = element.split(":")[1]
            val funcName = TokenList[index+1]
            var parameters = TokenList[index+2]
            var FunctionJSON = JSONObject()
            FunctionJSON.put ("Identifier","FunctionInit")
            FunctionJSON.put ("type","$type")
            FunctionJSON.put ("name","$funcName")
            FunctionJSON.put ("parameters","$parameters")
            Body.put(FunctionJSON)

        }
        if(elements[0] == "LoopInit"){
          var type = element.split(":")[1]
          val params = paramFinder(TokenList[index+1].split(":"))
          var LoopJSON = JSONObject()

          LoopJSON.put("Identifier","Loop")
          LoopJSON.put("type", "$type")
          LoopJSON.put("parameters","$params")
          Body.put(LoopJSON)

        }
        if(elements[0]=="Conditioning" && elements[1]=="if"){

          var type = element.split(":")[1]
          val params = paramFinder(TokenList[index+1].split(":"))
          var ConditionJSON = JSONObject()
          ConditionJSON.put("Identifier","Condition")
          ConditionJSON.put("type","$type")
          ConditionJSON.put("parameters","$params")
          Body.put(ConditionJSON)

        }
      if(elements[0]=="Conditioning" && elements[1]=="else" ){
          var ConditionJSON = JSONObject()
          ConditionJSON.put("Identifier","Condition")
          ConditionJSON.put( "type","else" )
          Body.put(ConditionJSON)
      }
      if (elements[0]=="Comment"){
        var SLC = JSONObject()
        var type = elements[1]
        //SLC -> Single Line Comment
        SLC.put("Identifier","Comment")
        SLC.put("type","$type")
        Body.put(SLC)

      }

    }
        ParentJSON.put("Body",Body)
        return ParentJSON
    }
}