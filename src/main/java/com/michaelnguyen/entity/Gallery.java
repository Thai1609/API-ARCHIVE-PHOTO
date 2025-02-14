package com.michaelnguyen.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "gallery")
public class Gallery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nameImage;

	private String urlImage;

	private String status;
	
	private String description;

	@ManyToOne
	@JsonBackReference
	@OnDelete(action = OnDeleteAction.CASCADE)  // ✅ Xóa tag -> tự động xóa gallery
	private Tag tag;

	@ManyToOne
	@JsonBackReference
	private User user;
}
