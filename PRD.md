Act like an experienced developer and in the "app" project, 
	In the models  where id should always be GenerationType.IDENTITY
	=> request should be received in controllers and processed from services
	=> i want to add images folder that will keep uploaded images, so find the appropriate location for it
	=> Create these the models
		=> blog(id, tile(String),content(@Lob),status)
		=> comment(id(GenerationType.IDENTITY), title(string),content(@Lob), doneBy(long))
		=> picture(id, type, path, alt_text, dateTime, doneBy(long))
		=> category(id, name, done_by(long),doneBy(long))
		=> postComment(postId(long), commentId(long))
		=> postPictures(postId(long), picture(long))
		=> postCategory(postId(long), categoryId(long))
		=> post(id, dateTime,blogId, doneBy(long))
		=> likes(id, postId,dateTime,doneBy(long))
		=> FOREIGN KEYS
			=> postComment is a joint table for "post" and "comment", many to many
			=> postPictures is a joint table for "post" and pictures, many to many
			=> postCategory is a joint table for "post" and category, many to many
			=> likes has a one to many relationship with post
			=> blog has a one to many relationship with post.
		
	=> except for doneBy in  post, every other doneBy(long) is a normal field with long type, not a foreign key.
	=> CREATE CUSTOM EXCEPTION Class in a "exception package"
		=> it will be used in communication with frontend
	=> RESPONSES TO FRONTEND
		=> "200" should return "Successfully retrieved" plus the table name
		=> "401" should return "invalid credentials for login" and "unauthorized for other endpoints"
		=> "400" should return invalid inputs
	=> SWAGGER
		=> add @Operation with summary and description with suitable contents
	=> OTHER EXCEPTIONS
		=> Use try-catch in controllers
	=> REPOSITORIES
		=> Use projection interface for all repositories
		=> List<Object[]> should be used for dashboard responses only
	=> TEST
		=> TEST all endpoints one by one
		=> if a test fails check the root cause
		=> if all tests pass review the requirements one more time and let me know...
	-- have all tests been done on all endpoints, make sure you login, dont 
	-- can you login in the backend given username to be admin and password admin123
	


	- -curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"admin123\",\"email\":\"admin@igihecyubuntu.com\",\"fullName\":\"Administrator\",\"phoneNumber\":\"+250123456789\",\"gender\":\"M\",\"accountCategory\":\"ADMIN\"}"
	-- curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"member\",\"password\":\"member123\",\"email\":\"member@igihecyubuntu.com\",\"fullName\":\"Member User\",\"phoneNumber\":\"+250123456789\",\"gender\":\"M\",\"accountCategory\":\"MEMBER\"}"
	-- curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json"    -d "{\"username\":\"client\",\"password\":\"client123\"}"


	- In the Navbar.jsx the login button is not redirecting to login component, lease fix it
	- after check if the login endpoints are working well




in the dashboard, in the left side menu, change the menu as collapsible by the main menu, so make sure the users can be folded in a toggle
	manner


  


As a pro frontend developer, using the same db structure as below:
		=> blog(id, tile(String),content(@Lob),status)
		=> tile(String),this will be a rte,status
		=> comment(id(GenerationType.IDENTITY), title(string),content(@Lob), doneBy(long))
		=> picture(id, type, path, alt_text, dateTime, doneBy(long))
		=> category(id, name, doneBy(long))
		=> postComment(postId(long), commentId(long))
		=> postPictures(postId(long), picture(long))
		=> postCategory(postId(long), categoryId(long))
		=> post(id, dateTime,blogId, doneBy(long))
		=> likes(id, postId,dateTime,doneBy(long))
and inline with the backend, do the necessary forms for each table in the dashboard:
	=> blog(tile ,content), the status will be automatic in the backend on the Blog model using @prepersist initialized as "Pending".
	for the content there has to be installed these tools:
	npm install @ckeditor/ckeditor5-react @ckeditor/ckeditor5-build-classic axios dompurify
	=> save picture(type, path, alt_text) where the path will be initialized by backend , so in the backend there should be a "images"
		 folder to hold them. Here the frontend should allow a user to browse picture on his local machine.
		 Also the front should be able to send the data along with multiple images, so the browse element should allow browsing 
		 multiple images. it would be better if the picture can be visualized before submitting.
	=> save the category(name) 
	=> save post, which automatically saves postCategory, and automatically saves postPictures to be saved from admin dashboard, on this 
		form the user wil select images from the server. those are the images save from the save picture form. the category will be 
		selected from a dropdown menu
	=> All the dateTime fields should be automatiacally be done from their corresponding backend models using @perpersist, so update the models 
		(picture, post,likes)
	=> All the doneBy should be come from frontend as the logged in user id, so check if the login holds the user id 
		in the session, but here use react-way

	=> MENU STRUCTURE:
		=> after user main menu, add "pictures" where this will have add edit and delete as submenu, "post categories" where this will
		also have add,edit and delete as submenu, "blog" as main menu and this has (add, edit and delete) as submenu
	=> Follow the list and implement the functionalities described.
	=> Once you finish, review one more time.

		


	Perfect, 
	=> Make the left side menu folded and leave post expanded by default
	=> Also, test all rerievals and make sure that all requests are not failing
	=> make the post go on top of other menus
	=> Check on the request being done on http://localhost:8080/api/blog, how it is bringing: 
		{"error":true,"message":"Internal server error","status":500}
	=> Make the left side menu scrollbar and make its container's height limited to the viewport of the whole page in 
		admin dashboard.
	=> on the Use the rich text editor  it is not appearing, there is appearing this message:
		Use the rich text editor to format your blog content. Status will be set to PENDING automatically.
	=> now the issue is that when i click on bold button it adds a tag like this: <strong>some contentt</strong>
		to all other buttons too i am not getting result live
	=> great, now make the left side menu have a fixed position and height so that it does scroll alogn
		 with other elements on the dashboard,but let other content to be scrollable
	=> also make a button to toggle the whole sidemenu hidden and show.
	=> The button that toggles is working but not working, now giving a white color
	=> on the richtext editor, give a fixed height too
	=> add a list in the toolbar also
	=> the two list buttons are added but not contents inside.




