package com.cluster9.logDispatcherRestService.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.NaturalId;
import org.springframework.transaction.annotation.Transactional;


// create a one-to-many relation with the log entity
@SuppressWarnings("serial")
@Entity
public class Tag implements Serializable{
	
	//@Id @GeneratedValue
	//Long id;

	//@NotBlank @Column(nullable = false, unique = true)
	@Id
	@Column(unique = true, nullable = false, length = 20)
	String name;
	
	String comment;

//	@OneToMany(mappedBy = "tag")
//	private List<WebLogParagraph> logs = new ArrayList<WebLogParagraph>();//// only the set interface available
//	
//	for testing purposes only. It's not saved in the DB though. this is ok for primitives only
//	@ElementCollection 
//	@CollectionTable(name="log_set",joinColumns=@JoinColumn(name="tag_id"))
//	@Column(name="log_names")
//	private List<String> logNames; 
	
	public Tag() {
		super();
	}
	// load WebLogParagraf from log_XXX.txt files folder
	public Tag(String tagName){
		this.name = tagName;
	}

//	public Long getId() {
//		return id;
//	}

//	public void setId(Long id) {
//		this.id = id;
//	}

	
	@Override
	public String toString() {
		return "Tag: "+this.name+"\n  comment: "+this.comment+"\n";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}
	
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
             
        if (!(obj instanceof Tag))
            return false;

        return
            name.equals(((Tag) obj).getName());
    }

	// TODO this can be a problem with the Set collection hash
    @Override
    public int hashCode() {
        return 1;
    }
	

}
