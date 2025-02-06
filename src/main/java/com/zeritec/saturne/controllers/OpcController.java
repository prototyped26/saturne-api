package com.zeritec.saturne.controllers;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zeritec.saturne.models.AssetLine;
import com.zeritec.saturne.models.AssetType;
import com.zeritec.saturne.models.Fund;
import com.zeritec.saturne.models.InvestmentRule;
import com.zeritec.saturne.models.InvestmentRuleType;
import com.zeritec.saturne.models.Investor;
import com.zeritec.saturne.models.Opc;
import com.zeritec.saturne.models.Opcvm;
import com.zeritec.saturne.models.OpcvmType;
import com.zeritec.saturne.models.RequestResponse;
import com.zeritec.saturne.models.Week;
import com.zeritec.saturne.models.Year;
import com.zeritec.saturne.services.AssetLineService;
import com.zeritec.saturne.services.AssetTypeService;
import com.zeritec.saturne.services.FundService;
import com.zeritec.saturne.services.InvestmentRuleService;
import com.zeritec.saturne.services.InvestmentRuleTypeService;
import com.zeritec.saturne.services.InvestorService;
import com.zeritec.saturne.services.OpcService;
import com.zeritec.saturne.services.OpcvmService;
import com.zeritec.saturne.services.OpcvmTypeService;
import com.zeritec.saturne.services.WeekService;
import com.zeritec.saturne.services.YearService;

@RestController
@RequestMapping("/api/opc")
public class OpcController {
	
	@Autowired
	private OpcService service;
	
	@Autowired
	private OpcvmService opcvmService;
	@Autowired
	private OpcvmTypeService typeOpcvmService;
	
	@Autowired
	private InvestmentRuleTypeService investmentRuleTypeService;
	@Autowired
	private InvestmentRuleService investmentRuleService;
	
	@Autowired
	private AssetTypeService assetTypeService;
	@Autowired
	private AssetLineService assetLineService;
	
	@Autowired
	private InvestorService investorService;
	
	@Autowired
	private FundService fundService;
	@Autowired
	private WeekService weekService;
	@Autowired
	private YearService yearService;
	
	private Year activeYear;

	@GetMapping("/current-week")
	public ResponseEntity<RequestResponse> list() {
		RequestResponse response = new RequestResponse();
		
		Calendar calendar = Calendar.getInstance();  
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR); 
        
        try {
        	 Iterable<Opc> opcs = service.getByWeek(weekNumber);
        	 response.setData(opcs);
        	 
        	 return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
        	response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/load/week/{week}")
	public ResponseEntity<RequestResponse> loadReport(@RequestParam("file") MultipartFile file, @PathVariable("week") int weekId) throws IOException {
		RequestResponse response = new RequestResponse();
		
        Iterable<Year> actives = yearService.actives();
		for(Year y: actives) {
			activeYear = y;
		}
        
        try {
        	Week week;
        	Optional<Week> weekOpt = weekService.getById(weekId);
        	// Un week défini sinon prendre la semaine en cours de l'année active ... 
        	if (weekOpt.isPresent()) {
        		week = weekOpt.get();
        	} else {
        		week = weekService.currentWeekOfYear(activeYear.getId());
        	}
        	
        	Workbook workbook = new XSSFWorkbook(file.getInputStream());
        	// GET FUND 
        	Fund fund = new Fund();
        	String approvalNumber = workbook.getSheetAt(0).getRow(6).getCell(3).getStringCellValue();
        	approvalNumber = fundService.normalizeApprovalNumber(approvalNumber);
        	
        	Optional<Fund> fundOpt = fundService.getByApprovalNumber(approvalNumber);
        	// si le fond n'existe pas 
        	if (fundOpt.isEmpty()) {
        		workbook.close();
        		response.setMessage("Attention cette Action ne peut se poursuivre car le fond n'est pas reconnu !");
    			//return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        	} else {
        		fund = fundOpt.get();
        	}
        	
        	Opc opc = new Opc();
        	opc.setCreatedAt(new Date());
        	opc.setFund(fund);
        	opc.setWeek(week);
        	
        	Iterable<OpcvmType> opcvmTypes = typeOpcvmService.getByGroup();
        	Iterable<InvestmentRuleType> investmentRuleTypes = investmentRuleTypeService.getAll();
        	Iterable<AssetType> assetTypes = assetTypeService.getAll();
        	
        	List<Opcvm> opcvms = opcvmService.extractOpcvmFromSheet(workbook, opcvmTypes);
        	List<InvestmentRule> rules = investmentRuleService.extractFromSheet(workbook, investmentRuleTypes);
        	List<AssetLine> lines = assetLineService.extractFromSheet(workbook, assetTypes);
        	List<Investor> investors = investorService.extractFromSheet(workbook);
        	
        	lines.forEach(line -> line.setOpc(opc));
        	
        	response.setData(investors);
        	
    		workbook.close();
        	 
        	 return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
        	response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
        
        
	}
}
