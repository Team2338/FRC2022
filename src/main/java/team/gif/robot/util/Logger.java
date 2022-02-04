package team.gif.robot.util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import edu.wpi.first.wpilibj.Timer;





    /**
     * This class facilitates the logging of data to an external USB flash drive.
     * A new file, always called "latest.txt" is written to in the /logs/ folder of
     * the drive.
     *
     * It is referenced statically in the program and is a singleton.
     * It supplies three levels of logging importance:
     *    INFO: For routine robot data.
     *    WARN: For exceeded "soft limits" of operation
     *    SEVERE: For exceeded "hard limits" of operation
     */
    public class Logger{

        private static Logger instance = new Logger();



        private FileWriter writer;
        private int id = 100000;
        private boolean empty = true;
        private boolean flushed = true;

        private Logger(){
            try{
                // "/U" is the default directory for RoboRIO flash drives
                File logFile = new File("/U/logs/latest.txt");

                // If there is already a "latest" log file, rename it
                // to its id before creating a new one.
                if(logFile.exists()){
                    Scanner getName = new Scanner(logFile);
                    if(getName.hasNext()){
                        id = Integer.parseInt(getName.nextLine());
                        File saved = new File("/U/logs/" + id + ".txt");
                        logFile.renameTo(saved);
                    }

                    getName.close();
                }

                logFile.createNewFile();

                writer = new FileWriter(logFile);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public static void setInstance(Logger instance) {
            Logger.instance = instance;
        }

        /**
         * Logs a message to the current log file.
         * @param tag Message importance tag
         * @param message Message to be logged
         */
        private void log(String tag, String message){
            try{
                // Write the log id to the first line of the file.
                if(empty){
                    empty = false;
                    writer.write(++id + "\r\n");
                }

                // Use StringBuilder for efficiency.
                StringBuilder msgBuilder = new StringBuilder(64);
                msgBuilder.append("[")
                        .append(Timer.getFPGATimestamp())
                        .append("] [")
                        .append(tag)
                        .append("]: ")
                        .append(message)
                        .append("\r\n");

                writer.write(msgBuilder.toString());
                flushed = false;
            }catch(IOException e){
                e.printStackTrace();
            }catch(NullPointerException e){
                try{
                    // If the file does not exist (aka there is no USB drive),
                    // use the roboRIO's file system instead.
                    File logFile = new File("/home/lvuser/latest.txt");
                    logFile.createNewFile();

                    writer = new FileWriter(logFile);
                }catch(IOException e1){
                    e.printStackTrace();
                }
            }
        }

        /**
         * Log message at the INFO importance level.
         */
        public static void info(String message){
            if(message.isEmpty())
                return;
            instance.log("INFO", message);
        }

        /**
         * Log a message at the WARN importance level.
         */
        public static void warn(String message){
            if(message.isEmpty())
                return;
            instance.log("WARN", message);
        }

        /**
         * Log a message at the SEVERE importance level.
         */
        public static void severe(String message){
            if(message.isEmpty())
                return;
            instance.log("SEVERE", message);
        }

        /**
         * Flushes the FileWriter if it has been written to since last call.
         */
        public static void flush(){
            if(instance.flushed)
                return;

            try{
                instance.writer.flush();
                instance.flushed = true;
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }



