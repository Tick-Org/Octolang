package generator;

import java.util.*;

public class Inbuilt {

    public ArrayList array(String Values){

        ArrayList<String> tempSet = new ArrayList<String>();
        ArrayList<Object> set = new ArrayList<Object>();
        String all = Values.substring(1,Values.length()-1);
        String Val = "";
        boolean param = false;
        for (int i = 0; i < all.length(); i++) {
            if(!(all.charAt(i)==',')||param){
                Val+=all.charAt(i);
            }
            else{
                tempSet.add(Val);
                Val="";
            }
            if(all.charAt(i)=='\\'){

                param=!param;

            }
        }


        for(String val :tempSet){

            if(val.contains(".")){
                set.add(Float.parseFloat(val));
            }
            else {
                try {
                    set.add(Integer.parseInt(val));
                }
                catch (NumberFormatException e){
                    set.add(val.replaceAll("\\\\",Character.toString('"')));
                }
            }
        }

        return set;

    }

    public void print(Object msg){
        System.out.println(msg);
    }
    public List<Integer> prog(int  ...params){
        int step;
        List<Integer> set = new ArrayList<>();
        try {
            step = params[2];
        }
        catch(ArrayIndexOutOfBoundsException exp) {
            step = 1;
        }
        int start = params[0];
        int end = params[1];
        while(start<=end){
            set.add(start);
            start+=step;
        }
    return set;
    }

}
