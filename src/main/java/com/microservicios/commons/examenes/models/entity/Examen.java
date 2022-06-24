package com.microservicios.commons.examenes.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "examenes")
public class Examen {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty
	@Size(min = 4,max = 20)
	private String nombre;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at")
	private Date createAt;
	// un examen varias preguntas, 1 a mucho y cascade, para q se eliminen ambos ,
	// persista en relacion
	// orphanRemoval para que quede huerfana la pregunta, id null, y eliminada
	// automanticamente
	// mapeado por el nombre del atributo de la contraparte
	@JsonIgnoreProperties(value = { "examen" }, allowSetters = true)
	@OneToMany(mappedBy = "examen", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pregunta> preguntas;
	
	//Muchos examenes pueden estar asociados a una sola asignatura
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private Asignatura asignatura;
	@Transient
	private boolean respondido;

	public boolean isRespondido() {
		return respondido;
	}

	public void setRespondido(boolean respondido) {
		this.respondido = respondido;
	}

	public Examen() {
		this.preguntas = new ArrayList<Pregunta>();

	}

	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}

	public List<Pregunta> getPreguntas() {
		return preguntas;
	}
//para que asigne por cada pregunta el examen , revisar metodo addpregunta , contruir relacion inversa
	// pregunta-examen y e-p

	public void setPreguntas(List<Pregunta> preguntas) {
		// resetear las preguntas
		this.preguntas.clear();
		// lo que se recibe se pasa por argumento, seria lo mismo que:
		// (p->{this.addPregunta(p)})
		preguntas.forEach(this::addPregunta);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public void addPregunta(Pregunta pregunta) {
		this.preguntas.add(pregunta);
		pregunta.setExamen(this);
	}

	public void removePregunta(Pregunta pregunta) {
		this.preguntas.remove(pregunta);
		pregunta.setExamen(null);

	}
	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Examen)) {
			return false;
		}
		Examen a = (Examen) obj;

		return this.id != null && this.id.equals(a.getId());

	}

}

