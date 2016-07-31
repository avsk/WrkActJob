package com.as400.workjob;

/**
 * Created by root on 8/1/16.
 */


/*
 * Copyright © 2000-2016 fhdumay for i5tools.eu
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Revision information (Subversion):
 * ==================================
 *       $ID: $
 *      $URL: https://repository.fhdumay.eu/svn/com/ipsoft/A4C-commands-legacy-1.0.x-j6/trunk/src/main/java/com/ipsoft/iseries/A4C/commands/AS400CommunicatorException.java $
 *     $Date: 2015-05-17 17:39:19 +0200 (Sun, 17 May 2015) $
 *   $Author: fhdumay $
 * $Revision: 1853 $
 * ==================================
 */

        import com.ibm.as400.access.AS400Message;
        import com.ibm.as400.access.CommandCall;
        import static com.ibm.as400.access.MessageFile.NO_FORMATTING;
        import static com.ibm.as400.access.MessageFile.SUBSTITUTE_FORMATTING_CHARACTERS;
        import com.ibm.as400.access.ProgramCall;
        import com.ibm.as400.access.QSYSObjectPathName;

/**
 *
 * @author Fabien H. Dumay (mailto: fhdumay@fhdumay.eu)
 * @version 1.0.0
 */
public class AS400CommunicatorException extends Exception {

    /**
     * Creates a new instance of <code>AS400CommunicatorException</code> without detail message.
     *
     * @since 1.0.0
     */
    public AS400CommunicatorException() {
    }

    /**
     * Constructs an instance of <code>AS400CommunicatorException</code> with the specified detail message.
     *
     * @param msg the detail message.
     *
     * @since 1.0.0
     */
    public AS400CommunicatorException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>AS400CommunicatorException</code> with the specified detail message and cause.
     *
     * @param msg   the detail message.
     * @param cause The exception that caused this exception to be thrown.
     *
     * @since 1.0.0
     */
    public AS400CommunicatorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an instance of <code>AS400CommunicatorException</code> for the specified program call object.
     *
     * @param pc               The program call for which to construct the exception.
     * @param firstMessageOnly Signals that only the FIRST message retrieved from the program call parameter should be printed.
     * @param formatMessage    The substitution markers in the message will be replaced with the actual message data.
     *
     * @return instance of {@link #AS400CommunicatorException(java.lang.String)}
     *
     * @since 1.0.0
     */
    public static AS400CommunicatorException constructException(ProgramCall pc, boolean firstMessageOnly, boolean formatMessage) {
        QSYSObjectPathName program = new QSYSObjectPathName(pc.getProgram());

        return new AS400CommunicatorException("Failure calling "
                + program.getObjectType()
                + " "
                + program.getLibraryName()
                + "/" + program.getObjectName() + "\n"
                + getMessages(pc.getMessageList(), firstMessageOnly, formatMessage)
        );
    }

    /**
     * Constructs an instance of <code>AS400CommunicatorException</code> for the specified command call object.
     * @param cc               The command call for which to construct the exception.
     * @param firstMessageOnly Signals that only the FIRST message retrieved from the program call parameter should be printed.
     * @param formatMessage    The substitution markers in the message will be replaced with the actual message data.
     *
     * @return instance of {@link #AS400CommunicatorException(java.lang.String)}
     */
    public static AS400CommunicatorException constructException(CommandCall cc, boolean firstMessageOnly, boolean formatMessage) {
        return new AS400CommunicatorException("Failure running command '" + cc.getCommand() + "'.\n" + getMessages(cc.getMessageList(), firstMessageOnly, formatMessage));
    }

    /**
     * Format the message list.
     *
     * @param msgList          List of message(s) to format
     * @param firstMessageOnly Display the first message of the list only.
     * @param formatMessage    Format the message
     *
     * @return A string containing the formatted message.
     */
    @SuppressWarnings("UseSpecificCatch")
    public static String getMessages(AS400Message[] msgList, boolean firstMessageOnly, boolean formatMessage) {
        StringBuilder wrk = new StringBuilder("===================");
        int formatting = formatMessage ? SUBSTITUTE_FORMATTING_CHARACTERS : NO_FORMATTING;

        for (AS400Message msg : msgList) {
            try {
                msg.load(formatting);
                wrk.append("\n----------").append(msg.getID()).append(" : ").append(msg.getText()).append("\n").append(msg.getHelp()).append("\n");
                if (firstMessageOnly) {
                    break;
                }
            } catch (Exception ex) {
                // Ignore errors....
            }
        }
        return wrk.append("===================").toString();
    }
}

