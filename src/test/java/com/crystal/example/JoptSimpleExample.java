package com.crystal.example;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * <p>JoptSimple是一个轻量级的Java命令行选项解析库，它可以帮助您轻松地在Java应用程序中处理命令行参数和选项</p>
 */
public class JoptSimpleExample {
    public static void main(String[] args) {
        // 新建选项解析器
        OptionParser parser = new OptionParser();

        // 提供定义了一个选项，这个选项是必需的，程序会根据输入的参数打印出相应的输入文件路径
        OptionSpec<String> inputFileOption = parser.accepts("input", "Input file")
                .withRequiredArg().describedAs("Path to input file");

        // 选项注入字符串数组里
        OptionSet options = parser.parse(args);

        // 获取输入文件路径
        String inputFilePath = options.valueOf(inputFileOption);
        System.out.println("Input File: " + inputFilePath);
    }
}
