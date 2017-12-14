package reports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import reports.domain.AppUser;
import reports.domain.Report;
import reports.repository.AppUserRepository;
import reports.repository.NoFile;
import reports.repository.ReportRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public List<NoFile> findAllReports(){
        return reportRepository.findAllByOrderByIdAsc();
    }

    public List<NoFile> findAllUserReports(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        AppUser owner = appUserRepository.findOneByUsername(loggedUsername);

        return reportRepository.findByOwner(owner);
    }

    public ResponseEntity<Report> deleteReport(@PathVariable Long id) {
        Report report = reportRepository.findOne(id);
        if (report == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            reportRepository.delete(report);
            return new ResponseEntity<>(report, HttpStatus.OK);
        }

    }

    public void downloadLoggedUserFile(Long id, HttpServletResponse response) throws IOException {
        Report report = reportRepository.findOne(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();

        if(report.getOwner().getUsername().equals(loggedUsername)){
            downloadFile(id, response);
        }else{
            throw new RuntimeException("To nie twoje sprawozdanie!");
        }
    }

    public void downloadFile (Long id, HttpServletResponse response)
            throws IOException {

        Report report = reportRepository.findOne(id);

        File someFile = new File(report.getName()+report.getExtension());
        FileOutputStream fos = new FileOutputStream(someFile);
        fos.write(report.getFile());
        fos.flush();
        fos.close();

        FileInputStream inputStream = new FileInputStream(someFile);

        String fileName = URLEncoder.encode(someFile.getName(), "UTF-8");
        fileName = URLDecoder.decode(fileName, "ISO8859_1");
        response.setContentType("application/x-msdownload");


        String headerValue = String.format("attachment; filename=\"%s\"", fileName);
        response.setHeader("Content-Disposition", headerValue);

        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        // reads parts in loop, when next we giving to output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outStream.close();
        someFile.delete();
    }

    public void uploadFile(byte[] file) throws IOException {
        if(file==null)throw new RuntimeException("Nie dodałeś pliku!");
        else if(file.length>104857600)throw new RuntimeException("Za duży plik!");
        Report report = new Report();
        report.setFile(file);
        reportRepository.save(report);
    }

    public void uploadDetails(Report report) throws IOException {
        Report reportTmp = reportRepository.findTop1ByOrderByIdDesc();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = auth.getName();
        AppUser loggedUser = appUserRepository.findOneByUsername(loggedUsername);

        LocalDateTime DateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dateTime = DateTime.format(formatter);

        reportTmp.setDescription(report.getDescription());
        reportTmp.setName(report.getName());
        reportTmp.setExtension(report.getExtension());
        reportTmp.setGrade(report.getGrade());
        reportTmp.setOwner(loggedUser);
        reportTmp.setDatetime(dateTime);

        reportRepository.save(reportTmp);

    }

    public void uploadGrade(Report report) throws IOException {
        Report reportTmp = reportRepository.getOne(report.getId());
        reportTmp.setGrade(report.getGrade());
        reportRepository.save(reportTmp);
    }
}
