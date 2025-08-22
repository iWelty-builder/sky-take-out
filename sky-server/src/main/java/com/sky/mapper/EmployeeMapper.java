package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     * @param employee
     */
    @Insert("insert into sky_take_out.employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void addemployee(Employee employee);

    /**
     * 分页查询员工数据
     *
     * @param employeePageQueryDTO@return
     */
    List<Employee> page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     *
     * @param employee
     */
    @Update("update employee set status=${status} where id=${id}")
    void startOrStop(Employee employee);
    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    /**
     * 修改员工信息
     * @param employee
     */
    @Update("update employee set name=#{name},username=#{username},phone=#{phone},sex=#{sex}," +
            "id_number=#{idNumber},status=#{status},update_time=#{updateTime}," +
            "update_user=#{updateUser} where id=#{id}")
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 修改员工密码
     * @param passwordEditDTO
     */
    @Update("update employee set password= #{newPassword} where id= #{empId}")
    void editPassword(PasswordEditDTO passwordEditDTO);
}
