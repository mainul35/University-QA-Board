package com.springprojects.test;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.springprojects.config.persistence.MySqlConfig;
import com.springprojects.entity.UserEntity;
import com.springprojects.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
 
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MySqlConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	
	@Test
    public void findByUsername() {
        assertThat(userRepository.findByUsername("mainul35")).isExactlyInstanceOf(UserEntity.class);
    }
	
	@Test
    public void findByEmail() {
        assertThat(userRepository.findByEmail("mainuls18@gmail.com")).isExactlyInstanceOf(UserEntity.class);
    }
	
	@Test
    public void findAll() {
        assertThat(userRepository.findAll()).asList();
    }
	
	@Test
    public void findByRole() {
        assertThat(userRepository.findByAuthority("ROLE_QA_MANAGER")).asList();
    }
}