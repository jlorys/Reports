package reports.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import reports.domain.Report;
import reports.repository.NoFile;
import reports.service.ReportService;


@RestController
@RequestMapping(value = "/api")
public class ReportRestController {

	@Autowired
	private ReportService reportService;

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
	public List<NoFile> reports() {
		return reportService.findAllReports();
	}
	
	@RequestMapping(value = "/userreports", method = RequestMethod.GET)
	public List<NoFile> userReports() {
	    return reportService.findAllUserReports();
	}
	
	@RequestMapping(value = "/report/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Report> deleteReport(@PathVariable Long id) {
        return reportService.deleteReport(id);
    }
	
	@RequestMapping(value = "/report/file/{id}", method = RequestMethod.GET)
	public void downloadFile (@PathVariable Long id, HttpServletResponse response) throws IOException {
        reportService.downloadFile(id, response);
    }
	
	@RequestMapping(value = "/report/uploadfile", method = RequestMethod.POST)
	public void uploadFile(@RequestBody byte[] file) throws IOException {
        reportService.uploadFile(file);
	}
	
	@RequestMapping(value = "/report/uploaddetails", method = RequestMethod.PUT)
	public void uploadDetails(@RequestBody Report report) throws IOException {
        reportService.uploadDetails(report);
    }
	
	@RequestMapping(value = "/report/uploadgrade", method = RequestMethod.PUT)
	public void uploadGrade(@RequestBody Report report) throws IOException {
        reportService.uploadGrade(report);
    }

}