package transformer;
import org.json.*;

import java.util.List;

public class transformer{
    private String paramFinder(String code){
        String param = "";
        Boolean inParam = false;

        for (int i=0;i <= code.length()-1;i++){
            char curChar = code.charAt(i);

            if(curChar == ')'){
                inParam = false;
                param += ')';

            }
            if(curChar == '('||inParam){

                param+=curChar;
                inParam = true;
            }
        }


        return param;
    }
    public JSONArray transformer(JSONObject object)throws Exception{
        String[] keys = JSONObject.getNames(object);
        JSONArray cTransformed = new JSONArray();
        StringBuilder code = new StringBuilder();

        for (String key : keys)
        {

            JSONArray value = (JSONArray) object.get(key);
            for (int i = 0; i < value.length(); i++) {

                JSONObject statement = (JSONObject) value.get(i);

                String id = statement.optString("Identifier");
                String type = statement.optString("type").trim();

                String param = statement.optString("parameters").trim();
                String Name = statement.optString("Name").trim();
                String funcName = statement.optString("name").trim();
                String Value = statement.optString("Value").trim();
                String doing = statement.optString("doing").trim();
                JSONObject transformedObj = new JSONObject();

                switch(id){

                    case "Loop":
                        transformedObj.put("type",type);

                        String sansBrack = param.substring(1, param.length() - 1);
                        String[] params = sansBrack.split(" ");

                        if(type.equals("for")) {

                            String iteratingVar = params[0];
                            String iterated = params[2];
                            transformedObj.put("iterator",iteratingVar);
                            transformedObj.put("iteratedSet",iterated);

                        }
                        else{
                            String condition =sansBrack;
                            transformedObj.put("parameters",condition);
                        }

                        break;
                    case "FunctionInit":
                        transformedObj.put("type",id);
                        String fSansBrack = param.substring(1, param.length() - 1);
                        String[] fParams  = fSansBrack.split(",");
                        transformedObj.put("name",funcName);
                        transformedObj.put("FuncType",type);
                        JSONArray paramList = new JSONArray();

                        for ( String iParam:fParams){
                            JSONObject paramListDev = new JSONObject();
                            String[] sub = iParam.trim().split(" ");
                            paramListDev.put("type",sub[0]);
                            paramListDev.put("val",sub[1]);
                            paramList.put(paramListDev);
                        }

                        transformedObj.put("parameters",paramList);
                        break;
                    case "VariableInit":
                        transformedObj.put("vartype",type);
                        transformedObj.put("type",id);
                        transformedObj.put("name",Name);
                        transformedObj.put("value",Value);
                        break;
                    case "CodeBlock":


                        transformedObj.put("type","status");
                        break;
                    case "Condition":
                        transformedObj.put("type",id);
                        transformedObj.put("id",type);
                        transformedObj.put("parameters", param);

                        break;
                    case "Expression":
                        transformedObj.put("type",type);
                        transformedObj.put("function",doing.split("\\(")[0]);

                        JSONArray eParams = new JSONArray();
                        eParams.put(paramFinder(doing).substring(1,paramFinder(doing).length()-1));
                        transformedObj.put("parameters",eParams);
                        break;

                }
                cTransformed.put(transformedObj);
            }

        }
        return cTransformed;
    }
}
