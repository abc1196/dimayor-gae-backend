package com.workshop.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.google.api.server.spi.response.BadRequestException;
import com.workshop.teams.Equipo;

public class EquipoDAO {

	private EntityManager em;

	public EquipoDAO(EntityManager em) {
		this.em = em;
	}

	public boolean addEquipo(Equipo e) {
		// Check for already exists
		try {
			em.getTransaction().begin();
			em.persist(e);
			em.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean updateEquipo(Equipo e) {
		try {
			em.getTransaction().begin();
			em.merge(e);
			em.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public Equipo removeEquipo(String id) {
		Equipo equipo = null;
		try {
			equipo = em.find(Equipo.class, id);
			em.getTransaction().begin();
			em.remove(equipo);
			em.getTransaction().commit();
			return equipo;
		} catch (Exception e) {
			return equipo;
		}
	}

	public List<Equipo> getEquipos() {
		List<Equipo> equipos = null;
		TypedQuery<Equipo> q = em.createNamedQuery("Equipo.getAll", Equipo.class);
		try {
			equipos = q.getResultList();
		} catch (NoResultException e) {
			equipos = new ArrayList<Equipo>();
		}

		return equipos;
	}

	public Equipo getEquipoById(String id) {
		Equipo equipo = em.find(Equipo.class, id);
		return equipo;
	}

}
