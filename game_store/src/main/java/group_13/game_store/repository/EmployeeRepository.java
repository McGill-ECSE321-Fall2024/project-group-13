package group_13.game_store.repository;

import org.springframework.data.repository.CrudRepository;
import group_13.game_store.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, String> {
    public Employee findByUsername(String employeeUsername);
}
