import static spark.Spark.*;
public class Main {

	public static void main(String[] args) {
		
		//port(8080);
        //simple get
		get("/hello", (req, res) -> "Hello World");
		
		//simple post
		post("/hello", (request, response) -> 
				"Hello World " + request.body()
		);		
		
		//simple get with response status
		get("private", (request, response) -> {
			response.status(401);
			return "Go Away!";
		});
		
		//simple get with path param
		get("/users/:name", (request,response) -> "Selected user: " + request.params(":name"));
		
		//simple get with path param that returns a xml text
		get("/news/:section", (request, response) ->{
			response.type("text/xml");
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>";
		});
		
		//simple get with path param that returns a json 
		get("/news/json/:section", (request, response) ->{
			response.type("application/json");
			return "{ news:" + request.params("section") + " }";
		});
		
		//simple get that returns a forbidden message
		get("protected", (request, response) -> {
			halt(403, "No way man!");
			return null;
		});
		
		get("/redirect", (request, response) ->{
			response.redirect("/news/json/world");
			return null;
		});
		
		get("/", (request, response) -> "root");
				
	}
}
