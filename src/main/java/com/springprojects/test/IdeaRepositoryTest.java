package com.springprojects.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.springprojects.config.persistence.MySqlConfig;
import com.springprojects.entity.Idea;
import com.springprojects.entity.Tag;
import com.springprojects.repository.IdeaRepository;
import com.springprojects.repository.TagRepository;
import com.springprojects.service.IdeaService;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MySqlConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
public class IdeaRepositoryTest {

	@Autowired
	private IdeaRepository ideaRepository;
	@Autowired
	private TagRepository tagRepository;
	
	@Test
	public void findAll() {
		assertThat(ideaRepository.findAll()).asList();
	}
	
	@Test
	public void delete() {
		assertThat(ideaRepository.findOne(1L)).isExactlyInstanceOf(Idea.class);
	}
	
	@Test
	public void saveIdea() {
		Idea idea = new Idea();
		idea.setAuthorEmail("teamg5.bit@gmail.com");
		idea.setIdeaId(1L);
		idea.setIdeaTitle("Idea 1");
		idea.setIdeaBody("Idea 1 Body");
		Tag tag = new Tag();
		tag.setTagId(1L);
		tag.setTagName("Computer Lab");
		tag.setOpeningDate(new Timestamp(System.currentTimeMillis()));
		tag.setClosingDate(new Timestamp(System.currentTimeMillis()));
		tag.setFinalClosingDate(new Timestamp(System.currentTimeMillis()));
		tagRepository.save(tag);
		idea.setTag(tag);
		ideaRepository.save(idea);
		assertThat(ideaRepository.findOne(1L)).isExactlyInstanceOf(Idea.class);
	}
	
}
