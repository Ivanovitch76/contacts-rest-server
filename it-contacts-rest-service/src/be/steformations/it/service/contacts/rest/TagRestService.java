package be.steformations.it.service.contacts.rest;

import be.steformations.it.java_data.contacts.dto.ContactDtoFactory;
import be.steformations.it.java_data.contacts.dto.TagDto;
import be.steformations.java_data.contacts.interfaces.beans.Tag;
import be.steformations.java_data.contacts.interfaces.dao.TagDao;

/*
 http://localhost:8080/contacts-rest/rs/tag
 */
@javax.ws.rs.Path("tag")
public class TagRestService {

	private TagDao tagDao;
	private ContactDtoFactory contactsDtoFactory;
		
	public TagRestService() {
		super();
		this.tagDao = ContactRestDaoFactory.getInstance().getTagDao();
		this.contactsDtoFactory = new ContactDtoFactory();
	}



	// GET http://localhost:8080/contacts-rest/rs/tag/1/value	
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{pk:[1-9]+}/value")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String getTagValueById(@javax.ws.rs.PathParam("pk") int id){
		Tag tag = this.tagDao.getTagById(id);
		if (tag == null){
			return "not found";
		}
		return tag.getValue();
	}
	
	
	// GET http://localhost:8080/contacts-rest/rs/tag/1	
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{pk:[1-9]+}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	public String getTagByValueAsText(@javax.ws.rs.PathParam("pk") int id){
		String xml = null;
		Tag tag = this.tagDao.getTagById(id);
		TagDto dto = new TagDto();
		if (tag != null){
			dto.setId(tag.getId());
			dto.setValue(tag.getValue());
		} 
		xml = this.contactsDtoFactory.tagToXml(dto);
		System.out.println(xml);
		return xml;
	}
	
	// POST http://localhost:8080/contacts-rest/rs/tag/{value}	
	@javax.ws.rs.POST
	@javax.ws.rs.Path("{value}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public TagDto createAndSaveTag(@javax.ws.rs.PathParam("value") String value){
		TagDto dto = new TagDto();
		Tag tag = this.tagDao.createAndSaveTag(value);
		if (tag != null){
			dto.setId(tag.getId());
			dto.setValue(tag.getValue());
		}
		
		return dto;
	}
	
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{value}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public javax.ws.rs.core.Response getTagByValue(@javax.ws.rs.PathParam("value") String value){
		javax.ws.rs.core.Response response = null;
		Tag tag = this.tagDao.getTagByValue(value);
		if (tag == null){
			response = javax.ws.rs.core.Response.status(404).build();
		} else {
			TagDto dto = new TagDto();
			dto.setId(tag.getId());
			dto.setValue(tag.getValue());
			response = javax.ws.rs.core.Response.ok(dto).build();
		}
		
		return response;
	}
	
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("delete/{id:[0-9]+}")
	public javax.ws.rs.core.Response removeTag(@javax.ws.rs.PathParam("id") int id){
		javax.ws.rs.core.Response response = null;
		Tag tag = this.tagDao.getTagById(id);
		if (tag != null){
			this.tagDao.removeTag(id);
			response = javax.ws.rs.core.Response.ok().build();
		} else {
			response = javax.ws.rs.core.Response.status(404).build();
		}
		
		return response;
	}
	
	// GET http://localhost:8080/contacts-rest/rs/tag
	@javax.ws.rs.GET
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public javax.ws.rs.core.Response getAllTags(){
		javax.ws.rs.core.Response response = null;
		
		java.util.List<? extends Tag> tags = this.tagDao.getAllTags();
		java.util.List<TagDto> dtos = new java.util.ArrayList<>();
		for (Tag tag : tags){
			TagDto dto = new TagDto();
			dto.setId(tag.getId());
			dto.setValue(tag.getValue());
			dtos.add(dto);
		}
		
		javax.ws.rs.core.GenericEntity<java.util.List<TagDto>> entity
			= new javax.ws.rs.core.GenericEntity<java.util.List<TagDto>>(dtos) {};
		response = javax.ws.rs.core.Response.ok(entity).build();
		
		return response;
	}
	
}
