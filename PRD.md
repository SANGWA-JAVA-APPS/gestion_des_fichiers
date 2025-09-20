like a pro software engineer and pro web dev,
	using the following tools: react-bootstrap to create some components where possible.
use all text stored in  a global file /data/texts.js with french/English translation
	 all text and call each text from it,and by making sure that the responsiveness stays operational
	make sure all text from <p> and all other html elements.
create a react/vite app, with javascript template
	 in this folder: D:\Apache\DEV\REACTJS\gestion_des_fichier, using pnpm
1. change the default homepage from App.jsx, change the default code to a four-pane
	menu grid.
	The grid has to have three panes, the panes should be wider than their
	height.
	the panes will act like a navigation of the system
2. the text of the panes should be as the following:
	LOCATION, USERS, DOCUMENT
3. use react-bootstrap to create some components where possible.
4. use light background all over the app.
5. use all text stored in  a global file /data/texts.js with french/English translation
	 all text and call each text from it,and by making sure that the responsiveness stays operational
	make sure all text from <p> and all other html elements.
6. make sure that the common styles are written in a global css file (.scss)
	here with reference of components tree, nest the styles like a pro
	usinig mixins for reusability
7. name of the website is GESTION DES FICHERS

8. for grid and responsiveness use the default react-bootstrap behavior and .scss
	for the custom css, so on homepage make sure the three panes are created in
	a Container-row-col kind of style.
9. Find a way to make the best reponsiveness for small devices.
10. make sure that all web app content are centered in a react-bootstrap container and 
	stucture its content using bootstrap style
11. the whole web layour should be full in 
 if you finish review the list one more time and let me know.
12. you fixed the global state from the title on top, but the layout for english is still not full
	when i click the language button the enlgish layout is coming not full 
13. i have changed the default language to en, but the header in App.jsx on line 14 is not full, please fix it
14. i saw the issue, the header expands based on the contents, so it should have display block
16. remove div from App.jsx  and header, just use React bootstra Container-Row-Col style
17. please redo the homepage by only making the header and use a simple React bootstra Container-Row-Col style,
	remove all header's custom css 
18. check if there are some bootsstrap customized styles and remove all of them
19. why is Row not gettin full with inside container in app.jsx on line 12
20. i have change the header to be on top, but it is in the middle, please fix it
21. i saw the issue was in index.css which had some styles that made everything in App.jsx to be vertically centered
	now it is ok, now do the following:
	- reapply the header a it way, 
	- reaapply the  three card, looped and use the global.scss where necessary, but dont customize bootstrap
	where not necessary. so the global .scss will again be imported in App.jsx
	- This time make the card icons horizontally placed on the left and the text on the right

1. create a spring boot app using the latest version which is suitable for java 17
	install the fillowing dependencies:
	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>
	   <dependency>
		   <groupId>org.mariadb.jdbc</groupId>
		   <artifactId>mariadb-java-client</artifactId>
		   <version>3.3.3</version>
	   </dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.3.0</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.datatype</groupId>
			<artifactId>jackson-datatype-hibernate6</artifactId>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.30</version>
			<optional>true</optional>
		</dependency>
		<!-- JWT Dependencies -->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.11.5</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.11.5</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.11.5</version>
			<scope>runtime</scope>
		</dependency>



2. note i have two projects inthe same workspace, i want to:
	- have all properties implemented in lawyer-backend to also be
	implemented in gestindesficher app
	- i also want to make sure all jwt authentication, swagger functionalities
	are implemented as they are implemented in lawyer-backend.
	- follow the same folder structure but instead of dtos use 
	project interfaces to be used in repositories
	- review the gestindesficher app and test if the swagger is working
	- The swagger is working perfectly but why isn't 'http://localhost:8104/api/auth/login'
	returning a jwt token ? is there another location where it is being returned?
	- now test the login and let me know if the token is being generated
3. i would like to group the current swagger endpoints to be grouped in a tag called "user"

4. also check if the feature of updating the password by user_id in accouts table,
5. check of there a functioality of retreiving all users. 
6. check of there is a functionality of viewing all users
7. i have now included an additional app in the workspace called gestion_des_ficher which is a frontend app that has to be integrated
	to the gestiondesfichier backend.
	- so start the integration by creating a login from frontend, all request will use axios so ceck of the package is installed
	- the requests should be in a folder from this path: src/services
	- by making the request setup in frontend make sure the reusability of the url path is present, for example the base path
		which corespond to the backend context path (Spring boot)
	- all the inserts should be in a file located here src/services/Inserts.jsx
	- All the get requests should be from src/services/GetRequests.jsx
	- all the update requests should be form src/services/UpdRequests.jsx
	- Start by login and make sure the token is maintained across all frontend components.
	- Change the frontend so that the login is the homepage
	- Make sure that all other componens are secure
	- Upon logging in, make sure the app redirects to the page that has the three panes located in App.jsx
	- Create the separate components for location another for user and another for document, where each component is called upon
		clicking its corresponding pane.
	- On each redirection, create a common menu that keep Location, user and document, so that the navigation os maintained
	- If all is set review this list and test one more time
	
8. as a pro java spring boot developer, Now i want you to create the necessary models, projection interfaces, repositories, services and 
	utils directories. find the common functionalities and make the necessary common classes in
	a package called "common"
	- However, now i want to use  a dedicated domain-based structure called "location", which will have its directories: models, projection interfaces, repositories, services
	- And another called document which will also its own models, projection interfaces, repositories, services
	- You will implement the the "location" based in the followiing entities:
		- country(id,name)
		- entity(id, name, countryId) where countryId is foreign key
		- module(id.name, entityId) where entityId is foreign key 
		- section (id,name, moduleId) where moduleId is foreign key 
	- know that the app is already running , so if all is done, review the list and test
	- if all tests pass, let me know
	
9. make the login pane a bit larger
10. in the frontend, make the title header a bit smaller in terms of font size and a little bit of the padding
11. Upon logging make the three tabs: location, user and location changed to a react-bootstrap navbar
	so the navbar will have submenu
	- the location will have country, entity, modules and sections
	-  the user will have account and roles
	- the document main menu will have some sub menus that i will tell you shortly.
12. make sure that eack of the submenu get its corresponding react component under /src/component,
	here each submenu will also have is folder under component.
	veryfy all each import if it is correctly imported correctly.
13. with reference to the backend database structure, make the corresponding forms in the frontend 
	"gestion_des_ficher"
	- for each form that has a foreign key field use a dropdown for that field, here check each table in the database
	- each form will use some insert request from services/
14. the component of each submenu should have a form that is structure like the models in the backend: com.bar.gestiondesfichier.location
	also you can refer to mcp database called:gestiondesfichier_db
15. now review the models fields from backend and make sure they align with frontend form fields
16. also create the necessary requests in frontend: /services/ to retrive data and load them in each submenu's component
17. i am getting errors on loading the data on each submenu
	on country Management i am getting an error alert: Failed to load countries: No static resource api/location/countries.
	on Entity Management i am getting an error alert: Failed to load entities: No static resource api/location/entities.
	on Module Management i am getting an error alert: Failed to load modules: No static resource api/location/modules.
	on Section Management Failed to load sections: No static resource api/location/sections.
18. also i want to replace  locationServices.jsx by Inserts.jsx, GetRequest.jsx, UpdRequests.jsx review all forms and change the 
	imports
19. now fix the request for user and its submenu as you fixed the ones from location from its submenu
9. make sure the design still follows the Container-Row-Col layout styles and redo the login
		-



-- on 18 september 2025 prompts:
-----------------------------------
- for each query in the repositories in the backend, make the pagination to be defaulted to 20 records per page, for this change the controller and 
	services accordingly
- fix the post request, i am getting this error: Failed to save country: Failed to create country while saving the country
- Observe the currect folder structure and keep the domain-based backend folder structure 
- Use a single package called "document" where it will hold models, repositories, controllers and services
- Use a common functionlity to be included in the common package found in com.bar.gestiondesfichier.common
- follow the logging of @slfj
- Return these https repsonses: 200 for a secceful request, 400 "check inputs",  403 "the your session has expired, please login again"
	- here make sure the frontend detects the response from backend and  alerts with the same backend message
	- those https reponses should be checked from frontend in all requests, post , get and put
- Follow the controller-service structure
- Use try and catch wherever possible in the services or controllers, here pick the best practice
- Keep using projection interfaces instead of dtos and make necessary changes in the repositories queries
- Document all endpoints for swagger
- Make a centralized @CrossOrigin tp allow frontend url, check if the "config" package exists and set the crossorigin up there
- in "config" package create  a 404 request not found config to return a specific message
	- for this, in frontend create common a response checker, which checks the returned response.
- 



In frontend i would like to:
	1. create a dashboard
	2. the system should redirect to the dashboard upon logging in.
	3. by referring to the backend database setup ,  check the database structure and create extensive dasboard with panes
	4. By following the existing backend domain-based structure and repositories queries, as pro crate queries that pulls reports of 	
		a file management system.
	5. make sure that the reports include files




In the database definition, i would like to add these models where:
	
	each controller will have pagination with default of 20 records.
	use the projection interfaces instead of dtos
	Make a centralized @CrossOrigin, 
	Use a single package called "document" where it will hold models, repositories, controllers and services
	Use a common functionlity to be included in the existing common package found in com.bar.gestiondesfichier.common
	follow the logging of @slfj
	Therefore,check the tables names that may conflict with mysql keywords create these models which will reflect the underying databse tables:
	=> docstatus             	(id, name),  the defaults are: applicable, suspended, remplaced, annulé, en_cours, acquis, vendu, transféré, litigieux, validé
	=> section_category   		(id, name), the default values are: financial,procument, hr, technical, IT, real Estate, Shareholders, legal, quality, HSE,	
		equipment, drug and alcohol, incident news letter, SOP
	=> norme_loi  			(id, date_time, doneby, docId, référence, description, date_vigueur, domaine_application, 												statut_id)
	=> comm_asset_land  		(id, date_time, doneby, docId, description, référence, date_btention, coordonnées GPS, emplacement, section_id, 									statut_id)
	=> permi_construction	  	(id, date_time, doneby, docId, référence_titre_foncier, réfé_permis_construire, date_validation, date_estimée_travaux, 									statut_id)
	=> accord_concession  		(id, date_time, doneby, docId, contrat_concession, emplacement, coordonnees_gps, rapport_transfert_gestion, date_debut_concession, date_fin_concession, 				statut_id)
	=> estate  			(id, date_time, doneby, docId, reference, estate_type,  emplacement, coordonnees_gps, date_of_building, comments, 									statut_id)
	=> equipemt_id  		(id, date_time, doneby, docId, equipment_type, serial_number, plate_number, etat_equipement, date_achat, date_visite_technique, assurance, documents_telecharger, 			statut_id)
	=> cert_licenses  		(id, date_time, doneby, docId, description, agent_certifica, numero_agent, date_certificate, duree_certificat, 										statut_id)
	=> comm_comp_policies 	 	(id, date_time, doneby, docId, reference, description, status, version, expiratino_date, sectionid, 											statut_id)
	=> comm_followup_audit  	(id, date_time, doneby, docId, reference, description, date_audit, auditor, num_non_conform, type_conform, percent_complete, doc_attach, section_id, 					statut_id)
	=> due_diligence  		(id, date_time, doneby, docId, reference, description, date_due_diligence, auditor, creation_date, completion_date, doc_attach, section_id, 						statut_id)
	=> comm_third_party  		(id, date_time, doneby, docId, name, location, validity, activities, section_id, 													statut_id)
	=> cargo_damage  		(id, date_time, doneby, docId, refe_request, description, quotation_contract_num, date_request, date_contract,		 								statut_id)
	=> litigation_followup  	(id, date_time, doneby, docId, creation_date, concern, statut, expected_completion, risk_value, 											statut_id)
	=> insurance  			(id, date_time, doneby, docId, concerns, coverage, values, date_validity, renewal_date, 												statut_id)
	=> third_party_claims 	 	(id, date_time, doneby, docId, reference, description, date_claim, department_in_charge, 												statut_id)

RELATIONSHIP
------------
- each table that has docid is in relationship with Document from com.bar.gestiondesfichier.document.model.Document.java
- each table that has statut_id is in relationship with docstatus
- i want to add expiration date in com.bar.gestiondesfichier.document.model.Document.java with LocalDateTime data_type and nullabe=false
- each table that has sectionid is in relationship with section_category table 

DEFAULT DATA
-------------
- in the load of the app, initialize the country for the model: com.bar.gestiondesfichier.location.model.Country, find the world list of countries online.
- for the load of the countries, check if the table is empty first to avoid duplicates

	







	

