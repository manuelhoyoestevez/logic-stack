package com.mhe.dev;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.Lexer;
import com.mhe.dev.compiler.lib.core.LexerImpl;
import com.mhe.dev.compiler.lib.core.MheLexicalCategory;
import com.mhe.dev.compiler.lib.core.MheLogger;
import com.mhe.dev.compiler.lib.core.StreamImpl;
import com.mhe.dev.compiler.lib.core.Token;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, CompilerException
    {
        //Path path1 = Paths.get("C:\\Users\\manuel.hoyo\\panama\\workspace\\te-api-abis\\src\\main\\java\\com\\iecisa\\cipta\\te\\api\\application\\AbisApiConfiguration.java");
        //Path path2 = Paths.get("C:\\Users\\manuel.hoyo\\panama\\workspace\\api-abis.war.src\\src\\main\\java\\com\\iecisa\\cipta\\te\\api\\application\\AbisApiConfiguration.java");
        //areFilesEqual(path1.toFile(), path2.toFile());


        Path dir1 = Paths.get("C:\\Users\\manuel.hoyo\\panama\\workspace\\api-abis.war.code\\WEB-INF\\classes\\com");
        Path dir2 = Paths.get("C:\\Users\\manuel.hoyo\\panama\\workspace\\api-abis.war.prod\\WEB-INF\\classes\\com");
        areDirectoriesEqual(dir1, dir2);
    }

    public static void areDirectoriesEqual(Path dir1, Path dir2) throws IOException, CompilerException
    {
        //System.out.println("Directories: " + dir1.toUri() + " / " + dir2.toUri());

        for (Path dir1Element : Files.list(dir1).collect(Collectors.toList()))
        {
            //System.out.println(" -> Element: " + dir1Element.toUri());

            Path relative = dir1.relativize(dir1Element);
            Path dir2Element = dir2.resolve(relative);

            if (Files.isDirectory(dir1Element))
            {
                if (!Files.isDirectory(dir2Element))
                {
                    System.out.println("" + dir2Element.toUri() + " IS NOT DIRECTORY");
                    continue;
                }

                areDirectoriesEqual(dir1Element, dir2Element);
            }

            if (Files.isRegularFile(dir1Element))
            {
                areFilesEqual2(dir1Element.toFile(), dir2Element.toFile());
            }
        }
    }

    public static boolean areFilesEqual2(File file1, File file2) throws IOException
    {
        //System.out.println(file1.getAbsolutePath() + " / " + file2.getAbsolutePath());

        BufferedReader fileReader1 = new BufferedReader(new FileReader(file1));
        BufferedReader fileReader2 = new BufferedReader(new FileReader(file2));

        String line1 = fileReader1.readLine();
        String line2 = fileReader2.readLine();

        int line = 0;

        while (line1 != null && line1.equals(line2))
        {
            line++;
            line1 = fileReader1.readLine();
            line2 = fileReader2.readLine();
        }

        if (line1 == null && line2 != null)
        {
            System.out.println("DISTINCT: File 1 " + file1.getName() + " end at line " + line + " and File 2 " +  file2.getName() + " not");
            return false;
        }

        if (line1 != null && line2 == null)
        {
            System.out.println("DISTINCT: File 1 " + file1.getName() + " doesn't end at line " + line + " and File 2 " +  file2.getName() + " does");
            return false;
        }

        if (line1 == null)
        {
            System.out.println("EQUAL:    "
                    + "File 1 " + file1.getName()
                    + " / "
                    + "File 2 " + file2.getName()
            );
            return true;
        }

        System.out.println("DISTINCT: line " + line + "\n -> File 1 " + file1.getName() + ": " + line1 + "\n -> File 2 " + file2.getName() + ": " + line2);
        return false;
    }

    public static boolean areFilesEqual(File file1, File file2) throws FileNotFoundException, CompilerException
    {
        //System.out.println(file1.getAbsolutePath() + " / " + file2.getAbsolutePath());

        Lexer<MheLexicalCategory> lexer1 = new LexerImpl(logger, new StreamImpl(logger, new BufferedReader(new FileReader(file1))));
        Lexer<MheLexicalCategory> lexer2 = new LexerImpl(logger, new StreamImpl(logger, new BufferedReader(new FileReader(file2))));

        Token<MheLexicalCategory> token1 = lexer1.getNextToken();
        Token<MheLexicalCategory> token2 = lexer2.getNextToken();

        while (tokenAreEqual(token1, token2)
                && token1.getCategory() != MheLexicalCategory.END
                && token2.getCategory() != MheLexicalCategory.END
        )
        {
            token1 = lexer1.getNextToken();
            token2 = lexer2.getNextToken();

            //System.out.println(" -> " + token1 + " \t/ " + token2);
        }

        if (!tokenAreEqual(token1, token2))
        {
            System.out.println("DISTINCT: "
                    + "File 1 " + file1.getName() + ": " + token1.toString()
                    + " / "
                    + "File 2 " + file2.getName() + ": " + token2.toString()
            );
            return false;
        }

        System.out.println("EQUAL:    "
                + "File 1 " + file1.getName()
                + " / "
                + "File 2 " + file2.getName()
        );
        return true;
    }

    private static boolean tokenAreEqual(Token<MheLexicalCategory> token1, Token<MheLexicalCategory> token2)
    {
        return token1.getCategory() == token2.getCategory() && token1.getLexeme().equals(token2.getLexeme());
    }

    private final static MheLogger logger = new MheLogger()
    {
        @Override
        public void stream(int col, int row, String message)
        {

        }

        @Override
        public void lexer(int col, int row, String message)
        {

        }

        @Override
        public void parser(int col, int row, String message)
        {

        }

        @Override
        public void semantic(int col, int row, String message)
        {

        }

        @Override
        public void warn(int col, int row, String message)
        {

        }

        @Override
        public void error(int col, int row, String message)
        {

        }
    };
}
