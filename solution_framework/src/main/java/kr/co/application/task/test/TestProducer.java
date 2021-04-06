package kr.co.application.task.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.application.repo.dto.EmployeeHRmDTO;
import kr.co.application.repo.mapper.EmployeeHRmMapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class TestProducer implements Runnable  {
	
	
	private boolean isRun = false;
	private @Autowired EmployeeHRmMapper employeeHrmDao;
	
    public TestProducer() {
		// TODO Auto-generated constructor stub
    	try {
    		log.info("Init TestProducer");
    	}catch(Exception e) {
    		log.error("error", e);
    	}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		isRun = true;
		
		while(isRun) {
			EmployeeHRmDTO dto = new EmployeeHRmDTO();
			dto.EmployeeNo = "20085734";
			
			List<EmployeeHRmDTO> empList = employeeHrmDao.selectEmployeeHRList(dto);
			
			if(empList == null || empList.size() == 0) {
				log.info("emplist size cnt 0");
			}else {
				for(EmployeeHRmDTO emp : empList) {
					log.info("EmployeeNo = " + emp.EmployeeNo);
				}
			}
			
			try {Thread.sleep(10000);} catch (InterruptedException e) {e.printStackTrace();}
			
			//isRun = false;
		}
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void stop() {
		isRun = false;
	}

}
