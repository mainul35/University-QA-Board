package com.springprojects.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.springprojects.config.persistence.MySqlConfig;
import com.springprojects.entity.Authority;
import com.springprojects.repository.AuthorityRepository;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MySqlConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class AuthorityRepositoryTest {

	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Test
	public void findAll() {
		assertThat(authorityRepository.findAll()).asList();
	}
	
	@Test public void saveOrUpdateAuthority() {
		assertThat(authorityRepository.findByAuthority("ROLE_ADMIN")).isNull();
		authorityRepository.save(new Authority(System.nanoTime(), "ROLE_ADMIN"));
		assertThat(authorityRepository.findByAuthority("ROLE_ADMIN")).isExactlyInstanceOf(Authority.class);
	}
	
//	@Test public void delete() {
//		Authority authority = authorityRepository.findByAuthority("ROLE_ADMIN");
//		assertThat(authority).isExactlyInstanceOf(Authority.class);
//		authorityRepository.delete(authority);
//		assertThat(authorityRepository.findByAuthority("ROLE_ADMIN")).isNull();
//	}
}
