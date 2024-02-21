package com.cluster9.logDispatcherRestService.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.cluster9.logDispatcherRestService.controllers.LoggingController;
import com.clustercld.logsmanager.entities.LogParagraph;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
@Entity
public class WebLogParagraph implements Serializable {

	@Transient
	private Logger logger = LoggerFactory.getLogger(LoggingController.class);
	
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@GeneratedValue
	Long id;

	int index;

	@NotBlank(message ="The file name is required")
    String fileName;
    

    
	@NotBlank(message ="A title is required")
    String title = "no title";

	@Lob
	@Column(name = "full_line", length = 48000)
	String lines;

	@JsonFormat(pattern = "yyyy-mm-dd")
	Date createdDate;
	@JsonFormat(pattern = "yyyy-mm-dd")
	Date accessedDate;

	//@JoinColumn(name="tag",referencedColumnName="name", insertable=false, updatable=false)
	@ManyToOne(cascade = {CascadeType.PERSIST , CascadeType.MERGE}, fetch= FetchType.EAGER)
	private Tag tag;

// see https://howtodoinjava.com/hibernate/hibernate-jpa-cascade-types/
/*	@Column
	@NotBlank(message ="A tag is required")
    @Size(min=2, max=15, message = "Please use less than 15 characters and no spaces")
    String tag;*/

	public WebLogParagraph() {
		super();
		this.lines = "";
	}


	public WebLogParagraph(int index, String fileName, Tag tag, String title) {
		super();
		this.index = index;
		if(fileName == null)
			fileName = "no file name";
		this.fileName = fileName;
		if (tag.getClass() == Tag.class) {
			this.tag = tag;
		} else {
			throw new RuntimeException("no Tag in WebParag constructor");
		}
		this.lines = "";
		this.title = title;
		this.setTag(tag);
		
		
	}

	public WebLogParagraph(LogParagraph p) {
		super();
		this.index = p.getIndex();
		if(p.getFileName() == null)
			p.setFileName("no file name");
		else
			this.fileName = p.getFileName();
		// the Tag instance must exists here
		if (p.getTag() == null | p.getTag().isEmpty()) {
			this.tag = new Tag("notag");
		} else {
			this.tag = new Tag(p.getTag());
		}

		this.title = p.getTitle();
		this.lines = "";
		this.castLines(p);
		
		this.processCreatedDate(p);

	}

	private void processCreatedDate(LogParagraph p) {
		if(p.getFileName().isEmpty())
			this.setCreatedDate(new Calendar.Builder().setCalendarType("iso8601").setDate(2010, 1, 20).build().getTime());
		else {
//			System.out.println("test splitter: " + p.getFileName());
			String cldDate = "";
			if(p.getFileName().contains("claude")) {
//				System.out.println("claude found");
				cldDate = p.getFileName().split(".txt")[0].split("log_claude")[1];	
			}
			else if (p.getFileName().contains("it")) {
				cldDate = p.getFileName().split(".txt")[0].split("log_it")[1];
			}
			else {
				System.out.println("no matching filename to process");
			}
//			System.out.println("test split - created date: " + cldDate);
//			System.out.println("year = " + cldDate.substring(6,8));
			this.setCreatedDate(new Calendar.Builder().setCalendarType("iso8601")
					.setDate(Integer.valueOf(cldDate.substring(0,  4)), Integer.valueOf(cldDate.substring(4, 6))-1, Integer.valueOf(cldDate.substring(6, 8)))
					.build()
					.getTime());
		}
//		System.out.println("old log files processed this created date = " + this.getCreatedDate());
		this.setAccessedDate(this.getCreatedDate());	

	}

	private void castLines(LogParagraph p) {
		if (p.getLines().isEmpty()) {
//			System.out.println("check this : no lines in paragraph");
		} else {
			p.getLines().stream().forEachOrdered( line -> {
				if (!(line.isEmpty())) {
					this.lines = this.lines.concat(line).concat("\n");
					// System.out.println("WebLogParagraph" + this.title + "line written " + this.getLines().length()	+ "  line:" + line.length());
				} else {
					this.lines = this.lines.concat("\n");
				}

			
			});
		}
		
	}
	

	public Tag getTag() {
		return tag;
	}

	
	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getAccessedDate() {
		return accessedDate;
	}

	public void setAccessedDate(Date accessedDate) {
		this.accessedDate = accessedDate;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTagName(){
		return tag.getName();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLines() {
		return lines;
	}

	public void setLines(String lines) {
		this.lines = lines;
	}

	public String addLine(String line) {
		this.lines = this.lines.concat(line);
		return line;
	}

	@Override
	public String toString() {
		return "WebLogParagraph [index=" + index + ", fileName=" + fileName + ", tag=" + tag.getName() + ", title=" + title
				+ ", lines=" + lines + "]";
	}

}
