package com.workshop.service;

import java.util.List;
import java.util.UUID;

import javax.jdo.annotations.Transactional;
import javax.persistence.EntityManager;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.workshop.dao.EquipoDAO;
import com.workshop.teams.Equipo;
import com.workshop.util.PersistenceManager;

@Api(name = "teams", version = "v1", namespace = @ApiNamespace(ownerDomain = "service.workshop.com", ownerName = "service.workshop.com", packagePath = ""))

public class EquipoServiceAPI {

	private EntityManager em = PersistenceManager.getEntityManager();
	private EquipoDAO equipoService = new EquipoDAO(em);

	@ApiMethod(name = "add", path = "equipo", httpMethod = ApiMethod.HttpMethod.POST)
	public Equipo addEquipo(@Named("nombre") String nombre, @Named("anoFundacion") Integer anoFundacion,
			@Named("estrellas") Integer estrellas)
			throws NotFoundException, InternalServerErrorException, BadRequestException {

		String id = createHash();
		Equipo equipo = new Equipo(id, nombre, anoFundacion, estrellas);
		if (equipoService.getEquipoById(id) != null) {
			throw new BadRequestException("Equipo ya existe");
		}
		if (equipoService.addEquipo(equipo)) {
			return equipo;
		} else {
			throw new InternalServerErrorException("Equipo no se pudo agregar.");
		}
	}

	@ApiMethod(name = "update", path = "equipo", httpMethod = ApiMethod.HttpMethod.PUT)
	public Equipo updateEquipo(Equipo e) throws NotFoundException, InternalServerErrorException {
		if (equipoService.getEquipoById(e.getIdEquipo()) == null) {
			throw new NotFoundException("Equipo no existe.");
		}
		if (equipoService.updateEquipo(e)) {
			return e;
		} else {
			throw new InternalServerErrorException("Equipo no se pudo actualizar.");
		}
	}

	@Transactional
	@ApiMethod(name = "remove", path = "equipo/{id}", httpMethod = ApiMethod.HttpMethod.DELETE)
	public Equipo removeTeam(@Named("id") String id) throws NotFoundException {
		Equipo equipo = equipoService.removeEquipo(id);
		if (equipo != null) {
			return equipo;
		} else {
			throw new NotFoundException("Equipo no existe.");
		}
	}

	@ApiMethod(name = "getAll", path = "equipos", httpMethod = ApiMethod.HttpMethod.GET)
	public List<Equipo> getTeams() {

		List<Equipo> equipos = equipoService.getEquipos();

		return equipos;
	}

	@ApiMethod(name = "getById", path = "equipos/{id}", httpMethod = ApiMethod.HttpMethod.GET)
	public Equipo getEquipo(@Named("id") String id) throws NotFoundException {

		Equipo equipo = equipoService.getEquipoById(id);
		if (equipo != null) {
			return equipo;
		} else {
			throw new NotFoundException("Equipo no existe.");
		}
	}

	/**
	 * createHash
	 * 
	 * @return uuid hash
	 */
	private String createHash() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}