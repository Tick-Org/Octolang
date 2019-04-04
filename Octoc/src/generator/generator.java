package generator;
import java.io.*;
import java.lang.reflect.*;
import org.json.*;

/**
 * generator
 */
public class generator {
    private static SyntaxDefs defenition = new SyntaxDefs();
    BufferedWriter out = new BufferedWriter(new FileWriter("/bin/octo/Octoc/src/compiler/octo.java"));
    private static Inbuilt inbuiltdefs = new Inbuilt();
    public generator(JSONArray trAST)throws Exception{

        String boiler="package compiler;import generator.Inbuilt;import java.util.ArrayList;public class octo{private static Inbuilt sys = new Inbuilt();";
        String code="public static void main(String args[]){";
        boolean inFunc = false;
        boolean inCodeBlock = false;

        for(int i=0;i<=trAST.length()-1;i++) {
            JSONObject stat = (JSONObject) trAST.get(i);
            String type = stat.optString("type");

            String id = stat.optString("id");
            String vartype = stat.optString("vartype");
            String name = stat.optString("name");
            String value = stat.optString("value");
            String func = stat.optString("function");

            if (!inFunc) {
                switch (type) {
                    case "VariableInit":

                        code += defenition.VariableInit(name, vartype, value) + ";";
                        break;
                    case "call":
                        Class c = Inbuilt.class;
                        JSONArray params = stat.optJSONArray("parameters");
                        code += func + "(" + params.join(",").replaceAll("\"", "").replace("\\", "\"").trim() + ");";
                        break;
                    case "Condition":
                        //out of var ideas i am so sorry
                        String paramt = stat.optString("parameters");
                        code += id + paramt + "{";
                        break;
                    case "FunctionInit":
                        String funcName = stat.optString("name");
                        String funcType = stat.optString("FuncType");
                        JSONArray Params = stat.optJSONArray("parameters");
                        inFunc=true;
                        boiler += "public static " + funcType + " " + funcName + "(";
                        for (int j = 0; j < Params.length(); j++) {
                            JSONObject param = (JSONObject) Params.get(j);
                            String paramName = param.optString("val");
                            String paramType = param.optString("type");
                            boiler += paramType + " " + paramName;
                            if(j!=Params.length()-1){
                                boiler+= ",";
                            }
                        }

                        boiler += "){";
                        break;
                    case "for":
                        String iterator = stat.optString("iterator");
                        String iteratedSet = stat.optString("iteratedSet");

                        code += "for(int " + iterator + ":" + iteratedSet + "){";

                        break;
                    case "status":
                        code += "}";

                        break;
                }
                code += "\n";
            }
            else{
                switch (type) {
                    case "VariableInit":

                        boiler += defenition.VariableInit(name, vartype, value) + ";";
                        break;
                    case "call":
                        JSONArray params = stat.optJSONArray("parameters");
                        boiler += func + "(" + params.join(",").replaceAll("\"", "").replace("\\", "\"").trim() + ");";
                        break;
                    case "Condition":
                        //out of var ideas i am so sorry
                        String paramt = stat.optString("parameters");
                        boiler += id + paramt + "{";
                        inCodeBlock=true;
                        break;

                    case "for":
                        String iterator = stat.optString("iterator");
                        String iteratedSet = stat.optString("iteratedSet");
                        inCodeBlock=true;
                        boiler += "for(int " + iterator + ":" + iteratedSet + "){";

                        break;
                    case "status":
                        boiler += "}";
                        if(!inCodeBlock){
                            inFunc=false;
                        }
                        break;
                }
            }
        }
        code+="}}";
        out.write(boiler+code);
        out.close();
    }
}