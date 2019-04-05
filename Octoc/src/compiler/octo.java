package compiler;import generator.Inbuilt;import java.util.ArrayList;public class octo{private static Inbuilt sys = new Inbuilt();public static String hello(String name,String surname){return (name+surname);}public static void main(String args[]){ArrayList<Object> a = sys.array("[53,\\hello,world\\,\\Bananas\\,33]");
if(5>3){
int p = 31+35;
sys.print (a.get(2));
if(3==1){
sys.print ("it works!");
}
}

for(int i:sys.prog(1,20)){
sys.print(i);
}

sys.print(hello("Srihari ","Unnikrishnan"));
}}