package com.as400.workjob;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;

/**
 * Created by root on 6/2/16.
 */
public class WrkActJobCmd {

    public static void main(String...args ){
        ArgumentParser parser = ArgumentParsers
                .newArgumentParser("WrkActJobCmd")
                .defaultHelp(true)
                .description("List of active jobs");

        parser.addArgument("-v","--verbose")
                .help("Verbosity : 1 - basic \n\t\t 2 - advanced")
                .type(Integer.class)
                .setDefault(1);

        parser.addArgument("-s","--select")
        .help("specify job details")
        ;

    }


}
