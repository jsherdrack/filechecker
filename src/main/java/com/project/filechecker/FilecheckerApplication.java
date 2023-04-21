package com.project.filechecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilecheckerApplication {


	public static void main(String[] args) {

		/**
		 *  Creates a session that runs the FilecheckerApplication class. In short, this method is responsible for running through all the
		 *  code. ie, it creates the database table in mysql, sifts through the given directory to extract all
		 *  filenames that match with the given extension and then creates a text file with
		 *  the filenames that have been created in the last 7 days.
		 *  It then sends an email to the given recipients.
		 */
		SpringApplication.run(FilecheckerApplication.class, args);
	}



}

