Make a reactjs (javascript & vite ) Gospel web app, only frontend. use pnpm
1. use react-bootstrap to create some components where possible.
2. use light background all over the app.
3. use #eb8f34 for buttons and titles
4. use the darker version of #eb8f34 onthe navbar and footer, also make lighter versino
	of #eb8f34 for the navbar and footer text.
5. use all text stored in  a global file, all text and call each text from it
6. file structure:
	src/App.jsx //call Home.jsx here
	src/home/Home.jsx // Header.jsx, Slide.jsx, EarlyNew,jsx,Navbar.jsx and Footer.jsx are invoked here
	src/home/Header.jsx // description 2/3 columns of intro about gospel
	src/home/Slide.jsx //three randome sclideshow
	src/home/EarlyNew,jsx
	src/navbar/Navbar.jsx
	src/footer/Footer.jsx
7. make other contents you think are good for a Gospel website
8. On the Header.jsx add a phone address on left, tel and email on center , both language (Anglish & French)
	login on the far right
9. Add an absolute-positioned whatsapp button that comes few seconds after everything has loaded
	the points to this number +250 788 630 838.
10. change the text colot from navbar and footer to #fff
11. make the header content one-liner and on the telephone put +250 788 630 838 and email put: Igihecyubuntu@gmail.com
	make no space between the header and navbar.
12. between the header and navbar 
13. in the header:
	 remove the login button, 
14. scan all and all the text from <p> and all other plain text from elements to come from a single file.
15. update and change the website title to be igihecyubuntu. chagne all occurences

16. change all occurences of citty to be Kigali, address(Street) name Kicukiro, remove the street number
17. capitalize the title from navbar only
18. change contactInfoStyle padding
19. make the <Row from Header.jsx on line 36 and <Row> on line 58 in separate files one called HeaderTop.jsx,
	another Overview.jsx call them both from Home.jsx, make necessary changes on states values.
20. make the container from Overview.jsx have two columns one 3 another 9 display the bible-header from asset
	folder and another make it allocate 4 cards woth title and a smaller titell of the and description
	writer, with a clickable title.
21. make sure that the common styles are written in a global css file
22. make all texts in <p> and headers and all other html elements come from data/texts.js with french/English
	translation
22. by keeping all texts in <p> and headers and all other html elements come from data/texts.js with french/English
	translation and keeping css in a global file and by making sure that the responsiveness stays operational, do these:
	- make the footer a bit more dark version if the current color
	- check if all texts in text.js have three languages
	- double check if all translations are well done on the earlyNews in texts.js, 
	- add a third image on the slide
	- add on Kinyarwanda from navbar, add it in data/texts.js translate as accurately as you can
	- make the Slide from slide.jsx slide with two images from asset, one is prayer1.png another is prayer2.png
	- keep all features of the current card, on in EarlyNew.jsx,   change the card to a blog-full-featured card with 
		comments capability with 3 level sub comments display
	- from navbar remove Sermons and events menus
	- remove {getText('buttons.love', currentLanguage)} and add writer's name
	- on each card from Earlynew.jsx a love button and make it clickable, i removed a header from card header so keep it that way
	- on the same (news-card-header) change the color to pure dark
	- remove the background colot from news-card-header in EarlyNew.jsx and i have changed its text to be  {news.title[currentLanguage]} keep it that way
	- in each card's body increase the text (randomely) related to Gospel to be a bigger paragraph
	- in the carf body make a rwo that has 2 and 10, shift the current text of each card in its body to the 10 col
	- make one card per row and full
	- install using pnpm: npm install --save react-icons-kit
	- add the icon by imprting: import {book} from 'react-icons-kit/icomoon/book'
	- and use it like this: <Icon size={'100%'} icon={home}/> inside <Col xs={2}> on 48 from Earlynew.jsx
	

	- on line 35 of EarlyNew.jsx make the cards to be 6
	- increase the padding of <Card.Header on line 38 inside EarlyNew.jsx
	- increase the line-height of vision-text a bit more
	- make the vision-text text a bit smaller
	- Great, make it a bit more gray again
	- change the text from vision-text to a bit gray
	- cross check all css local or globalin VideoFospel.jsx and make sure the vision-text is not overriden, it has to
		be #fff
	- double check the text in the overlay is still behind make it appear infront
	- update the phone to be this: +250 788 630 838‬ / ‪+250 785 462 584‬

	- the text on line 39, make it come in front of overlay not behind
	- put back the bottom part of the overlay
	- you changed the overlay in the video component, keep the Aboutus compoent btu revert the changes you made for
	text in the overlay, the layout and size
	- similarly as it it is done for contactus create a new component  create the About us and in it add 
		our mission:
		Our mission is to proclaim the Gospel of the Grace of God as revealed in Christ Jesus, helping people unlearn religion and discover their true identity in Christ. We are committed to teaching the truth of God's word rightly divided, equipping believers to grow in the knowledge of Christ and walk in the liberty of the Gospel.
		our vision
		Our vision is to raise a generation rooted in the revelation of Christ, who will impact the world with the truth of the Gospel, living lives free from guilt, fear, and condemnation, and manifesting the love and character of God in every sphere of life.
		What We Believe
		We believe that Jesus Christ is the full expression of God.

		We believe that salvation is by grace through faith, not by works.
		We believe the Bible must be interpreted in the light of Christ.
		We believe that the believer is eternally saved and one with Christ.
		We believe in teaching the Word rightly divided without mixture of law and grace.
		We believe that the Gospel reveals God's love, not His anger.
		We believe in spiritual growth through accurate knowledge of Christ.
	
	-  make the footer a dark version of the current bgcolor
	- create a separate contactu page and make invokable from navbar, add the contactus information, style the ui with
		the bible image in a good lookig manner.
	- again i bit more bigger
	- now increase the size of the image a little more bit
	-  style image from the row, so that it goes outside the row a little bit
	- set the picture in that row to be that of the bible from asset
	- keep that row inside the overlay at the bottom.
	- in the video component i want to to a row with fixed height of 100px, at the boottom that has a small picture column, a title and description
	on another column and on a third column to have a scrolling text. 
	- make the text in the video component responsive on small devices
	- is it possible to make the first-played video auto loop
	- create an dark overlay on the video and add this text on white in large font:
		Our vision is to raise a generation rooted in the revelation of Christ, who will impact the world with the truth of the Gospel, living lives free from guilt, fear, and condemnation, and manifesting the love and character of God in every sphere of life.

	- remove margin bottom and padding bottom from  HeaderTop component
	-  change the height to 3/4
	- make the video component have no padding or margin on top, make the height 1/2 of the viewport and the width
		the corresponding to that 1/2 height.
	- remove the tittle in the video component. make the youtube urls randomize from 
		https://www.youtube.com/watch?v=TThbYNAUGv8&list=RDTThbYNAUGv8&start_radio=1
		and https://www.youtube.com/watch?v=5VCYjzyTNCw&list=RDcJ9OJbknGgE&index=4
	- make teh video the second after headerTop in Home.jsx
	- Create another component and call it videoGospel and make a react vieo player suitable to play a video from this youtube link:
		https://www.youtube.com/watch?v=5VCYjzyTNCw&list=RDcJ9OJbknGgE&index=4
	- make the video auto start.
	- make the componet full screen horizontally, keep the volume at 0.
	- change the content of <p>in the third <Row> in Overview.jsx to:
		Our mission is to proclaim the Gospel of the Grace of God as revealed in Christ Jesus, helping 
		people unlearn religion and discover their true identity in Christ. We are committed to teaching
		the truth of God's word rightly divided, equipping believers to grow in the knowledge of Christ 
		and walk in the liberty of the Gospel. 
	- Also change the <h1> from the Overview.jsx to Our Mission
	- change the button for French/Language to a dropdown. 

	- change the content of
	- make the cards from the current second <Row> from Overview.jsx have no shadows and make them have side borders 
		only right and left borders
	- move the first <Row> from Overview.jsx belows the <second> and then second will the first
-	- correct header.address and header.tel to display the correct contents	
	
11. make another full screen black slideshow with 4 images to slide and some texts accrodingly


sunday service change to preachings
urubyuriko ruveho
remove the "community outreach" replace by testimony

1. Inyigisho-- preaching
2. bible study.
3. tetimony.
4. 





in the app application , which is a spring boot app, i would like to do the following:
1. configure the database as the application ahs the jpa dependency
2. By referring to the "lawyer_backend", do the following
	1. Create the folder structure in the "app" application as it is in the "lawyer_backend"
	2. Create the models , but only create the jwt and users authentication models
	3. create the corresponding repositories, controllers and services as they are in the "lawyer_backend"
	4. if there are other components in the lawyer_backend" related to user authentication and jwt please add on the "app" application
3. Dont ask to run the "app" appliaction, just check all the requirements and make sure  that all are set on the "app" backend
4. check the correspondingsecurity dependencies and apply them in the "app" application
5. make sure that the pom tags is well structured
6. Supposed that the app is running, check that the login is working
Dont ask to run the "app" appliaction, just check all the requirements are well implemented
7. make sure that when one los in in "app" application returns the token, so that it can be used on the frontend
8. make the necessary files or use the existing: entity, repository, service direcotries and make sure the user can be edited, review if all is okay
9. make the necessary files or use the existing and make a way to only change the password by checking the current logged in user

by going to "igihe cyubuntu" and by keeping all texts in <p> and headers and all other html elements come from data/texts.js with french/English
	translation and keeping css in a global file and by making sure that the responsiveness stays operational, do these:
10. now in the "igihe cyubuntu" app, i want to create the admin directory inside the /src
	1. create a "users" menu with sub menu "create", "view users", "change password"  
	2. create the corresponding components for each submenu, create a dashboard where admin can see the users pane for now
	3. create a connecton file calling Conn.jsx and connect to the "app" application, test it
	4. using this Conn.jsx make the necessary code to create, view users, change the password in the components created under /src/admin
	5. also create a file called Repository.jsx that will hold only the select requests, this also will need Conn.jsx
	6. Also create another file called NewValues.jsx that will hold the post requests only, this also will need the Conn.jsx
	7. In the same way create another file called UpdateValues.jsx that will hold the put requests, this also will need the Conn.jsx
	8. test that each request is working and let me know

	9. all is working now, i need to make the login on the homepage functional and redirect to login, and if there is no login create it it is own folder, 
		called "login", if you create it, use the conn.jsx and NewValues.jsx
	10. Make the dashboard to use the language selected from the homepage, so that it stays consistent
	11. Now change all admin to be set in English only, and keep the homepage components and pages to multiple languages
	12. change the language to English in the placeholder in the Dashboard inside the text inputs also
