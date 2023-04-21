package com.project.filechecker.Repository;


import com.project.filechecker.Entity.FileScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileScan, String> {

}