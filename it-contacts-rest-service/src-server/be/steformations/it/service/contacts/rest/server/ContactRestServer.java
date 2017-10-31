package be.steformations.it.service.contacts.rest.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;

import be.steformations.it.service.contacts.rest.ContactRestService;
import be.steformations.it.service.contacts.rest.CountryRestService;
import be.steformations.it.service.contacts.rest.TagRestService;

public class ContactRestServer {

	public static void main(String[] args) throws Exception{
		java.net.URI uri = new java.net.URI("http://localhost:8080/contacts-rest/rs");
		
		org.glassfish.jersey.server.ResourceConfig config
			= new org.glassfish.jersey.server.ResourceConfig();
		config.register(TagRestService.class);
		config.register(CountryRestService.class);
		config.register(ContactRestService.class);
		
		org.glassfish.jersey.jdkhttp.JdkHttpServerFactory.createHttpServer(uri, config);
		
		System.out.println("Contact Rest service ready");
		
	}
	
}
