package com.cluster9.logDispatcherRestService.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cluster9.logDispatcherRestService.entities.Tag;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {
	
	public Page<Tag> findByName(@Param("name") String name, Pageable pageable);
	
	public Tag findByName(@Param("name") String name);
	
	public List<Tag> findAll();
	
	public Page<Tag> findAll(Pageable pageable);

    public boolean existsByName(String tagName);
}
