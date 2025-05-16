package com.ajeet.backEndAPI.Entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data // provide getter and setter
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
	name = "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
	// here title is unique field
)
public class PostEntity {

	// create id as foreign key
	@Id
	@GeneratedValue(
	strategy = GenerationType.IDENTITY
	)
	private Long id;
	
	@Column(name = "title", nullable = false, length = 50)
	private String title;
	@Column(name = "content", nullable = false)
	private String content;
	@Column(name = "description", nullable = false)	
	private String description;
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content; 
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public String getDescription() {
		return description;
	}
	
	// mapping with comments
	// orphanremoval => remove the child when parent is removed.
	// cascade =  When we perform some action on the target entity, the same action will be applied to the associated entity.
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)	
	private Set<CommentEntity> comments = new HashSet<>();
	
}
