package be.steformations.it.service.contacts.rest;

import javax.ws.rs.PathParam;

import be.steformations.it.java_data.contacts.dto.ContactDto;
import be.steformations.it.java_data.contacts.dto.CountryDto;
import be.steformations.it.java_data.contacts.dto.TagDto;
import be.steformations.java_data.contacts.interfaces.beans.Contact;
import be.steformations.java_data.contacts.interfaces.beans.Tag;
import be.steformations.java_data.contacts.interfaces.dao.ContactDao;

@javax.ws.rs.Path("contact")
public class ContactRestService {

	private ContactDao contactDao;
	
	public ContactRestService() {
		super();
		this.contactDao = ContactRestDaoFactory.getInstance().getContactDao();
	}
	
	// GET http://localhost:8080/contacts-rest/rs/contact/{id}
	@javax.ws.rs.GET
	@javax.ws.rs.Path("{id}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public javax.ws.rs.core.Response getContactById(@javax.ws.rs.PathParam("id") int id){		
		javax.ws.rs.core.Response response = null;

		Contact c = this.contactDao.getContactById(id);
		System.out.println("ContactRestService.getContactById()" + c);
		if (c == null){
			response = javax.ws.rs.core.Response.status(404).build();
		} else {
			ContactDto dto = this.createDto(c);
			response = javax.ws.rs.core.Response.ok(dto).build();
		}
		
		return response;
	}
	
	// GET http://localhost:8080/contacts-rest/rs/contact
	@javax.ws.rs.GET
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public javax.ws.rs.core.Response getAllContacts(
			@javax.ws.rs.PathParam("id") int id){		
		javax.ws.rs.core.Response response = null;
		
		java.util.List<? extends Contact> contacts = this.contactDao.getAllContacts();
		java.util.ArrayList<ContactDto> dtos = new java.util.ArrayList<ContactDto>();
		for (Contact contact : contacts){
			dtos.add(this.createDto(contact));
		}
		javax.ws.rs.core.GenericEntity<java.util.List<ContactDto>> entity
			= new javax.ws.rs.core.GenericEntity<java.util.List<ContactDto>>(dtos){};
		response = javax.ws.rs.core.Response.ok(entity).build();	
		
		return response;
	}
	
	// DELETE http://localhost:8080/contacts-rest/rs/contact/{id}
	@javax.ws.rs.DELETE
	@javax.ws.rs.Path("{id}")
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public javax.ws.rs.core.Response removeContact(
			@javax.ws.rs.PathParam("id") int id){		
		javax.ws.rs.core.Response response = null;
		Contact c = this.contactDao.getContactById(id);
		System.out.println("ContactRestService.getContactById()" + c);
		if (c == null){
			response = javax.ws.rs.core.Response.status(404).build();
		} else {
			ContactDto dto = this.createDto(c);
			this.contactDao.removeContact(id);
			response = javax.ws.rs.core.Response.ok(dto).build();
		}
		
		return response;
	}
	
	// POST http://localhost:8080/contacts-rest/rs/contact
	@javax.ws.rs.POST
	@javax.ws.rs.Consumes(javax.ws.rs.core.MediaType.APPLICATION_XML)
	@javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_XML)
	public javax.ws.rs.core.Response createAndSaveContact(ContactDto input){		
		javax.ws.rs.core.Response response = null;
		String countryAbbreviation = input.getCountry().getAbbreviation();
		java.util.List<String> tagValues = new java.util.ArrayList<>();
		for (TagDto t : input.getTags()){
			tagValues.add(t.getValue());
		}
		Contact c = this.contactDao.createAndSaveContact(input.getFirstname(), input.getName(), input.getEmail(), countryAbbreviation, tagValues);
		if (c == null){
			response = javax.ws.rs.core.Response.status(500).build();
		} else {
			ContactDto dto = this.createDto(c);
			response = javax.ws.rs.core.Response.ok(dto).build();
		}
		
		return response;
	}
	
	private ContactDto createDto(Contact c){
		CountryDto countryDto = null;
		if (c.getCountry() != null){
			countryDto = new CountryDto();
			countryDto.setId(c.getCountry().getId());
			countryDto.setAbbreviation(c.getCountry().getAbbreviation());
			countryDto.setName(c.getCountry().getName());
		}
		
		ContactDto dto = new ContactDto();
		dto.setId(c.getId());
		dto.setFirstname(c.getFirstname());
		dto.setName(c.getName());
		dto.setEmail(c.getEmail());
		dto.setCountry(countryDto);
		
		 for (Tag t : this.contactDao.getTagsByContact(c.getId())){ 
			 TagDto tagDto = new TagDto();
			 tagDto.setId(t.getId()); 
			 tagDto.setValue(t.getValue()); 
			 dto.getTags().add(tagDto); 
		}
		 return dto;
	}
	
}
