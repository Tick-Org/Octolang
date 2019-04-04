package generator;
import org.json.JSONArray;
import java.text.MessageFormat;
import generator.Inbuilt;

public class SyntaxDefs {
    public String FunctionDef(String Type, String Name, String Parameters){
        Object[] code = new Object[]{Type,Name,Parameters};

        return MessageFormat.format("\\u public {0} {1} {2} {",code);
    }

    public String VariableInit(String VariableName,String VariableType, String VariableValue){

        StringBuilder code = new StringBuilder();
        if(!VariableType.equals("arr")) {

            code.append(VariableType + " ");
            code.append(VariableName + " = ");
            code.append(VariableValue);
        }
        else{

            code.append("ArrayList<Object>" + " ");
            code.append(VariableName + " = ");
            code.append("sys.array("+"\""+VariableValue.replaceAll("\"","\\\\\\\\")+"\""+")");
        }
        return code.toString();
    }
    public String ConditionInit(String Condition){
        Object code = new Object[]{Condition};
        return MessageFormat.format("if({0}){",code);
    }

}
