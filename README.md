
**In RateLimiterApplication.java:**

Create a container for your filter (FilterRegistrationBean), which is Spring's way of saying:

filter.setFilter(new RateLimittingFilet());
   -> This says: "Here's the filter (the logic to check and limit the number of requests from an IP)."

filter.addUrlPatterns("/api/*");
   -> This tells Spring: "Only apply this RateLimitter to any request starting with /api/ (like /api/hello, /api/test, etc)."

Return the filter bean so Spring Boot will run it for you during app startup.


-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**In the RateLimittingFilet.java:**

**doFilter**
   -> The is the main gatekeeper method. Every time someone (a user or system) sends a request to your server, this method gets called before anything else happens.

**HttpServletRequest request = (HttpServletRequest) servletRequest;
HttpServletResponse response = (HttpServletResponse) servletResponse;**

   -> These two lines just convert generic objects into more useful ones. Think of it like changing a general "box" into a "parcel" and "envelope" that you can actually open and read.
   -> request lets you read details about the request (like who sent it, what URL, etc.)
   -> response lets you send something back to the person who made the request.

**String ipAddress = request.getRemoteAddr();**
   -> This line gets the IP address of the person or system that sent the request.
      Like saying:
      "Who is knocking on my door? Oh, it’s someone from 192.168.1.1"

**numberofipaddress.putIfAbsent(ipAddress, new AtomicInteger(0));**
   -> Checks if we’ve seen this person (IP address) before. If not, we add them to a list and start counting their visits.
   "If I haven't seen this person before, start their count at 0."

**AtomicInteger atomicInteger = numberofipaddress.get(ipAddress);**
   -> Fetches the counter (how many times that person has already visited).
      So now you can check how many times this person knocked on your door.

**int requests = atomicInteger.incrementAndGet();**
   -> Increases the count by 1.
      So, if someone sends another request, we say:
      "Okay, this is your 3rd visit now."

**if(requests > MAX_NUMBEROF_REQUESTS)**
   -> Checks if the visitor has gone over the allowed number of visits (e.g., 5).

**response.getWriter().write("Too many Requests");**
   -> Along with the error code, it also writes a message:

**filterChain.doFilter(request, response);**
   -> If the number of requests is within the limit, this line says:
   "You're good to go! Let the request pass to the next handler (like the controller that gives the actual response)."
---------------------------------------------------------------------------------------------------------------------------------------------------------------------

Build the project and you can test it in PostMan:

Make a get request to http://localhost:8080/api/hello

send the request more then 5 times. You will see on 6th request you will be seeing Error message. This is not limited to any minutes. 
