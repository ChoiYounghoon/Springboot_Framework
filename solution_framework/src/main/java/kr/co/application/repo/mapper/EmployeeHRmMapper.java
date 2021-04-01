package kr.co.application.repo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.application.repo.dto.EmployeeHRmDTO;

@Mapper
@Repository
public interface EmployeeHRmMapper {
	public java.util.List<EmployeeHRmDTO> selectEmployeeHRList(EmployeeHRmDTO dto);
}
