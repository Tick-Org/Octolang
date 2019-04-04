package compiler;
import generator.generator;
import parser.Parser;
import transformer.transformer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static String readFileAsString(String filePath)throws Exception
    {

        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
    public static  void main (String[] args)throws Exception{

        Parser parser = new Parser(readFileAsString(args[0]));
        transformer transforme = new transformer();

        generator gen = new generator(transforme.transformer(parser.CreateParser(parser.parse())));

    }
}
