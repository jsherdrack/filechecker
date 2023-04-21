package com.project.filechecker.Runner;


import com.project.filechecker.Entity.FileScan;
import com.project.filechecker.Repository.FileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.project.filechecker.Function.FileLooper.fileLooper;
import static java.lang.System.exit;


@Component
public class DBOperationRunner implements CommandLineRunner {

    @Autowired
    FileRepository frep;



    @Override
        public void run(String... args) throws Exception {

        //file path of the folders you're running through
    //    String pathing = "/dir";
        String pathing = "H:\\_MLC Project Data\\MLC Project Data";

        //set variable to file looping results
        List<File> filelist1 =fileLooper(pathing);

        //set pattern for dates
        LocalDate myDateObj = LocalDate.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");

      //  LocalDate formattedDate = myDateObj.format(myFormatObj);

        String access="", mod="", change="", cr="";


        //Loops through the file looping results to obtain information about files
        for (File fileData : filelist1) {


            BasicFileAttributes attr = Files.readAttributes(Path.of(fileData.getAbsolutePath()), BasicFileAttributes.class);
            FileTime ft = attr.creationTime();
            Instant ins = ft.toInstant();
            LocalDateTime ldt = LocalDateTime.ofInstant(ins, ZoneId.systemDefault());
            LocalDate ld = ldt.toLocalDate();


            System.out.println(fileData.getName());
            System.out.println(fileData.getAbsolutePath());
            //   System.out.println(ld);

            //Save absolute file path, filename, created date,file created date, extension and filepath
            frep.saveAll(Arrays.asList(
                    new FileScan(fileData.getAbsolutePath(), fileData.getName(), myDateObj, ld, getExtension(fileData.getName()), FilenameUtils.getPath(String.valueOf(fileData)))));
        }

        System.out.println(filelist1);

        System.out.println("----------All Data saved into Database----------------------");


        System.out.println("---------------------Returning SQL Query----------------------");


        /**
         * A JDBC SELECT SQL connection to connect to the local database to process multiple SQL queries and save them
         * to a variable
         */
        try {
            String url = "jdbc:mariadb://lysithea:3306/myfiledb";
            Connection conn = DriverManager.getConnection(url, "jerome", "StreamHotelGraze");
            Statement stmt = conn.createStatement();
            ResultSet rs;
            ResultSet rs1;
            ResultSet rs2;
            ResultSet rs3;
            ResultSet rs4;
            ResultSet rs5;

            rs = stmt.executeQuery(

                    "(SELECT file_extension,COUNT(*) as counting FROM file_scan WHERE\n" +
                          "file_created_date >= DATE_ADD(CURDATE(), INTERVAL -7 DAY)\n" +
                            "AND report_date = CURDATE());"
            );


            Integer count = null;
            while (rs.next()) {
                count = rs.getInt("counting");
            }

            rs1 = stmt.executeQuery(

                    "(SELECT file_extension,COUNT(*) as counting FROM file_scan   WHERE\n" +
                                   "file_created_date >= DATE_ADD(CURDATE(), INTERVAL -7 DAY) AND " +
                            "file_extension LIKE '%arf%'" +
                            "AND report_date = CURDATE());"
            );

            Integer arfCount = 0;
            while (rs1.next()) {
                arfCount = rs1.getInt("counting");
            }

            rs2 = stmt.executeQuery(

                    "(SELECT file_extension,COUNT(*) as counting FROM file_scan   WHERE\n" +
                                   "file_created_date >= DATE_ADD(CURDATE(), INTERVAL -7 DAY) AND " +
                            "file_extension LIKE '%dcm%'"+
                            "AND report_date = CURDATE());"
            );

            Integer dcmCount = 0;
            while (rs2.next()) {
                dcmCount = rs2.getInt("counting");
            }

            rs3 = stmt.executeQuery(

                    "(SELECT file_extension,COUNT(*) as counting FROM file_scan   WHERE\n" +
                                   "file_created_date >= DATE_ADD(CURDATE(), INTERVAL -7 DAY) AND " +
                            "file_extension LIKE '%adicht%'"+
                            "AND report_date = CURDATE());"
            );

            Integer adichtCount = 0;
            while (rs3.next()) {
                adichtCount = rs3.getInt("counting");
            }

            rs4 = stmt.executeQuery(

                    "(SELECT file_extension,COUNT(*) as counting FROM file_scan   WHERE\n" +
                                   "file_created_date >= DATE_ADD(CURDATE(), INTERVAL -7 DAY) AND " +
                            "file_extension LIKE '%bmp%'"+
                            "AND report_date = CURDATE());"
            );

            Integer bmpCount = 0;
            while (rs4.next()) {
                bmpCount = rs4.getInt("counting");

            }

            rs5 = stmt.executeQuery(

                    "(SELECT * FROM file_scan  WHERE " +
                                   "file_created_date >= DATE_ADD(CURDATE(), INTERVAL -7 DAY)" +
                            "AND report_date = CURDATE()" +
                            "ORDER BY file_full_path ASC);"

            );

            String allFilesString = null;
            List allFilesList = new ArrayList<>();
            while (rs5.next()) {
                allFilesString = rs5.getString("file_full_path");
                allFilesList.add("\n"+ allFilesString  );
                System.out.println(allFilesString);
            }

            //removes content of text file
            PrintWriter pw = new PrintWriter("Report.txt");
            pw.close();

            /**
             * Creates a txt file and input the below text mixed with variable results
             */
            List data = new ArrayList();
            data.add("Report created: " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss"))) +
                    "\n" + "---------------------------------------------------------------------------------");
            data.add("Summary: ");
            data.add("Found a total of " + count + " files\n");
            data.add("Found: " + adichtCount + " files for ECG");
            data.add("Found: " + dcmCount + " files for DCIM");
            data.add("Found: " + bmpCount + " files for BMP");
            data.add("Found: " + arfCount + " files for ABR");
            data.add("\nFile locations: ");
            data.add(allFilesList+"");


            writeToFile(data, "Report.txt");
            conn.close();
            //Lets you know if there is an error and highlights the issue
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

//        String from = USER_NAME;
//        String pass = PASSWORD;
//        String[] to =  RECIPIENT; // list of recipient email addresses
//        String subject = "Weekly report "+ (LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
//        //read created text file
//        readFile();
//        String body = mailString.toString();
//        //Send email with declared variable
//        sendFromOutlook(from, pass, to, subject, body);



        //close spring boot session
        exit(0);
        }

    /**
     * Function to change FileTime type file to String
     */
    public static String formatDateTime(FileTime fileTime) {

        LocalDateTime localDateTime = fileTime
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return localDateTime.format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    /**
     * Function to write data to a file
     */
    private static void writeToFile(java.util.List list, String path) {
        BufferedWriter out = null;
        try {
            File file = new File(path);
            out = new BufferedWriter(new FileWriter(file, true));
            for (Object s : list) {
                out.write((String) s);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
        }
    }

    /**
     * Function to receive extension of a filename.
     */
    public static String getExtension(String filename){
        return FilenameUtils.getExtension(filename);
    }

}

